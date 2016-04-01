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
 * Servlet implementation class HostsServlet
 */
@WebServlet("/HostsServlet")
public class HostsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HostsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		String url = "http://10.76.190.84:8181/onos/v1/hosts";
		
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
