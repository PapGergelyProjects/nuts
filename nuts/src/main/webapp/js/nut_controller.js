
function getById(elemName){
	return document.getElementById(elemName);
}

locationSelector.controller('mapInit', function($scope, $window){
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
		
		$window.map = map;
		$window.searchBox = searchBox;
	}
});

locationSelector.controller('place_select_ctrl', function($scope, $http, setLocation){
	$scope.search = function(){
		let coordinates = searchBox.getPlaces()[0].geometry.location;
		let radius = Number(getById('rad').value);
		let json = {radius:radius, searchCoordinate:{latitude:coordinates.lat(), longitude:coordinates.lng()}}
		$scope.lati = coordinates.lat();
		$scope.lng = coordinates.lng();
		let service = getById('times').checked ? $http.post('/nuts/radius/stop_times', json) : $http.post('/nuts/radius/stop_location', json);
		service.then(function(reponse){
			setLocation.setCoordinates(json['searchCoordinate'], reponse['data'], radius);
		}, function(reponse){
			console.log(reponse);//error
		});
	}
});

locationSelector.controller('coordinate_select_ctrl', function($scope, $http, setLocation){
	$scope.latitude = 47.497912;
	$scope.longnitude = 19.040235;
	$scope.setGivenCoordinates = function() {
		let lat = Number(getById('lat').value);
		let lng = Number(getById('lon').value);
		let radius = Number(getById('rad').value);
		let json = {radius:radius, searchCoordinate:{latitude:lat, longitude:lng}};
		let service = getById('times').checked ? $http.post('/nuts/radius/stop_times', json) : $http.post('/nuts/radius/stop_location', json);
		service.then(function(reponse){
			console.log(reponse['data']);
			setLocation.setCoordinates(json['searchCoordinate'], reponse['data'], radius);
		}, function(reponse){
			console.log(reponse);//error
		});
	};
});

locationSelector.controller('utils_ctrl', function($scope){
	$scope.radius=500;
	$scope.checkValue = function(){
		if(Number(getById('rad').value) > 5000){
			$scope.error_msg = "The radius can be 5000 m, please give a lower one.";
			getById('sh_palce').disabled=true;
			getById('coord_search_btn').disabled=true;
		}else{
			$scope.error_msg = "";
			getById('sh_palce').disabled=false;
			getById('coord_search_btn').disabled=false;
		}
	}
});
