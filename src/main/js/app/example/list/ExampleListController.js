(function() {
  'use strict';

  angular
  .module('app')
  .controller('ExampleListController', Controller);

  /** @ngInject */
  function Controller(ExampleListService) {
    var ctrl = this;
    var svc = ExampleListService;
    ctrl.sort='LIBTYPE';
    ctrl.text='';
    ctrl.list=[];
    ctrl.activate = function () {
      svc.getInfosAndList().then(function(data) {
        ctrl.list=data.list;
        ctrl.infos=data.infos;
      },function(err){
        ctrl.err = err;
      });
    };

     ctrl.activate();
    }
})();
