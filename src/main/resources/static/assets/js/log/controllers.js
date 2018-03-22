'use strict';
 
// declare modules
angular.module('log')
	
	.controller('log', function($scope, $http) {
		$scope.from = 0;
		$scope.lines = 100;
		$scope.logs = [];
		var source;
		$http.get("log")
		.then(function (response) {
			$scope.files = response.data;
			$scope.selectedFile = $scope.files[0]
		});
		
		$scope.clean = function () {
			$scope.logs = [];
			$scope.from = 0
		}
		
		$scope.change = function () {
			$scope.stop();
			$scope.clean();
		}
		
		$scope.view = function () {
			$scope.clean();
			$http.get("log", {
				params: { file: $scope.selectedFile, lines: $scope.lines, from: $scope.from, grep: $scope.grep }
			})
			.then(function (response) {
				 var responseLogs = []; 
				 angular.forEach(response.data.logLines, function(value) {
					 responseLogs.push({
				            'level' : value.level.toLowerCase(),
				            'message' : value.message
				        });
				  });
				 $scope.logs = responseLogs.concat($scope.logs);
				 $scope.from += $scope.lines;
			});
		}
		$scope.start = function startTail() {
			$scope.tailActive = true;
	        source = new EventSource('log/tail?file='+$scope.selectedFile);
	        source.onmessage = function(msg) {
	        	var log = JSON.parse(msg.data);
	        	$scope.$apply(function () {
		        	$scope.logs.unshift({
				            'level' : log.level.toLowerCase(),
				            'message' : log.message
				        });
	        	});
	        }
	        //TODO handle error and close connection properly
	    }
		
		$scope.stop = function stopTail() {
			if (source) {
				$scope.tailActive = false;
				source.close();
			}
		}
	})
	
	.directive("scroll", ['$http', function ($http) {
	    return function(scope, element, attrs) {
	    	var raw = element[0];
	        element.bind("scroll", function() {
	             if (raw.scrollTop + raw.offsetHeight > raw.scrollHeight && scope.from > 0) {
	            	 $http.get("log", {
	     				params: { file: scope.selectedFile, lines: scope.lines, from: scope.from, grep: scope.grep }
	     			})
	     			.then(function (response) {
	     				 var responseLogs = []; 
	     				 angular.forEach(response.data.logLines, function(value) {
	     					 responseLogs.push({
	     				            'level' : value.level.toLowerCase(),
	     				            'message' : value.message
	     				        });
	     				  });
	     				 scope.logs = scope.logs.concat(responseLogs);
	     				 scope.from = response.data.index;
	     			});
	             }
	            scope.$apply();
	        });
	    };
	}])
	.directive('ngScrollBottom', ['$timeout', function ($timeout) {
	  return {
	    scope: {
	      ngScrollBottom: "="
	    },
	    link: function ($scope, $element) {
	      $scope.$watchCollection('ngScrollBottom', function (newValue) {
	        if (newValue) {
	          $timeout(function(){
	            $element[0].scrollTop = $element[0].scrollHeight;
	          }, 0);
	        }
	      });
	    }
	  }
	}]);


