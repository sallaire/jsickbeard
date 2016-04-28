(function () {
    'use strict';

    angular.module('app.resource', ['app.core'])
        .factory('User', User);

    function User($resource, constant, $cookies) {
        var credentials = $cookies.get('credentials');
        var authorization = credentials ? {authorization: 'Basic ' + credentials.authentication} : {};
        
        var User = $resource(constant.baseURI + '/user', {}, {
            get: {
                method: 'GET',
                headers: authorization
            }
        });
        return User;
    }
});

