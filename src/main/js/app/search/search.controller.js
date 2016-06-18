(function () {
    'use strict';

    angular
        .module('app.search')
        .controller('SearchController', Controller);

    /** @ngInject */
    function Controller(SearchService, $stateParams) {
        var ctrl = this;

        ctrl.metadatas = {};

        ctrl.activate = function () {
            SearchService.search($stateParams.lang, $stateParams.text).then(function(metadatas) {
                ctrl.metadatas = metadatas;
            });
        };

        ctrl.activate();

        ctrl.add = function (tvshowId, quality, lang) {
            SearchService.add(tvshowId, quality, lang);
        };

        ctrl.delete = function (tvshowId, quality, lang) {
            SearchService.delete(tvshowId, quality, lang);
        };
    }
})();
