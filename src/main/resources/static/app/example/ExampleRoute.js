(function() {
	'use strict';

	angular
	.module('app')
	.config(routerConfig);

	/** @ngInject */
	function routerConfig($stateProvider) {

			// State abstrait : Parent
    		$stateProvider
    		.state('example', {
    			abstract:true,
    			url: '/example',
    			templateUrl: 'app/example/example.html'
    		});

    		// State concret
    		$stateProvider
    		.state('example.list', {
    			url: '/list',
    			templateUrl: 'app/example/list/example-list.tpl.html',
    			controller: 'ExampleListController',
    			controllerAs: 'ctrl'
    		});
    	}

})();
