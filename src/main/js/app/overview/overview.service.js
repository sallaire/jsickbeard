(function () {
    'use strict';

    angular
        .module('app.overview')
        .service('OverviewService', Service);

    /** @ngInject */
    function Service($q, logger, EpisodeWanted, EpisodeSnatched, EpisodeDownloaded, EpisodeUpcoming) {
        var service = this;

        /**
         * Méthode pour récupérer les référentiels
         * @returns {object}
         */
        service.getEpisodes = function () {

            var episodes = [
                EpisodeWanted.query(),
                EpisodeSnatched.query(),
                EpisodeDownloaded.query({from: 0, length: 10}),
                EpisodeUpcoming.query({from: 0, length: 10})
            ];

            return $q.all(episodes)
                .then(function (episodes) {
                        return {
                            wanteds: episodes[0],
                            snatcheds: episodes[1],
                            downloaded: episodes[2],
                            upcomings: episodes[3]
                        };
                    }
                );
        };

        service.getEpisodesDownloaded = function (query) {
            return EpisodeDownloaded.query({from: 0, length: 50})
        };

        service.getEpisodesUpcoming = function (query) {
            return EpisodeUpcoming.query({from: 0, length: 50})
        };
    }
})();
