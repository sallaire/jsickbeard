(function () {
    'use strict';

    angular
        .module('app.search')
        .controller('SearchController', Controller);

    /** @ngInject */
    function Controller(SearchService, $scope) {
        var ctrl = this;

        ctrl.metadatas = {};

        ctrl.activate = function () {
        };

        ctrl.activate();

        ctrl.addTvShow = function () {
            ctrl.tvshow = {
                name: ''
            };
        };

        $scope.$on('search', function(event, titleTvShow) {
            SearchService.search(titleTvShow).then(function(metadatas) {
                ctrl.metadatas = metadatas;
            }) ;
        });
    }
})();
