(function () {
    'use strict';

    angular
        .module('app.layout')
        .controller('HeaderController', Controller);

    /** @ngInject */
    function Controller($rootScope, _, $stateParams, $state, $mdSidenav) {
        var ctrl = this;
        $rootScope.$state = $state;
        ctrl.searchTvshow = $stateParams.text;
        ctrl.theme = 'overview';

        ctrl.activate = function () {
        };

        ctrl.activate();

        ctrl.toggleMenu = function () {
            return $mdSidenav('left').toggle();
        };

        ctrl.return = function () {
            $state.go('overview');
            ctrl.theme = 'overview';
            ctrl.searchTvshow = '';
        };

        ctrl.emitSearch = function () {
            if (!_.isEmpty(ctrl.searchTvshow)) {
                ctrl.theme = 'search';
                $state.go('search', {lang: 'fr', text: ctrl.searchTvshow});
            } else {
                $state.go('overview');
                ctrl.theme = 'overview';
            }
        };
    }
})();
