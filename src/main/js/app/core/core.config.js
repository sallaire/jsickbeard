(function () {
    'use strict';

    angular
        .module('app.core')
        .config(config)
        .config(httpProvider)
        .config(resourceProvider)
        .config(theme);

    /** @ngInject */
    function config($logProvider, cfpLoadingBarProvider) {
        // Enable log
        $logProvider.debugEnabled(true);

        // Suppression du spinner lors des chargement XHR
        cfpLoadingBarProvider.includeSpinner = false;
    }

    /** @ngInject */
    function httpProvider($httpProvider) {
        $httpProvider.interceptors.push('authHttpRequestInterceptor');
    }

    /** @ngInject */
    function resourceProvider($resourceProvider) {
        $resourceProvider.defaults.actions = {
            create: {method: 'POST'},
            get: {method: 'GET'},
            getAll: {method: 'GET', isArray: true},
            update: {method: 'PUT'},
            delete: {method: 'DELETE'}
        };
    }

    /** @ngInject */
    function theme($mdThemingProvider) {
        $mdThemingProvider.alwaysWatchTheme(true);
        $mdThemingProvider.theme('default')
            .primaryPalette('blue');
        $mdThemingProvider.theme('overview')
            .primaryPalette('blue');
        $mdThemingProvider.theme('search')
            .primaryPalette('pink')
    }

})();
