'use strict';
 
// declare modules
angular.module('show')
	.controller('tvshows', function($scope, $http) {
		$scope.qualities = [
		      {key:'SD', value:'SD'},
		      {key:'P720', value:'720p'},
		      {key:'P1080', value:'1080p'}
		    ];
		$scope.audios = [
		      {key:'en', value:'Anglais'},
		      {key:'fr', value:'Français'}
		    ];
		$http.get("tvshow")
		.then(function (response) {
			$scope.results = response.data;
		});
		
		$scope.modify = function (tvShow) {
			tvShow.loading = true;
			$http.post("tvshow/"+tvShow.id+"/config",
				{ quality: tvShow.quality, audio: tvShow.audioLang }
			).then(function () {
				tvShow.followed=true;
				tvShow.editShow=false;
				tvShow.loading=false;
			}, function errorCallback(response) {
			   tvShow.loading=false;
			   tvShow.error=true;
			});
		};
		
		$scope.delete = function (id) {
			$http.delete("tvshow/"+id+"/config").then(function () {
				$http.get("tvshow")
				.then(function (response) {
					$scope.results = response.data;
				})
			}, function errorCallback(response) {
			   // todo
			});
		};
	})
	
	.controller('tvshow', function($scope, $http, $routeParams, $location, $route) {
		$scope.showId = $routeParams.showId;
		$http.get("tvshow/"+$routeParams.showId)
			.then(function (response) {
				$scope.show = response.data;
				$scope.seasons = [];
				for (var i = 1; i <= $scope.show.nbSeasons; i++) {
					$scope.seasons.push(i)
				}
				$scope.selectedSeason = $scope.show.nbSeasons;
				$scope.showSeason();
				if ($scope.show.customNames.length == 0) {
					$scope.show.customNames.push($scope.show.originalName);
				}
			});
		$scope.showSeason = function() {
			$http.get("tvshows/" + $scope.showId + "/season/" + $scope.selectedSeason)
				.then(function (response) {
					$scope.episodes = response.data;
				});
			}
		
		$scope.updateEpisode = function(e, status) {
			e.loading=true;
			$http.put("episode/"+e.episodeId, {}, {
				params: { status: status }
			}).then(function () {
				$scope.showSeason();
				e.loading = false;
			}, function errorCallback(response) {
				e.loading = false;
			});
		}
		
		$scope.edit = function() {
			$scope.editing=true;
			$scope.audioLang = $scope.show.audioLang;
			$scope.quality = $scope.show.quality;
		}
		
		$scope.save = function() {
			$scope.loading = true;
			$http.post("tvshow/"+$scope.show.id+"/config",
				{ quality: $scope.quality, audio: $scope.audioLang }
			).then(function () {
				$scope.show.audioLang = $scope.audioLang;
				$scope.show.quality = $scope.quality;
				$scope.loading = false;
				$scope.editing=false;
			}, function errorCallback(response) {
				$scope.loading = false;
			});
		}
		
		$scope.cancel = function() {
			$scope.editing=false;
		}
		
		$scope.unfollow = function() {
			$http.delete("tvshow/"+$routeParams.showId+"/config")
			.then(function (response) {
				$location.path('tvshows');
			});
		}
		
		$scope.refresh = function() {
			$scope.loading = true;
			$http.post("tvshow/"+$routeParams.showId)
			.then(function (response) {
				$route.reload();
			}, function errorCallback(response) {
				$scope.loading = false;
			});
		}
		
		$scope.addTag = function() {
			$scope.newTag = true;
		}
		
		$scope.cancelTag = function() {
			$scope.newTag = false;
			$scope.tag = "";
		}
		
		$scope.saveTag = function() {
			$scope.show.customNames.push($scope.tag);
			$http.put("tvshow/"+$routeParams.showId, $scope.show.customNames);
			$scope.newTag = false;
			$scope.tag = "";
		}
		
		$scope.removeTag = function(tag) {
			var index = $scope.show.customNames.indexOf(tag);
			if (index > -1) {
				$scope.show.customNames.splice(index, 1);
				$http.put("tvshow/"+$routeParams.showId, $scope.show.customNames);
			}
		}
	})
	.controller('snatched', function($scope, $http) {
		var load = function() {
			$http.get("episode?status=SNATCHED&from=0&length=10")
				.then(function (response) {
					$scope.episodes = response.data;
				});
		}
			
		
		$scope.updateEpisode = function(e, status) {
			e.loading=true;
			$http.put("episode/"+e.episodeId, {}, {
				params: { status: status }
			}).then(function (response) {
				load();
				if(response.status == 404) {
					$scope.$emit('wantedEvent');
				} else {
					$scope.$emit('downloadedEvent');
				}
				
			}, function errorCallback(response) {
			   e.loading=false;
			});
		}
		
		load();
	})
	
	.controller('wanted', function($scope, $http, $rootScope) {
		var load = function() {
			$http.get("episode?status=WANTED&from=0&length=10")
				.then(function (response) {
					$scope.episodes = response.data;
				});
		}
		$scope.updateEpisode = function(e, status) {
			e.loading=true;
			$http.put("episode/"+e.episodeId, {}, {
				params: { status: status }
			}).then(function () {
				load();
				$scope.$emit('downloadedEvent');
			}, function errorCallback(response) {
			   e.loading=false;
			});
		}
		
		$rootScope.$on('wantedEvent', function(event) {
		      load();
		});
		
		load();
	})
	
	.controller('downloaded', ['$scope', '$http', '$rootScope', function($scope, $http, $rootScope) {
		var load = function() {
			$http.get("episode?status=DOWNLOADED&from=0&length=10")
				.then(function (response) {
					$scope.episodes = response.data;
				});
		}
		$rootScope.$on('downloadedEvent', function(event) {
		      load();
		});
		
		load();
	}])
	
	.controller('upcoming', function($scope, $http) {
		$http.get("episode?status=UNAIRED&from=0&length=10")
			.then(function (response) {
				$scope.episodes = response.data;
			});
	})
	
	.controller('menu', function($scope, $http, $location) {
		$http.get("user")
		.then(function (response) {
			$scope.user = response.data;
		});
		$scope.$watch('searchShow', function (nVal, oVal)
			    {	
					if (nVal !== oVal) {
						if (!nVal || nVal.length == 0) {
							$location.path('/overview');
						} else {
							$location.path('/search').search('name', nVal).search('lang', 'fr');	
						}
				    }
			    });
		$scope.clear = function () {
			$scope.searchShow = '';
		}

		$scope.isActive = function (path) {
		  return ($location.path().substr(0, path.length) === path);
		}
	})
	.controller('search', function($scope, $http, $location) {
		$scope.name = $location.search().name;
		$scope.qualities = [
		      {key:'SD', value:'SD'},
		      {key:'P720', value:'720p'},
		      {key:'P1080', value:'1080p'}
		    ];
		$scope.audios = [
		      {key:'en', value:'Anglais'},
		      {key:'fr', value:'Français'}
		    ];
		$http.get("tvshows", {
			params: { name: $location.search().name, lang: $location.search().lang }
		})
		.then(function (response) {
			$scope.results = response.data;
		});
		
		$scope.add = function (tvShow) {
			tvShow.loading = true;
			$http.post("tvshow/"+tvShow.id+"/config",
				{ quality: tvShow.quality, audio: tvShow.audioLang }
			).then(function () {
				tvShow.followed=true;
				tvShow.addShow=false;
				tvShow.loading=false;
			}, function errorCallback(response) {
			   tvShow.loading=false;
			   tvShow.error=true;
			});
		};
	});


