(function () {
    'use strict';

    angular
        .module('app.search')
        .config(searchStateConfig);

    /* @ngInject */
    function searchStateConfig($stateProvider) {
        $stateProvider.state('search', stateConfig());
    }

    function stateConfig() {
        return {
            url: '/search',
            parent: 'connected',
            views: {
                'content@': {
                    templateUrl: 'app/search/search.html',
                    controller: 'SearchController',
                    controllerAs: 'ctrl'
                }
            }
        };
    }
})();
