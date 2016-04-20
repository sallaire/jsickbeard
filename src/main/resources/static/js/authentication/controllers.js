'use strict';
angular.module('authentication')
	.controller('login', function ($scope, $location, authorization) {
		$scope.login = function () {
		var credentials = {
			username: this.username,
			password: this.password
		};

		var success = function (data) {
		  authorization.setCredentials(credentials.username, credentials.password);
		  $location.path('/');
		};

		var error = function () {
		// TODO: apply user notification here..
		};
 
		authorization.login(credentials, success, error);
  };
});