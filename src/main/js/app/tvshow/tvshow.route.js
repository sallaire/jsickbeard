(function () {
    'use strict';

    angular
        .module('app.tvshow')
        .config(tvshowStateConfig);

    /* @ngInject */
    function tvshowStateConfig($stateProvider) {
        $stateProvider.state('tvshow', stateConfig());
    }

    function stateConfig() {
        return {
            url: '/tvshow/:id',
            parent: 'connected',
            views: {
                'content@': {
                    templateUrl: 'app/tvshow/tvshow.html',
                    controller: 'TvshowController',
                    controllerAs: 'ctrl'
                }
            }
        };
    }
})();
