(function () {
    'use strict';

    angular
        .module('app.login')
        .config(loginStateConfig);

    /* @ngInject */
    function loginStateConfig($stateProvider) {
        $stateProvider.state('login', stateConfig());
    }

    function stateConfig() {
        return {
            url: '/login',
            parent: 'disconnected',
            views: {
                'content@': {
                    templateUrl: 'app/login/login.html',
                    controller: 'LoginController',
                    controllerAs: 'ctrl'
                }
            }
        };
    }

})();
