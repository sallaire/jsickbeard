(function() {
  'use strict';

  angular
    .module('app.core')
    .config(config)
      .config(theme);

  /** @ngInject */
  function config($logProvider, cfpLoadingBarProvider) {
    // Enable log
    $logProvider.debugEnabled(true);
    
    // Suppression du spinner lors des chargement XHR
    cfpLoadingBarProvider.includeSpinner = false;
  }

  /** @ngInject */
  function theme($mdThemingProvider) {
    $mdThemingProvider.alwaysWatchTheme(true);
    $mdThemingProvider.theme('default')
        .primaryPalette('blue');
    $mdThemingProvider.theme('overview')
        .primaryPalette('blue');
    $mdThemingProvider.theme('search')
        .primaryPalette('pink')
  }

})();
