(function () {
    'use strict';

    angular
        .module('app.core')
        .config(routerConfig);

    /** @ngInject */
    function routerConfig($stateProvider, $urlRouterProvider) {
        $stateProvider.state('main', mainStateConfig());
        $stateProvider.state('connected', connectedStateConfig());
        $stateProvider.state('disconnected', disconnectedStateConfig());
        $stateProvider.state('default', defaultStateConfig());

        $urlRouterProvider.otherwise('/');
    }

    /**
     * Main state configuration
     */
    function mainStateConfig() {
        return {
            abstract: true
        };
    }

    /**
     * Connected state configuration
     */
    function connectedStateConfig() {
        return {
            abstract: true,
            parent: 'main',
            views: {
                'header@': {
                    templateUrl: 'app/layout/header.html',
                    controller: 'HeaderController',
                    controllerAs: 'ctrl'
                },
                'nav@': {
                    templateUrl: 'app/layout/nav.html',
                    controller: 'HeaderController',
                    controllerAs: 'ctrl'
                }
            }
        };
    }

    /**
     * Not conneccted configuration
     */
    function disconnectedStateConfig() {
        return {
            abstract: true,
            parent: 'main',
            views: {
                'header@': {
                    templateUrl: 'app/layout/header.disconnect.html'
                }
            }
        };
    }

    /**
     * Default state configuration
     */
    function defaultStateConfig() {
        var stateConfig = {
            url: '',
            onEnter: redirect
        };

        /* @ngInject */
        function redirect($state) {
            $state.go('login');
        }

        return stateConfig;
    }

})();
