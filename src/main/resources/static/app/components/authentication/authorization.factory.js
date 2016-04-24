(function () {
    'use strict';

    angular.module('app.authentication', ['app.core']).factory('authorization', Authorization);

    /** @ngInject */
    function Authorization($http, $cookieStore, $rootScope, constant) {
        var service = {};

        service.setCredentials = function (username, password) {
            var authdata = btoa(username + ':' + password);

            $rootScope.globals = {
                currentUser: {
                    username: username,
                    authdata: authdata
                }
            };

            $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata; // jshint ignore:line
            $cookieStore.put('globals', $rootScope.globals);
        };

        service.clearCredentials = function () {
            $rootScope.globals = {};
            $cookieStore.remove('globals');
            $http.defaults.headers.common.Authorization = 'Basic ';
        };

        service.login = function (credentials, sucessCallBack, errorCallBack) {

            /* Dummy authentication for testing, uses $timeout to simulate api call
             ----------------------------------------------*/
            //$timeout(function(){
            //    var response = { success: credentials.username === 'test' && credentials.password === 'test' };
            //    if(!response.success) {
            //        response.message = 'Username or password is incorrect';
            //		errorCallBack(response);
            //    }
            //	else {
            //		sucessCallBack(response);
            //	}

            //}, 1000);

            /* Use this for real authentication
             ----------------------------------------------*/
            var headers = credentials ? {authorization: "Basic " + btoa(credentials.username + ":" + credentials.password)} : {};
            $http.get(constant.baseURI + '/user', {headers: headers})
                .then(function (response) {
                    console.log("dada");
                    sucessCallBack(response);
                }, function (response) {
                    errorCallBack(response);
                });
        };
        return service;
    }

})();