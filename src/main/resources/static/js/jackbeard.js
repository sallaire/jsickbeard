'use strict';
 
// declare modules
angular.module('authentication', []);

	var app = angular.module('jackbeard', ['authentication','show','ngRoute','ngCookies']);
	
	app.config(['$routeProvider',
		  function($routeProvider) {
			$routeProvider.
			  when('/tvshows', {
				templateUrl: 'templates/tvshows.html',
				controller: 'tvshows'
			})
			.when('/tvshow/:showId', {
				templateUrl: 'templates/tvshow.html',
				controller: 'tvshow'
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
			.when('/search', { 
				templateUrl: 'templates/search.html', 
				controller: 'search' 
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
