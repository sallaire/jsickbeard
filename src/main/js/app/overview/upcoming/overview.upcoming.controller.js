(function () {
    'use strict';

    angular
        .module('app.overview')
        .controller('OverviewUpcomingController', Controller);

    /** @ngInject */
    function Controller(OverviewService, logger) {

        var ctrl = this;
        
        // Table des épisodes téléchargé.
        ctrl.upcomingTable = {
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
                ctrl.upcomingTable.promise = OverviewService.getEpisodesUpcoming(ctrl.upcomingTable.query).$promise;

                ctrl.upcomingTable.promise.then(success, error);

                function success(response) {
                    //ctrl.upcomingTable.total = response.data.total;
                    ctrl.episodes.upcoming = response;
                    logger.log(ctrl.episodes.upcoming);
                    ctrl.upcomingTable.selected = [];
                }

                function error(err) {
                    logger.error(err);
                }
            },
            onPaginate: function (page, limit) {
                ctrl.upcomingTable.query.limit = limit;
                ctrl.upcomingTable.query.page = page;
                ctrl.upcomingTable.getEpisodes();
            },
            onReorder: function (sort) {
                ctrl.upcomingTable.query.sort = sort;
                ctrl.upcomingTable.getEpisodes();
            },
            remove: function () {
                //OverviewService.removeEpisodeDownload(vm.entityTable.selected);
                ctrl.upcomingTable.selected = [];
                ctrl.upcomingTable.getEpisodes();
            }
        };

        ctrl.activate = function () {
            ctrl.upcomingTable.getEpisodes();
            console.log('upcoming');
        };

        ctrl.activate();
    }
})();
