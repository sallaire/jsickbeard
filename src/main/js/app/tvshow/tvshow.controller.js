(function () {
    'use strict';

    angular
        .module('app.tvshow')
        .controller('TvshowController', Controller);

    /** @ngInject */
    function Controller(TvshowService, _, $stateParams, logger, $mdDialog) {
        var ctrl = this;
        ctrl.loading = true;
        ctrl.tvshow = {};

        // expandCard.
        ctrl.selectedCardIndex = [0];
        ctrl.selectCardIndex = function (seasonNumber) {
            var index = seasonNumber;

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
            TvshowService.get($stateParams.id).then(function (tvshow) {
                ctrl.tvshow = tvshow;
                ctrl.loading = false;
            });
        };

        ctrl.activate();

    }
})();
