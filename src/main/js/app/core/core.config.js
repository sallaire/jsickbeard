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
    cfpLoadingBarProvider.includeSpinner = true;
  }

  /** @ngInject */
  function theme($mdThemingProvider) {
    $mdThemingProvider.theme('home')
        .primaryPalette('blue');
    $mdThemingProvider.theme('search')
        .primaryPalette('red')
  }

})();
