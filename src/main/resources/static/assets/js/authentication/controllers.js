'use strict';
angular.module('authentication')
	.controller('login', function ($scope, $rootScope, $location, $httpParamSerializerJQLike, authenticationService) {
		
		authenticationService.clearCredentials();
		$rootScope.authenticated = false;
		
		$scope.login = function () {
			var credentials = {
				username: this.username,
				password: this.password
			};
	
			var success = function (data) {
				$rootScope.authenticated = true;
				authenticationService.setCredentials(credentials.username, credentials.password);
				$location.path('/overview');
			};
	
			var error = function () {
				$scope.error = "Utilisateur ou mot de passe incorrect, veuillez ré-essayer"
			};
	 
			authenticationService.login($httpParamSerializerJQLike(credentials), success, error);
		};
	});