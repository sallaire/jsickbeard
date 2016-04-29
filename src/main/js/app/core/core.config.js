(function() {
  'use strict';

  angular
    .module('app.core')
    .config(config);

  /** @ngInject */
  function config($logProvider, cfpLoadingBarProvider) {
    // Enable log
    $logProvider.debugEnabled(true);
    
    // Suppression du spinner lors des chargement XHR
    cfpLoadingBarProvider.includeSpinner = true;
  }

})();
