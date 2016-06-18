(function () {
    'use strict';

    angular
        .module('app.overview')
        .controller('OverviewDownloadedController', Controller);

    /** @ngInject */
    function Controller(OverviewService, logger) {

        var ctrl = this;

        // Table des épisodes téléchargé.
        ctrl.table = {
            selected: [],
            query: {
                sort: 'airDate', // default order
                limit: 15,
                page: 1
            },
            options: {
                autoSelect: false,
                boundaryLinks: false,
                pageSelector: true,
                rowSelection: true
            },
            getEpisodes: function getEntities() {
                ctrl.table.promise = OverviewService.getEpisodes(ctrl.table.query);

                ctrl.table.promise.then(success, error);

                function success(response) {
                    //ctrl.table.total = response.data.total;
                    ctrl.episodes = response;
                    logger.log(ctrl.episodes);
                    ctrl.table.selected = [];
                }

                function error(err) {
                    logger.log(err);
                }
            },
            onPaginate: function (page, limit) {
                ctrl.table.query.limit = limit;
                ctrl.table.query.page = page;
                ctrl.table.getEpisodes();
            },
            onReorder: function (sort) {
                ctrl.table.query.sort = sort;
                ctrl.table.getEpisodes();
            },
            remove: function () {
                //OverviewService.removeEpisodeDownload(vm.entityTable.selected);
                ctrl.table.selected = [];
                ctrl.table.getEpisodes();
            }
        };

        ctrl.activate = function () {
            ctrl.table.getEpisodes();
        };

        ctrl.activate();
    }
})();
