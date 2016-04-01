package com.vz.sdn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;

import com.jcraft.jsch.Session;

/**
 * Servlet implementation class PortsServlet
 */
@WebServlet("/PortsServlet")
public class PortsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PortsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		String DeviceID = request.getParameter("deviceid");
		String url = "http://10.76.190.84:8181/onos/v1/devices/" + DeviceID + "/ports";
		
		URL obj = new URL(url);		
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		String userpass = "karaf:karaf";
		String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
		con.setRequestProperty ("Authorization",basicAuth);
		
		BufferedReader in = new BufferedReader(
		new InputStreamReader(con.getInputStream()));		
		String inputLine;
		StringBuffer responsestr = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			responsestr.append(inputLine);
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		SshCommandsAdaptor sobj = new SshCommandsAdaptor();
		Session session = sobj.createSshSession("training","Test@123","10.76.190.84");
		String qos = sobj.getQosId(session, "virbr0");
		String que = sobj.getQueueId(session, qos);
		String rate = sobj.getQueueMaxRate(session, que);
		
		System.out.println("-----------------------------------------Rate:"+rate);
		
		out.print(responsestr.toString());		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
