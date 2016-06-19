(function () {
    'use strict';

    angular
        .module('app.search')
        .controller('SearchController', Controller);

    /** @ngInject */
    function Controller(SearchService, $stateParams, logger, $mdDialog) {
        var ctrl = this;
        ctrl.loading = true;
        ctrl.metadatas = {};

        ctrl.activate = function () {
            ctrl.loading = true;
            SearchService.search($stateParams.lang, $stateParams.text).then(function (metadatas) {
                ctrl.metadatas = metadatas;
                ctrl.loading = false;
            });
        };

        ctrl.activate();

        ctrl.delete = function (metadata) {
            SearchService.delete(metadata.id)
                .then(function () {
                    logger.info(metadata.name + ' supprimé');
                    ctrl.activate();
                });
        };

        ctrl.add = function (ev, metadata) {
            $mdDialog.show({
                    controller: 'SearchAddController',
                    controllerAs: 'ctrl',
                    templateUrl: 'app/search/dialog/search.add.html',
                    targetEvent: ev,
                    clickOutsideToClose: true,
                    bindToController: true,
                    locals: {metadata: metadata}
                })
                .then(function (params) {
                    SearchService.add(metadata.id, params.quality ? 'P720' : 'SD', params.lang ? 'fr' : 'en')
                        .then(function () {
                            logger.info(metadata.name + ' ajouté');
                            ctrl.activate();
                        });

                });

        };

        ctrl.info = function (ev, id) {
            $mdDialog.show({
                    controller: 'SearchInfoController',
                    controllerAs: 'ctrl',
                    templateUrl: 'app/search/dialog/search.info.html',
                    targetEvent: ev,
                    fullscreen: true,
                    clickOutsideToClose: true,
                    bindToController: true,
                    locals: {tvshowId: id}
                })
                .then(function (params) {

                });

        };
    }
})();
