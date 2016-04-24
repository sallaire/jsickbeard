(function() {
	'use strict';

	angular.module('app')
	.service('ExampleWS',Service);

	/** @ngInject */
	function Service($http) {
		var service=this;

		service.getInfos=function(){
			return $http.get('peoples/1');
		};

		service.getList=function(){
    		return $http.get('peoples');
    	};

	}
})();