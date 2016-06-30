(function () {
    'use strict';

    angular.module('app.tvshow')
        .service('TvshowService', Service);

    /** @ngInject */
    function Service($q, _, Tvshow, TvshowSeason, TvshowEpisode, TvshowConfig) {
        var service = this;

        service.get = function (id) {
            return Tvshow.get({id: id}).$promise;
        };

        service.getSeason = function (id, seasonNumber) {
            return TvshowSeason.get({id: id, seasonNumber: seasonNumber});
        };

        service.getEpisode = function (id, seasonNumber, episodeNumber) {
            return TvshowEpisode.get({id: id, seasonNumber: seasonNumber, episodeNumber: episodeNumber});
        };

        service.delete = function (id) {
            return TvshowConfig.delete({id: id}).$promise;
        };
    }
})();
