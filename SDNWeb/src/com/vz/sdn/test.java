package com.vz.sdn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;
import org.omg.CORBA.portable.OutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;

import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

/**
 * Servlet implementation class VzServlet
 */
@WebServlet("/VzServlet")
public class test extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @throws IOException 
     * @throws JSONException 
     * @throws ParseException 
     * @see HttpServlet#HttpServlet()
     */
    public test() throws IOException, JSONException, ParseException {
        super();
        

    		
    }
  
        
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

    	String url = "http://10.76.190.84:8181/onos/v1/devices";
		
		URL obj = new URL(url);
		
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		String userpass = "karaf:karaf";
		String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
		con.setRequestProperty ("Authorization",basicAuth);
		
		BufferedReader in = new BufferedReader(
				    		        new InputStreamReader(con.getInputStream()));
				//InputStream in = con.getInputStream();
				
				//HttpURLConnection con = (HttpURLConnection) obj.openConnection();    		
	    		// optional default is GET
	    //con.setRequestMethod("GET");

		//add request header
		
		

		//int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + con.getResponseCode());

//		BufferedReader in = new BufferedReader(
//   		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response1 = new StringBuffer();
//
		while ((inputLine = in.readLine()) != null) {
			response1.append(inputLine);
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//print result
		System.out.println(response1.toString());
		
		response.sendRedirect("test.html");
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
