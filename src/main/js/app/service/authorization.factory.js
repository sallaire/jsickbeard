(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('authorization', Authorization);

    /** @ngInject */
    function Authorization($cookies) {
        var service = {};

        service.setCredentials = function (username, password) {
            var currentUser = {
                username: username,
                authentication: btoa(username + ':' + password)
            };

            $cookies.putObject('currentUser', currentUser);
            return currentUser.authentication;
        };

        service.clearCredentials = function () {
            $cookies.remove('currentUser');
        };

        service.getAuth = function() {
            return $cookies.getObject('currentUser');
        };

        return service;
    }
})();