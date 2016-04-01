package com.vz.sdn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
@WebServlet("/DevicesServlet")
public class DevicesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @throws IOException 
     * @throws JSONException 
     * @throws ParseException 
     * @see HttpServlet#HttpServlet()
     */
	
	//public String responseJSON;
	
    public DevicesServlet() throws IOException, JSONException, ParseException {
        super();    		
    }
  
        
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		PrintWriter out = response.getWriter();
		String url = "http://10.76.190.80:8181/onos/v1/devices";
		
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
		
		
		
//		JSONParser parser = new JSONParser();
//		Object obj1;  		
//	    		            
//        obj1 = parser.parse(responsestr.toString());
//        JSONObject jsonObject = new JSONObject((parser.parse(responsestr.toString())).toString());
//		
//		System.out.println("The 1nd element of array");
//        System.out.println(jsonObject.get("devices"));
//        //responseJSON = (String) jsonObject.get("devices");
//        System.out.println();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
	}

}
