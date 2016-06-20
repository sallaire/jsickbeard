(function () {
    'use strict';

    angular
        .module('app.search')
        .controller('SearchController', Controller);

    /** @ngInject */
    function Controller(SearchService, _, $stateParams, logger, $mdDialog) {
        var ctrl = this;
        ctrl.loading = true;
        ctrl.metadatas = {};

        // expandCard.
        ctrl.selectedCardIndex = [0];
        ctrl.selectCardIndex = function (tvshow) {
            var index = tvshow.id;

            if (_.contains(ctrl.selectedCardIndex, index)) {
                ctrl.selectedCardIndex = _.without(ctrl.selectedCardIndex, index);
            } else {
                ctrl.selectedCardIndex.push(index);
                if (tvshow.episodes && tvshow.episodes.length < 1) {
                    ctrl.info(tvshow);
                }
            }
        };

        ctrl.isSelect = function (index) {
            return _.contains(ctrl.selectedCardIndex, index);
        };


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

        ctrl.info2 = function (ev, id) {
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

        ctrl.info = function (tvshow) {
            SearchService.getTvshow(tvshow.id).$promise
                .then(function (_tvshow) {
                    tvshow.episodes = _tvshow.episodes;
                    tvshow.lastSeason = _.max(_tvshow.episodes, function(episode){ return episode.season; }).season;
                });
        };

    }
})();
