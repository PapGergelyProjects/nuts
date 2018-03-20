locationSelector.directive('loading', function($rootScope){
	return{
		restric: 'E',
		replace: true,
		template: '<div id"progress_div"><progress id="progress_bar"></progress></div>',
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