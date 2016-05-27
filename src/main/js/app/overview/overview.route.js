(function () {
    'use strict';

    angular.module('app.overview').config(overviewStateConfig);

    /* @ngInject */
    function overviewStateConfig($stateProvider) {
        $stateProvider.state('overview', stateConfig());
    }

    function stateConfig() {
        return {
            url: '/overview',
            parent: 'connected',
            views: {
                'content@': {
                    templateUrl: 'app/overview/overview.html',
                    controller: 'OverviewController',
                    controllerAs: 'ctrl'
                },
                'downloaded@overview': {
                    templateUrl: 'app/overview/downloaded/overview.downloaded.html',
                    controller: 'OverviewDownloadedController',
                    controllerAs: 'ctrl'
                },
                'upcoming@overview': {
                    templateUrl: 'app/overview/upcoming/overview.upcoming.html',
                    controller: 'OverviewUpcomingController',
                    controllerAs: 'ctrl'
                }
            }
        };
    }
})();
