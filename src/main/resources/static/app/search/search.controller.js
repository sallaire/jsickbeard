(function() {
	'use strict';

	angular.module('app.search', ['app.core']).controller('SearchController', Controller);

	/** @ngInject */
	function Controller(_) {
		var ctrl = this;

        ctrl.search = {};

		ctrl.clearSearch = clearSearch;

		ctrl.activate = function() {
		};

		ctrl.activate();

		ctrl.addTvShow = function() {
			ctrl.tvshow = {
				name : ''
			};
		};

		function clearSearch() {
			ctrl.search = {};
		}
	}
})();
