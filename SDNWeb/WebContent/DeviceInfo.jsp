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
	
	$.ajax({			
   	 	url: "http://localhost:8080/SDNWeb/DevicesServlet",
        type: "GET",               
        success: function (data) { 
        	var obj = $.parseJSON(data);
        	var table1 = '';
        	var table2 = '';
        	$.each(obj, function() {                
                table1 += '<span><b>Device Type:</b> ' + this[0]['type'] + '<br><b>Hardware Vendor: </b>Edge-Core Network <br><b>Device Model: </b>AS4600-54T<br><b>Software:</b> ' + this[0]['hw'] + ' ' + this[0]['sw'] + '</span>';                
                $('#deviceid').value = this[0]['id'];   
                $.ajax({			
               	 	url: "http://localhost:8080/SDNWeb/PortsServlet",
                    type: "GET",               
                    data: {"deviceid": this[0]['id']},
                    success: function (data) {                    	
                    	var obj = $.parseJSON(data); 
                    	var portcount = 0;
                    	$.each(obj.ports, function() {      
                    		if(this['port'] != 'local')
                    			portcount ++;
                    	});
                    	
                    	table2 += '<h4>No. of Active Ports: ' + portcount + '</h4>';
                    	table2 += '<table class="table"><tr><th>Port Name</th><th>Physical Port Speed(Mbps)</th></tr>';
                    	$.each(obj.ports, function() {      
                    		if(this['port'] != 'local')
                    			{
                    			 table2 += '<tr><td>' + this['annotations']['portName'] + '</td><td>' + this['portSpeed'] + '</td></tr>';		
                    			}     
                        });
                    	$('#jsonPorts').html(table2);
                    },
                    error: function (xhr, ajaxOptions, thrownError) { //Add these parameters to display the required response
                        alert(xhr.statusText);
                        alert(xhr.responseText);
                    }
                });       
            });        	
        	table1 += '<br> <br> <img src="images/switch.jpg" width="250px">';
        	
            $('#jsonDevice').html(table1);
            
        },
        error: function (xhr, ajaxOptions, thrownError) { //Add these parameters to display the required response
            alert(xhr.statusText);
            alert(xhr.responseText);
        }
    });	
	alert(sdeviceid);
	
	     
});
	
</script>
</head>

<body>
	<div id="bw" style="padding:30px; background-color:white; width:100%;  ">
		<div style="color:maroon"><h3>Device</h3></div>
		<input id=deviceid type=hidden value='' />
		<div id="jsonDevice" style="width:50%; float:left"></div>
		<div id="jsonPorts" style="width:50%; float:left"></div>
	</div>
</body>
</html>