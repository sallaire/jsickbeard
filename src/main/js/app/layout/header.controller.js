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
        ctrl.theme = 'home';

        ctrl.activate = function () {

        };

        ctrl.activate();

        ctrl.toggleMenu = function () {
            return $mdSidenav('left').toggle();
        };

        ctrl.return = function() {
            $state.go('home');
            ctrl.theme = 'home';
            ctrl.searchTvShow = '';
        };

        ctrl.emitSearch = function () {
            if (!_.isEmpty(ctrl.searchTvShow)) {
                ctrl.theme = 'search';
                $state.go('search');
                $rootScope.$broadcast('search', ctrl.searchTvShow);
            } else {
                $state.go('home');
                ctrl.theme = 'home';
            }
        };
    }
})();
