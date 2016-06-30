(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('Tvshow', TvshowResource)
        .factory('TvshowSeason', SeasonResource)
        .factory('TvshowEpisode', EpisodeResource);

    /** @ngInject */
    function TvshowResource(Resource, constant) {
        return Resource(constant.baseURI + '/tvshow/:id', {id: '@id'});
    }

    /** @ngInject */
    function SeasonResource(Resource, constant) {
        return Resource(constant.baseURI + '/tvshow/:id/season/seasonNumber', {
            id: '@id',
            seasonNumber: '@seasonNumber'
        });
    }

    /** @ngInject */
    function EpisodeResource(Resource, constant) {
        return Resource(constant.baseURI + '/tvshow/:id/season/:seasonNumber/episode/:episodeNumber', {
            id: '@id',
            seasonNumber: '@seasonNumber',
            episodeNumber: '@episodeNumber'
        });
    }

})();
