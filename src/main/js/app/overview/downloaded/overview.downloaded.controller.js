(function () {
    'use strict';

    angular
        .module('app.overview')
        .controller('OverviewDownloadedController', Controller);

    /** @ngInject */
    function Controller(OverviewService, logger) {

        var ctrl = this;

        // Table des épisodes téléchargé.
        ctrl.downloadedTable = {
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
                ctrl.downloadedTable.promise = OverviewService.getEpisodesDownloaded(ctrl.downloadedTable.query).$promise;

                ctrl.downloadedTable.promise.then(success, error);

                function success(response) {
                    //ctrl.downloadedTable.total = response.data.total;
                    ctrl.episodes.downloaded = response;
                    logger.log(ctrl.episodes.downloaded);
                    ctrl.downloadedTable.selected = [];
                }

                function error(err) {
                    logger.log(err);
                }
            },
            onPaginate: function (page, limit) {
                ctrl.downloadedTable.query.limit = limit;
                ctrl.downloadedTable.query.page = page;
                ctrl.downloadedTable.getEpisodes();
            },
            onReorder: function (sort) {
                ctrl.downloadedTable.query.sort = sort;
                ctrl.downloadedTable.getEpisodes();
            },
            remove: function () {
                //OverviewService.removeEpisodeDownload(vm.entityTable.selected);
                ctrl.downloadedTable.selected = [];
                ctrl.downloadedTable.getEpisodes();
            }
        };

        ctrl.activate = function () {
            ctrl.downloadedTable.getEpisodes();
        };

        ctrl.activate();
    }
})();
