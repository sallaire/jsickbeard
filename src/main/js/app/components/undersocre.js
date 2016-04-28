(function() {
	'use strict';
	
	/**
	* Mise en factory d'underscore pour pouvoir l'injecter.
	**/
	angular.module('underscore', []).factory('_', prepare);
	
	/** @ngInject */
	function prepare($window){
		return $window._;
	}

})();

