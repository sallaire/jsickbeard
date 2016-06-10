'use strict';
  
angular.module('authentication')
 .factory('httpInterceptor', function httpInterceptor ($q, $window, $location) {
	return function (promise) {
      var success = function (response) {
          return response;
      };

      var error = function (response) {
          if (response.status === 401) {
              $location.url('/login');
          }

          return $q.reject(response);
      };

      return promise.then(success, error);
	};
  })
  .factory('authorization',
    ['$http', '$cookieStore', '$rootScope', '$timeout',
    function ($http, $cookieStore, $rootScope, $timeout) {
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
			 var headers = credentials ? {authorization : "Basic "+ btoa(credentials.username + ":" + credentials.password)} : {};
            $http.get('/jackbeard/user', {headers : headers})
                .then(function (response) {
                    sucessCallBack(response);
                }, function (response) {
                    errorCallBack(response);
                });
         };
         return service;
    }])

