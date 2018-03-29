
function getById(elemName){
	return document.getElementById(elemName);
}

locationSelector.controller('wrapDiv', function($rootScope, $scope, loader){
	$rootScope.loading = false;
})

locationSelector.controller('mapInit', function($rootScope, $scope, $window, click_radius){
	$window.initMap = function(){
		var map = new google.maps.Map(document.getElementById('google_map'), {
			center:new google.maps.LatLng(47.497912, 19.040235),
			zoom:11
		});
		
		var marker = new google.maps.Marker({
			position:{lat:47.497912, lng:19.040235},
			map: map,
			title: 'Center of Budapest'
		});
		
		var searchBox = new google.maps.places.SearchBox(getById('place'));
		
		var mapEventLst = google.maps.event.addListener(map, 'click', function(event){
			click_radius.addClickLst(event, $rootScope);
		});
		
		$window.map = map;
		$window.searchBox = searchBox;
	}
});

locationSelector.controller('place_select_ctrl', function($rootScope, $scope, $http, setLocation){
	$scope.search = function(){
		let coordinates = searchBox.getPlaces()[0].geometry.location;
		let radius = Number(getById('rad').value);
		let json = {radius:radius, searchCoordinate:{latitude:coordinates.lat(), longitude:coordinates.lng()}}
		$scope.lati = coordinates.lat();
		$scope.lng = coordinates.lng();
		let service = getById('times').checked ? $http.post('/nuts/radius/stop_times', json) : $http.post('/nuts/radius/stop_location', json);
		$rootScope.loading = true;
		service.then(function(reponse){
			setLocation.setCoordinates(json['searchCoordinate'], reponse['data'], radius);
			$rootScope.loading = false;
		}, function(reponse){
			console.log(reponse['data']);//error
		});
	}
});

locationSelector.controller('coordinate_select_ctrl', function($rootScope, $scope, $http, setLocation){
	$scope.latitude = 47.497912;
	$scope.longnitude = 19.040235;
	$scope.setGivenCoordinates = function() {
		let lat = Number(getById('lat').value);
		let lng = Number(getById('lon').value);
		let radius = Number(getById('rad').value);
		let json = {radius:radius, searchCoordinate:{latitude:lat, longitude:lng}};
		let service = getById('times').checked ? $http.post('/nuts/radius/stop_times', json) : $http.post('/nuts/radius/stop_location', json);
		$rootScope.loading = true;
		service.then(function(reponse){
			setLocation.setCoordinates(json['searchCoordinate'], reponse['data'], radius);
			$rootScope.loading = false;
		}, function(reponse){
			console.log(reponse);//error
		});
	};
});

locationSelector.controller('utils_ctrl', function($scope, $window){
	$scope.radius=500;
	$scope.checkValue = function(){
		if(Number(getById('rad').value) > 5000){
			$scope.error_msg = "The radius can be 5000 m, please give a lower one.";
			getById('sh_palce').disabled=true;
			getById('coord_search_btn').disabled=true;
			getById('points').disabled=true;
			getById('google_map').setAttribute("disabled", "disabled");
		}else{
			$scope.error_msg = "";
			getById('sh_palce').disabled=false;
			getById('coord_search_btn').disabled=false;
			getById('points').disabled=false;
			getById('google_map').setAttribute("disabled", "enabled");
		}
	}
	$scope.checkClick = function(){
		if(getById('points').checked){
			map.setOptions({draggableCursor:'crosshair'});
		}else{
			map.setOptions({draggableCursor:''});
		}
	}
	
	$scope.redirect = function(){
		$window.location.href = "/nuts/radius/options";
	}
});

locationSelector.controller('process_ctrl', function($rootScope, $scope, $http, $interval){
	/*This needed to track the server side, because the data insertion, refreshment take serious time, while the user cannot do anything, but wait.
	so with this solution, at least the actual process will show. */
	$scope.backgroundProcesses = function(){
		$http.get('/nuts/radius/server_stat').then(function(resp){
			if(resp['data'].length > 0){
				getById('process_list').innerHTML = resp['data'].join("\n");
				$rootScope.loading = true;
			}else{
				$rootScope.loading = false;
			}
		});
	}
	$interval($scope.backgroundProcesses, 2000);
});


/********************		Opt		************************/
adminFunctions.controller('feed_list', function($scope){
	$scope.feeds = [
		'Aachen, Germany',
		'Budapest, Hungary',
		'Huntsville, AL, USA',
		'Indiana, USA',
		'Innisfail QLD 4860, Australia',
		'Israel'
	];
	
	$scope.clk = function(){
		console.log("click");
	}
});