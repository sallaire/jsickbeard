(function () {
    'use strict';

    angular
        .module('app.layout')
        .controller('HeaderController', Controller);

    /** @ngInject */
    function Controller($rootScope, _, $state, $mdSidenav) {
        var ctrl = this;
        $rootScope.$state = $state;
        ctrl.searchTvShow = '';

        ctrl.activate = function () {

        };

        ctrl.activate();

        ctrl.toggleMenu = function () {
            return $mdSidenav('left').toggle();
        };

        ctrl.return = function() {
            $state.go('home');
        };

        ctrl.emitSearch = function () {
            if (!_.isEmpty(ctrl.searchTvShow)) {
                $state.go('search');
                $rootScope.$broadcast('search', ctrl.searchTvShow);
            } else {
                $state.go('home');
            }
        };
    }
})();
