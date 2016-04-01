<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet" href="assets/css/main.css" />
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>     
<script>

$(document).ready(function(){
	var table1='';
	$.ajax({			
   	 	url: "http://localhost:8080/SDNWeb/HostsServlet",
        type: "GET",               
        //data: {"deviceid": this[0]['id']},
        success: function (data) {                    	
        	var obj = $.parseJSON(data);   
        	var srn = 1;
        	table1 += '<table class="table"><tr><th>Host</th><th>MAC</th><th>IP</th><th>Connected Device Port</th><th>QoS</th></tr>';
        	$.each(obj.hosts, function() {      
        		 table1 += '<tr><td>' + srn + '</td><td>' + this['mac'] + '</td><td>'  + this['ipAddresses'] + '</td><td>' + this['location']["port"] + '</td><td></td></tr>';
        		 srn ++;        		 
        		 });
        	$('#jsonHosts').html(table1);
        },
        error: function (xhr, ajaxOptions, thrownError) { //Add these parameters to display the required response
            alert(xhr.statusText);
            alert(xhr.responseText);
        }
        
    });
	
	
	     
});
	
</script>
</head>

<body>
	<div id="bw" style="padding:30px; background-color:white; width:100%; ">
		<div style="color:maroon"><h3>Hosts</h3></div>
		<div id="jsonHosts"></div>
	</div>
</body>
</html>