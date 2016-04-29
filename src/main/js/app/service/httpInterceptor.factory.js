(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('httpInterceptor', HttpInterceptor);

    /** @ngInject */
    function HttpInterceptor($q, $state) {
        return function (promise) {

            var success = function (response) {
                return response;
            };

            var error = function (response) {
                if (response.status === 401) {
                    $state.go('home');
                }
                return $q.reject(response);
            };

            return promise.then(success, error);
        };
    }

})();