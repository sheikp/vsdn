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
      String qosId=policy.getQosId(session,portname);
      System.out.println(qosId);
      String queueId=policy.getQueueId(session,qosId);
      System.out.println("queueId " + queueId);
      String queueRate=policy.getQueueMaxRate(session,queueId);
      System.out.println("rate " + queueRate);
      session.disconnect();
    }
    catch(Exception e){
      System.out.println(e);
    }
     
  }
  public String getQosId(Session sessn, String portname)
  {
      String cmd = "ovs-vsctl list port " + portname + " | grep qos | cut -d':' -f2| sed -e 's/\\r //g'";
      System.out.println(cmd);
      String qosId=runcommand(sessn,cmd);
      return qosId;
  }
  public String getQueueId(Session sessn, String qosId)
  {
      String cmd1 = "ovs-vsctl list qos " + qosId + " | grep queues | sed -e 's/\\}//g' | awk -F= '{print $2}'";
      //String cmd1 = "ovs-vsctl list qos " + "15c686ee-23d7-40b3-b07a-fb050fd98e0a" + " | grep queues | sed -e 's/\\}//g' | awk -F= '{print $2}'";
      System.out.println(cmd1);
      String queueId=runcommand(sessn,cmd1);
      return queueId;
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
        try{Thread.sleep(1000);}catch(Exception ee){}
      }
      return buf.toString();
  }
}
