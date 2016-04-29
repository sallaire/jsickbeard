(function () {
    'use strict';

    angular
        .module('app.search')
        .controller('SearchController', Controller);

    /** @ngInject */
    function Controller() {
        var ctrl = this;

        ctrl.search = {};

        ctrl.clearSearch = clearSearch;

        ctrl.activate = function () {
        };

        ctrl.activate();

        ctrl.addTvShow = function () {
            ctrl.tvshow = {
                name: ''
            };
        };

        function clearSearch() {
            ctrl.search = {};
        }
    }
})();
