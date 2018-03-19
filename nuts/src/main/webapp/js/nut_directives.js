locationSelector.directive('loading', function($rootScope){
	return{
		restric: 'E',
		replace: true,
		template: '<div ><img class="prog" src="/nuts/img/loading.gif"></div>',
		link: function(scope, element, attr){
			$rootScope.$watch('loading', function(isLoad){
				$rootScope.loading = isLoad;
				let acc = isLoad ? "disabled" : "enabled";
				getById('google_map').setAttribute("disabled", acc);
				getById('below_search').setAttribute("disabled", acc);
				getById('utils').setAttribute("disabled", acc);
			});
		}
	}
});