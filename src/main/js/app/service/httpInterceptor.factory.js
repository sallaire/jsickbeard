(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('httpInterceptor', HttpInterceptor)
        .factory('authHttpRequestInterceptor', authHttpRequestInterceptor);

    /** @ngInject */
    function authHttpRequestInterceptor(authorization) {
        return {

            request: function (config) {
                // This is the authentication service that I use.
                // I store the bearer token in the local storage and retrieve it when needed.
                // You can use your own implementation for this
                var authData = authorization.getAuth();

                if (authData && authData.authentication) {
                    config.headers["Authorization"] = 'Basic ' + authData.authentication;
                }

                return config;
            }
        };
    }

    /** @ngInject */
    function HttpInterceptor($q, $state, logger) {
        return function (promise) {

            var success = function (response) {
                return response;
            };

            var error = function (response) {
                if (response.status === 401) {
                    $state.go('login');
                    logger.warning('Vous n\'êtes pas autorisé à accéder à cette resource');
                }
                return $q.reject(response);
            };

            return promise.then(success, error);
        };
    }

})();