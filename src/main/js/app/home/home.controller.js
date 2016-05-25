(function () {
    'use strict';

    angular
        .module('app.home')
        .controller('HomeController', Controller);

    /** @ngInject */
    function Controller(HomeService, logger) {

        var ctrl = this;

        ctrl.episodes = {
            wanteds: {},
            snatcheds: {},
            downloaded: {},
            upcomings: {}
        };

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
                ctrl.downloadedTable.promise = HomeService.getEpisodesDownloaded(ctrl.downloadedTable.query).$promise;

                ctrl.downloadedTable.promise.then(success, error);

                function success(response) {
                    //ctrl.downloadedTable.total = response.data.total;
                    ctrl.episodes.downloaded = response;
                    logger.log(ctrl.episodes.downloaded);
                    ctrl.downloadedTable.selected = [];
                }

                function error(err) {
                    logger.error(err);
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
                //HomeService.removeEpisodeDownload(vm.entityTable.selected);
                ctrl.downloadedTable.selected = [];
                ctrl.downloadedTable.getEpisodes();
            }
        };

        ctrl.activate = function () {
            ctrl.downloadedTable.getEpisodes();
        };

        ctrl.activate();

        /**
         * Récupérer les informations sur les épisodes.
         */
        function getEpisodes() {
            HomeService.getEpisodes()
                .then(function (episodes) {
                    ctrl.episodes = episodes;
                })
                .catch(function (err) {
                        logger.log(err, 'Error lors de la récupération des épisodes.');
                    }
                );
        }


    }
})();
