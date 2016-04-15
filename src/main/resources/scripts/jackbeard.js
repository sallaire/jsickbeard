'use strict';
 
// declare modules
angular.module('authentication', []);

	var app = angular.module('jackbeard', ['authentication','ngRoute','ngCookies']);
	
	app.config(['$routeProvider',
		  function($routeProvider) {
			$routeProvider.
			  when('/tvshows', {
				templateUrl: 'templates/tvshows.html',
				controller: 'tvShowsCtrl'
			})
			.when('/tvshow/:showId', {
				templateUrl: 'templates/tvshow.html',
				controller: 'tvShowCtrl'
			 })
			.when('/overview', {
				templateUrl: 'templates/overview.html'
			 }) 
			.when('/addShow/:showId', {
				templateUrl: 'templates/addShow.html',
				controller: 'addShow'
			 }) 
			.when('/login', { 
				templateUrl: 'templates/login.html', 
				controller: 'login' 
			 })
			.otherwise({
				redirectTo: '/overview'
			  });
		}]);
 
	app.run(['$rootScope', '$location', '$cookieStore', '$http',
    function ($rootScope, $location, $cookieStore, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
        }
  
        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in
            if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
                $location.path('/login');
            }
        });
    }]);
  
	app.controller('tvShowsCtrl', function($scope, $http) {
		$http.get("http://37.187.19.83:9000/tvshows")
			.then(function (response) {
				$scope.myShows = response.data;
			});
	});
	
	app.controller('tvShowCtrl', function($scope, $http, $routeParams) {
		$http.get("http://37.187.19.83:9000/tvshow/"+$routeParams.showId)
			.then(function (response) {
				$scope.myShow = response.data;
			});
	});
	
	app.controller('addShow', function($scope, $http, $routeParams) {
		$http.get("http://37.187.19.83:9000/metadata/tvshow/"+$routeParams.showId)
			.then(function (response) {
				$scope.myShow = response.data;
			});
		$scope.add = function () {
			$http.post("http://37.187.19.83:9000/tvshow/"+$routeParams.showId,
				{ name: $scope.myShow.name, status: $scope.status, quality: $scope.quality, audio: $scope.audio }
			);
		};
	});
	
	app.controller('snatched', function($scope, $http) {
		$http.get("http://37.187.19.83:9000/episodes/snatched")
			.then(function (response) {
				$scope.episodes = response.data;
			});
		$scope.truncate = function(showId, season, number, quality, lang) {
			$http.delete("http://37.187.19.83:9000/episode/"+showId+"/"+season+"/"+number+"/snatched?quality="+quality+"&lang="+lang);
		}
	});
	
	app.controller('wanted', function($scope, $http) {
		$http.get("http://37.187.19.83:9000/episodes/wanted")
			.then(function (response) {
				$scope.episodes = response.data;
			});
	});
	
	app.controller('downloaded', function($scope, $http) {
		$http.get("http://37.187.19.83:9000/episodes/downloaded?from=0&length=10")
			.then(function (response) {
				$scope.episodes = response.data;
			});
	});
	
	app.controller('upcoming', function($scope, $http) {
		$http.get("http://37.187.19.83:9000/episodes/upcoming?from=0&length=10")
			.then(function (response) {
				$scope.episodes = response.data;
			});
	});
	
	app.filter('appdate', function($filter, client) {
		return function(input) {
			if (input == null) {
				return "";
			}
			var serrdate = client.getDateTime(input);
			var _date = $filter('date')(serrdate, 'MMM dd yyyy hh:mm:ss');
			return _date.toUpperCase();
		};
	});
