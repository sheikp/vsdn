package SSHKeyFunction;

import com.jcraft.jsch.*;
import java.io.*;

public class SetPolicy{
  public static void main(String[] arg){
    
    try{
      JSch jsch=new JSch();


      Session session=jsch.getSession("training", "10.76.190.84", 22);

      System.out.println("session enterd");
      
      java.util.Properties config = new java.util.Properties(); 
      config.put("StrictHostKeyChecking", "no");
      session.setConfig(config);
     
      session.setPassword("Test@123");
      session.connect(30000);   // making a connection with timeout.

  
      Channel channel=session.openChannel("exec");

      channel.setInputStream(null);

      ((ChannelExec)channel).setErrStream(System.err);

      InputStream in=channel.getInputStream();
     
      String portname = "virbr0"; 
      String qosname = "newqos";
      String qostype = "linux-htb";
      String queuenum = "0";
      String bandwidth = "1000000";
      String cmd = "sudo ovs-vsctl -- set Port " + portname +
            " qos=@" +qosname+ " -- --id=@"+ qosname+
            " create QoS type="+ qostype + " queues="+ queuenum+  "=@q" + 
              queuenum +" -- --id=@q" + queuenum +
         " create Queue other-config:min-rate=" + bandwidth+ " other-config:max-rate=" + bandwidth;
      System.out.println(cmd);
      ((ChannelExec)channel).setCommand(cmd);
      channel.connect();
      String str = readInputStream(in,channel);
      System.out.println(str);
      channel.disconnect();
      session.disconnect();
    }
    catch(Exception e){
      System.out.println(e);
    }
  }

  public void func(String PortName)
  {
	  try{
	      JSch jsch=new JSch();


	      Session session=jsch.getSession("training", "10.76.190.84", 22);

	      System.out.println("session entered");
	      
	      java.util.Properties config = new java.util.Properties(); 
	      config.put("StrictHostKeyChecking", "no");
	      session.setConfig(config);
	     
	      session.setPassword("Test@123");
	      session.connect(30000);   // making a connection with timeout.

	  
	      Channel channel=session.openChannel("exec");

	      channel.setInputStream(null);

	      ((ChannelExec)channel).setErrStream(System.err);

	      InputStream in=channel.getInputStream();
	     
	      String portname = PortName; 
	      String qosname = "newqos";
	      String qostype = "linux-htb";
	      String queuenum = "0";
	      String bandwidth = "1000000";
	      String cmd = "sudo ovs-vsctl -- set Port " + portname +
	            " qos=@" +qosname+ " -- --id=@"+ qosname+
	            " create QoS type="+ qostype + " queues="+ queuenum+  "=@q" + 
	              queuenum +" -- --id=@q" + queuenum +
	         " create Queue other-config:min-rate=" + bandwidth+ " other-config:max-rate=" + bandwidth;
	      System.out.println(cmd);
	      ((ChannelExec)channel).setCommand(cmd);
	      channel.connect();
	      String str = readInputStream(in,channel);
	      System.out.println(str);
	      channel.disconnect();
	      session.disconnect();
	    }
	    catch(Exception e){
	      System.out.println(e);
	    }
  }
  
  private static String readInputStream(InputStream in, Channel channel) throws IOException
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
          buf.append("exit-status: "+channel.getExitStatus());
          break;
        }
        try{Thread.sleep(1000);}catch(Exception ee){}
      }
      return buf.toString();
  }
}
