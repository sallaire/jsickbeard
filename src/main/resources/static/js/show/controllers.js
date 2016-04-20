'use strict';
 
// declare modules
angular.module('show', [])
	.controller('tvshows', function($scope, $http) {
		$http.get("http://37.187.19.83:9000/tvshows")
			.then(function (response) {
				$scope.myShows = response.data;
			});
	})
	
	.controller('tvshow', function($scope, $http, $routeParams, $location) {
		$http.get("http://37.187.19.83:9000/tvshow/"+$routeParams.showId)
			.then(function (response) {
				$scope.myShow = response.data;
			}, function (response) {
                if(response.status == 404) {
					$location.path("/addShow/"+$routeParams.showId);
				}
            });
		$scope.updateStatus = function() {
			var ids = $scope.myShow.episodes.filter(function (el) {
				return el.selected;
			}).map(function(el) {
				return el.episodeId;
			});
			
			$http.put("http://37.187.19.83:9000/tvshow/"+$routeParams.showId+"/episodes", {
				status : $scope.status, ids : ids
			});
		};
		
		$scope.updateShow = function() {
			$http.post("http://37.187.19.83:9000/metadata/tvshow/"+$routeParams.showId);
		};
	})
	
	.controller('addShow', function($scope, $http, $routeParams) {
		$http.get("http://37.187.19.83:9000/metadata/tvshow/"+$routeParams.showId)
			.then(function (response) {
				$scope.myShow = response.data;
			});
		$scope.add = function () {
			$http.post("http://37.187.19.83:9000/tvshow/"+$routeParams.showId,
				{ name: $scope.myShow.name, status: $scope.status, quality: $scope.quality, audio: $scope.audio }
			);
		};
	})
	
	.controller('snatched', function($scope, $http) {
		$http.get("http://37.187.19.83:9000/episodes/snatched")
			.then(function (response) {
				$scope.episodes = response.data;
			});
		$scope.truncate = function(showId, season, number, quality, lang) {
			$http.delete("http://37.187.19.83:9000/episode/"+showId+"/"+season+"/"+number+"/snatched?quality="+quality+"&lang="+lang);
		}
	})
	
	.controller('wanted', function($scope, $http) {
		$http.get("http://37.187.19.83:9000/episodes/wanted")
			.then(function (response) {
				$scope.episodes = response.data;
			});
	})
	
	.controller('downloaded', function($scope, $http) {
		$http.get("http://37.187.19.83:9000/episodes/downloaded?from=0&length=10")
			.then(function (response) {
				$scope.episodes = response.data;
			});
	})
	
	.controller('upcoming', function($scope, $http) {
		$http.get("http://37.187.19.83:9000/episodes/upcoming?from=0&length=10")
			.then(function (response) {
				$scope.episodes = response.data;
			});
	})
	
	.controller('search', function($scope, $http, $location) {
		$scope.searchShow = function() {
			$http.get("http://37.187.19.83:9000/metadata/tvshow", {
					params: { name: $scope.name, lang: "fr" }
				})
				.then(function (response) {
					$scope.results = response.data;
				});
		}
		$scope.selectShow = function($showId) {
			$location.path('/tvshow/'+$showId);
		}
	});
