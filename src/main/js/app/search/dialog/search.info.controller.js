(function () {
    'use strict';

    angular
        .module('app.search')
        .controller('SearchInfoController', Controller);

    function Controller(SearchService) {
        var ctrl = this;
        ctrl.tvshow = {};

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
                ctrl.table.promise = SearchService.getTvshow(ctrl.tvshowId).$promise;

                ctrl.table.promise.then(success, error);

                function success(response) {
                    //ctrl.table.total = response.data.total;
                    ctrl.tvshow = response;
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

        ctrl.active = function () {
            ctrl.table.getEpisodes();
        };

        ctrl.active();
    }
})();
