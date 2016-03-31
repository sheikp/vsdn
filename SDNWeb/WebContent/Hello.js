	function Hello($scope, $http) {
		
    	    $http.get('http://10.76.190.84:8181/onos/v1/devices').
    	        success(function(data) {
    	            $scope.greeting = {"id":1,"content":"Hello, World!"};
    	        });
    	    $http.get('http://10.76.190.84:8181/onos/v1/devices').
	        error(function(status) {
	        
	        });
    	}