var circleStorage = [];
var markerStorage = [];
var stopMarkerStorage = [];
locationSelector.service('setLocation', function(stopCoordinatesAssembler){
	var instance = this;
	this.setCoordinates = function(coordinate, stopCoordinates, r){
		let newCoord = new google.maps.LatLng(coordinate['latitude'], coordinate['longitude']);
		map.setCenter(newCoord);
		map.setZoom(16);
		
		instance.clearMarkers();
		let coordMarker = new google.maps.Marker({
			position:{lat:coordinate['latitude'], lng:coordinate['longitude']},
			map: map,
			title: 'Center'
		});
		markerStorage.push(coordMarker);
		stopCoordinatesAssembler.showStopCoordinates(stopCoordinates);
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
		if(0<stopMarkerStorage.length){
			for(let i=0; i<stopMarkerStorage.length; i++){
				stopMarkerStorage[i].setMap(null);
			}
		}
	}
});

locationSelector.service('stopCoordinatesAssembler', function(){
	var instance = this;
	this.showStopCoordinates = function(stopCoordinates){
		if(stopCoordinates!=null){
			for(let key in stopCoordinates){
				let coordMap = stopCoordinates[key];
				let stopCoords={};
				for (let coordKey in coordMap){
					let routStr = [];
					let stopList = coordMap[coordKey];
					stopCoords = stopList[0]['stopCoordinate'];
					for(let i=0; i<stopList.length; i++){
						let locationStruct = stopList[i];
						routStr.push(locationStruct['routeName']);
					}
					let stopCoordMarker = new google.maps.Marker({
						position:{lat:Number(stopCoords['latitude']), lng:Number(stopCoords['longitude'])},
						map: map,
						icon: '/nuts/img/marker_blue.png',
						title: stopList[0]['stopName']
					});
					stopCoordMarker.addListener('click', function(){
						let text = document.createElement("div");
						text.appendChild(instance.stopName(stopList[0]['stopName']));
						text.appendChild(instance.stopDistance(stopList[0]['stopDistance']));
						let iterCollection = getById('times').checked ? instance.stopRoutesAndTimes(stopList) : instance.stopRoutes(stopList);
						for(let route of iterCollection){
							text.appendChild(route);
						}
						let infoWindow = new google.maps.InfoWindow({
							content:text
						});
						infoWindow.open(map, stopCoordMarker);
					});
					stopMarkerStorage.push(stopCoordMarker);
				}
			}
		}
	}
	
	this.stopName = function(stopNam){
		let par = document.createElement("p");
		let bol = document.createElement("b");
		let text = document.createTextNode(stopNam);
		bol.appendChild(text);
		par.appendChild(bol);
		
		return par;
	}
	
	this.stopDistance = function(dist){
		let par = document.createElement("p");
		let text = document.createTextNode("Distance from origo: "+dist+" m");
		par.appendChild(text);
		
		return par;
	}

	this.stopRoutes = function*(stopList){
		for(let i=0; i<stopList.length; i++){
			let locationStruct = stopList[i];
			let routeDiv = document.createElement("div");
			routeDiv.setAttribute("class","route_sign");
			routeDiv.setAttribute("style","color:#"+locationStruct['stopTextColor']+"; background-color:#"+locationStruct['stopColor']+";");
			let rou = document.createTextNode(locationStruct['routeName']);
			routeDiv.appendChild(rou);
			
			yield routeDiv;
		}
	}
	
	this.stopRoutesAndTimes = function*(stopList){
		for(let i=0; i<stopList.length; i++){
			let locationStruct = stopList[i];
			let stopTimeDiv = document.createElement("div");
			stopTimeDiv.setAttribute("class","stop_times");
			
			let routeDiv = document.createElement("div");
			routeDiv.setAttribute("class","route_sign");
			routeDiv.setAttribute("style","color:#"+locationStruct['stopTextColor']+"; background-color:#"+locationStruct['stopColor']+";");
			let rou = document.createTextNode(locationStruct['routeName']);
			routeDiv.appendChild(rou);
			stopTimeDiv.appendChild(routeDiv);
			
			let routeTimes = document.createElement("div");
			routeTimes.setAttribute("class","route_times");
			
			let departTimes = stopList[i]['departureTime'];
			if(departTimes.length==1 && departTimes[0].length == 0){
				let spanTime = document.createElement("span");
				spanTime.appendChild(document.createTextNode("n/a"));
				routeTimes.appendChild(spanTime);
			}else{
				for(let time in departTimes){
					let spanTime = document.createElement("span");
					spanTime.appendChild(document.createTextNode("| "+departTimes[time].substring(0,5)+" |"));
					routeTimes.appendChild(spanTime);
				}
			}
			stopTimeDiv.appendChild(routeTimes);
			
			yield stopTimeDiv;
		}
	}
	
});


