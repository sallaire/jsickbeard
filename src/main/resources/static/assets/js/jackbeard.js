'use strict';

// declare modules
angular.module('authentication', []);
angular.module('show', []);
angular.module('log', []);
angular.module('user', []);

var app = angular.module('jackbeard', ['authentication','show', 'log', 'user', 'ngRoute','ngCookies', 'ngMessages']);
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
			 }).when('/logs', {
				templateUrl: 'templates/log.html',
				controller: 'log'
			 }) 
			.when('/tvshow/config/:showId', {
				templateUrl: 'templates/showConfig.html',
				controller: 'tvShowConfig'
			 }) 
			.when('/login', { 
				templateUrl: 'templates/login.html', 
				controller: 'login',
				hideMenu: true
			 })
			.when('/search', { 
				templateUrl: 'templates/search.html', 
				controller: 'search' 
			 })
			 .when('/account', { 
				templateUrl: 'templates/account.html', 
				controller: 'account' 
			 })
			.otherwise({
				redirectTo: '/login'
			  });
		}]);
 
	app.run(['$rootScope', '$location', '$cookieStore', '$http',
    function ($rootScope, $location, $cookieStore, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;
            $rootScope.authenticated = true;
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
