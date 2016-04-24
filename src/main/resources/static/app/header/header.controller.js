(function() {
  'use strict';

  angular
    .module('app.header',  ['app.core'])
    .controller('HeaderController', Controller);

  /** @ngInject */
  function Controller($mdSidenav) {
    var ctrl = this;
  
    
    ctrl.activate=function(){
    	
    };

    ctrl.activate();
    
    ctrl.toggleMenu = function() {
        return $mdSidenav('left').toggle();
    };
  }
})();
