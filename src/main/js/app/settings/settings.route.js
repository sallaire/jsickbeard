(function () {
    'use strict';

    angular
        .module('app.settings')
        .config(settingsStateConfig);

    /* @ngInject */
    function settingsStateConfig($stateProvider) {
        $stateProvider.state('settings', stateConfig());
    }

    function stateConfig() {
        return {
            url: '/settings',
            parent: 'connected',
            views: {
                'content@': {
                    templateUrl: 'app/settings/settings.html',
                    controller: 'SettingsController',
                    controllerAs: 'ctrl'
                }
            }
        };
    }
})();
