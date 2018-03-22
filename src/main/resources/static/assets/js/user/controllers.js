'use strict';
 
// declare modules
angular.module('user', ["chart.js"])
	.controller('account', function($scope, $http) {
		$http.get("account")
		.then(function (response) {
			$scope.account = response.data;
			
			$scope.options = {
				legend: {
					display: true,
					position: "bottom"
				}
			};
			$scope.genre = {};
			$scope.genre.labels = [];
			$scope.genre.data = [];
			
			Object.keys($scope.account.showsByGenre).forEach(function (key) {
				$scope.genre.labels.push(key);
				$scope.genre.data.push($scope.account.showsByGenre[key]);
			});
			
			$scope.network = {};
			$scope.network.labels = [];
			$scope.network.data = [];
			
			Object.keys($scope.account.showsByNetwork).forEach(function (key) {
				$scope.network.labels.push(key);
				$scope.network.data.push($scope.account.showsByNetwork[key]);
			});
			
		});
		
		$scope.changePassword =  function () {
			$http.put("user",
					{ password: $scope.newPassword });
		}
	}).directive('passwordMatch', function() {
		   return {
			      restrict: 'A',
			      require: 'ngModel',
			      scope: {
			         otherModelValue: '=passwordMatch'
			      },
			      link: function(scope, element, attributes, ngModel) {
			         ngModel.$validators.passwordMatch = function(modelValue) {
			            return modelValue === scope.otherModelValue;
			         };

			         scope.$watch('otherModelValue', function() {
			            ngModel.$validate();
			         });
			      }
			  }
		});


