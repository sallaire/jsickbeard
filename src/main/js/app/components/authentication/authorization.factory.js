(function () {
    'use strict';

    angular.module('app.authentication', ['app.core']).factory('authorization', Authorization);

    /** @ngInject */
    function Authorization($cookies, User) {
        var service = {};

        service.setCredentials = function (username, password) {
            var currentUser = {
                username: username,
                authentication: btoa(username + ':' + password)
            };

            $cookies.put('credentials', currentUser);
        };

        service.clearCredentials = function () {
            $cookies.remove('credentials');
        };

        service.login = function () {
            return User.get();
        };

        return service;
    }
})();