(function () {
    'use strict';

    angular
        .module('app.search')
        .controller('SearchAddController', Controller);

    function Controller($mdDialog) {
        var ctrl = this;
        ctrl.addParams = {
            quality : true,
            lang: true
        };

        ctrl.add = function () {
            $mdDialog.hide(ctrl.addParams);
        };
    }
})();
