var circleStorage = [];
var markerStorage = [];
locationSelector.service('setLocation', function(){
	var instance = this;
	this.setCoordinates = function(coordinate, stopCoordinates, r){//latitude, longnitude
		let newCoord = new google.maps.LatLng(coordinate['latitude'], coordinate['longitude']);
		map.setCenter(newCoord);
		map.setZoom(16);
		
		instance.clearMarkers();
		let coordMarker = new google.maps.Marker({
			position:{lat:coordinate['latitude'], lng:coordinate['longitude']},
			map: map,
			title: 'Given coord'
		});
		markerStorage.push(coordMarker);
		
		if(stopCoordinates!=null && 0<stopCoordinates.length){
			for(let i=0; i<stopCoordinates; i++){
				let stopCoord = stopCoordinates[i];
				let stopCoordMarker = new google.maps.Marker({
					position:{lat:stopCoord['latitude'], lng:stopCoord['longitude']},
					map: map,
					icon: '/nuts/img/marker_blue.png',
					title: stopCoord['stop_name']
				});
			}
		}
		
		instance.addCircle(coordinate['latitude'], coordinate['longitude'], r);
	}
	
	this.addCircle = function(latitude, longnitude, r){
		if(circleStorage.length>0){
			for(let i=0; i<circleStorage.length; i++){
				circleStorage[i].setMap(null);
			}
		}
		let circle = new google.maps.Circle({
			strokeColor: '#0040ff',
			map: map,
			center: {lat:latitude, lng:longnitude},
			fillOpacity: 0.1,
			radius: r
		});
		circleStorage.push(circle);
	}
	
	this.clearMarkers = function(){
		if(0<markerStorage.length){
			for(let i=0; i<markerStorage.length; i++){
				markerStorage[i].setMap(null);
			}
		}
	}
});
