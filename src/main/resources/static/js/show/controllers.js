'use strict';

// declare modules
angular.module('show', [])
	.controller('tvshows', function($scope, $http) {
		$http.get("/jackbeard/tvshow")
			.then(function (response) {
				$scope.myShows = response.data;
			});
	})
	
	.controller('tvshow', function($scope, $http, $routeParams, $location) {
		$http.get("/jackbeard/tvshow/"+$routeParams.showId)
			.then(function (response) {
				$scope.myShow = response.data;
			}, function (response) {
                if(response.status == 404) {
					$location.path("/tvshow/config/"+$routeParams.showId);
				}
            });
		$scope.updateStatus = function() {
			var ids = $scope.myShow.episodes.filter(function (el) {
				return el.selected;
			}).map(function(el) {
				return el.episodeId;
			});
			
			$http.put("/jackbeard/episode", {
				status : $scope.status, ids : ids
			});
		};
		
		$scope.updateShow = function() {
			$http.post("/jackbeard/metadata/tvshow/"+$routeParams.showId);
		};
	})
	
	.controller('tvShowConfig', function($scope, $http, $routeParams, $location) {
		$http.get("/jackbeard/tvshow/"+$routeParams.showId+"?fields=tvshow,config")
			.then(function (response) {
				$scope.myShow = response.data;
			});
		$scope.add = function () {
			$http.post("/jackbeard/tvshow/config/"+$routeParams.showId,
				{ name: $scope.myShow.name, status: $scope.status, quality: $scope.myShow.quality, audio: $scope.myShow.audioLang }
			).then(function (response) {
				$location.path('/tvshow/'+$routeParams.showId);
			});
		};
	})
	
	.controller('snatched', function($scope, $http) {
		$http.get("/jackbeard/episode?status=SNATCHED&from=0&length=10")
			.then(function (response) {
				$scope.episodes = response.data;
			});
		$scope.truncate = function(showId, season, number, quality, lang) {
			$http.delete("/jackbeard/episode/"+showId+"/"+season+"/"+number+"/snatched?quality="+quality+"&lang="+lang);
		}
		$scope.search = function(showId, episodeId) {
			$http.post("/jackbeard/tvshow/"+showId+"/episode/"+episodeId);
		}
	})
	
	.controller('wanted', function($scope, $http) {
		$http.get("/jackbeard/episode?status=WANTED&from=0&length=10")
			.then(function (response) {
				$scope.episodes = response.data;
			});
	})
	
	.controller('downloaded', function($scope, $http) {
		$http.get("/jackbeard/episode?status=DOWNLOADED&from=0&length=10")
			.then(function (response) {
				$scope.episodes = response.data;
			});
	})
	
	.controller('upcoming', function($scope, $http) {
		$http.get("/jackbeard/episode?status=UNAIRED&from=0&length=10")
			.then(function (response) {
				$scope.episodes = response.data;
			});
	})
	
	.controller('search', function($scope, $http, $location) {
		$scope.searchShow = function() {
			$http.get("/jackbeard/metadata/tvshow", {
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
