package com.vz.sdn;

import com.jcraft.jsch.*;
import java.io.*;

//ovs-vsctl list port eth0.1 
//ovs-vsctl list qos 15c686ee-23d7-40b3-b07a-fb050fd98e0a
//ovs-vsctl list queue 7127935d-58e5-43c4-bba8-87751cd690b2

public class SshCommandsAdaptor{
  public static void main(String[] arg){
    
    try{
      SshCommandsAdaptor policy = new SshCommandsAdaptor();

      Session session = policy.createSshSession("root","root","192.168.2.189");
      String portname = "eth0.1"; 
      String qostype = "linux-htb";
      String bandwidth = "2000000";
      policy.createQosToPort(session,portname,qostype,bandwidth);
      policy.removePortQos(session,portname);
      policy.getPortBandwidthRate(session,portname);
      /*
      String qosId=policy.getQosId(session,portname);
      System.out.println(qosId);
      String queueId=policy.getQueueId(session,qosId);
      System.out.println("queueId " + queueId);
      String queueRate=policy.getQueueMaxRate(session,queueId);
      System.out.println("rate " + queueRate);
     */
      session.disconnect();
    }
    catch(Exception e){
      System.out.println(e);
    }
     
  }
  public String createQosToPort(Session sessn, String portname,String qostype, String bandwidth)
  {
      String portname1 = portname.replaceAll("[./]","_");
      String qosname = portname1+"_qos";
      String queuenum = "0";
      String cmd = "ovs-vsctl -- set Port " + portname +
            " qos=@" +qosname+ " -- --id=@"+ qosname+
            " create QoS type="+ qostype + " queues="+ queuenum+  "=@q_" +
              bandwidth +" -- --id=@q_" + bandwidth +
         " create Queue other-config:min-rate=" + bandwidth+ " other-config:max-rate=" + bandwidth;
      System.out.println(cmd);
      runcommand(sessn,cmd);
      return "SUCESS";
  }
  public String getQosId(Session sessn, String portname)
  {
      String cmd = "ovs-vsctl list port " + portname + " | grep qos | cut -d':' -f2| sed -e 's/\\r //g'";
      System.out.println(cmd);
      String qosId=runcommand(sessn,cmd);
      return qosId;
  }
  public String getPortBandwidthRate(Session sessn, String portname)
  {
      String qosId=getQosId(sessn,portname);
      String queueId=getQueueId(sessn,qosId);
      String queueRate=getQueueMaxRate(sessn,queueId);
      System.out.println(queueRate);
      return queueRate;
  }
  public String getQueueId(Session sessn, String qosId)
  {
      String cmd1 = "ovs-vsctl list qos " + qosId + " | grep queues | sed -e 's/\\}//g' | awk -F= '{print $2}'";
      //String cmd1 = "ovs-vsctl list qos " + "15c686ee-23d7-40b3-b07a-fb050fd98e0a" + " | grep queues | sed -e 's/\\}//g' | awk -F= '{print $2}'";
      System.out.println(cmd1);
      String queueId=runcommand(sessn,cmd1);
      return queueId;
  }
  public String removeQueueById(Session sessn, String queueId)
  {
      String cmd1 = "ovs-vsctl -- destroy queue " + queueId ;
      System.out.println(cmd1);
      String result=runcommand(sessn,cmd1);
      return "SUCCESS";
  }
  public String removeQosById(Session sessn, String qosId)
  {
      String cmd1 = "ovs-vsctl -- destroy qos " + qosId ;
      System.out.println(cmd1);
      String queueId=runcommand(sessn,cmd1);
      return "SUCCESS";
 }
  public String removeQosFromPort(Session sessn, String portname)
  {
      String cmd1 = "ovs-vsctl clear port " + portname + " qos";
      System.out.println(cmd1);
      String queueId=runcommand(sessn,cmd1);
      return "SUCCESS";
  }
  public String removePortQos(Session sessn, String portname)
  {
      System.out.println("Inside the removePortQos");
      String qosId=getQosId(sessn,portname);
      String queueId=getQueueId(sessn,qosId);
      String output=removeQosFromPort(sessn,portname);
      output=removeQosById(sessn,qosId);
      output=removeQueueById(sessn, queueId);
      System.out.println("Exiting the removePortQos");
      return "SUCCESS";
  }
  public String getQueueMaxRate(Session sessn, String queueId)
  {
      String queue_bandwidth="ovs-vsctl list queue " + queueId +" | grep other_config | cut -d':' -f2| cut -d',' -f1 | cut -d'\"' -f2";
      System.out.println("queue bandwidht id :" + queue_bandwidth);
      String queuerate=runcommand(sessn,queue_bandwidth);
      return queuerate;
  }
  public Session createSshSession(String userId, String passwd, String hostIP)
  {
    Session session = null;
    try{
      JSch jsch=new JSch();


      session=jsch.getSession(userId, hostIP, 22);

      java.util.Properties config = new java.util.Properties(); 
      config.put("StrictHostKeyChecking", "no");
      session.setConfig(config);
     
      session.setPassword(passwd);
      session.connect(30000);   // making a connection with timeout.
    }
    catch(Exception e){
      System.out.println(e);
    }
    return session;
 }

  public String runcommand(Session sessn, String cmd)
  {

    String result="";
    try{

      Channel channel=sessn.openChannel("exec");

      channel.setInputStream(null);

      ((ChannelExec)channel).setErrStream(System.err);

      InputStream in=channel.getInputStream();
      ((ChannelExec)channel).setCommand(cmd);
      channel.connect();
      String str = readInputStream(in,channel);
      result = str.replaceAll("[\\t\\n\\r ]","");
      channel.disconnect();
    }
    catch(Exception e){
      System.out.println(e);
    }
      return result;
   }
  private String readInputStream(InputStream in, Channel channel) throws IOException
  {
      StringBuffer buf = new StringBuffer();
      byte[] tmp=new byte[1024];
      while(true){
        while(in.available()>0){
          int i=in.read(tmp, 0, 1024);
          if(i<0)break;
          buf.append(new String(tmp, 0, i));
        }
        if(channel.isClosed()){
          if(in.available()>0) continue;
          //buf.append("exit-status: "+channel.getExitStatus());
          break;
        }
        try{Thread.sleep(10);}catch(Exception ee){}
      }
      return buf.toString();
  }
}
