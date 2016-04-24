(function () {
    'use strict';

    angular.module('app.home').config(homeStateConfig);

    /* @ngInject */
    function homeStateConfig($stateProvider) {
        $stateProvider.state('home', stateConfig());
    }

    function stateConfig() {
        return {
            url: '/home',
            parent: 'connected',
            views:{
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'HomeController',
                    controllerAs: 'ctrl'
                }
            }
        };
    }
})();
