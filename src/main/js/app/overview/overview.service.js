(function () {
    'use strict';

    angular
        .module('app.overview')
        .service('OverviewService', Service);

    /** @ngInject */
    function Service($q, _, logger, EpisodeWanted, EpisodeSnatched, EpisodeDownloaded, EpisodeSkipped, EpisodeUnaired) {
        var service = this;

        /**
         * Méthode pour récupérer les référentiels
         * @returns {object}
         */
        service.getEpisodes = function () {

            var episodes = [
                EpisodeWanted.getAll({from: 0, length: 10}).$promise,
                EpisodeSnatched.getAll({from: 0, length: 10}).$promise,
                EpisodeDownloaded.getAll({from: 0, length: 10}).$promise,
                EpisodeSkipped.getAll({from: 0, length: 10}).$promise
            ];

            return $q.all(episodes)
                .then(function (episodes) {
                        return episodes[0].concat(episodes[1]).concat(episodes[2]).concat(episodes[3]);
                    }
                );
        };

        service.getEpisodesDownloaded = function (query) {
            return EpisodeDownloaded.getAll({from: 0, length: 50})
        };

        service.getEpisodesUnaired = function (query) {
            return EpisodeUnaired.getAll({from: 0, length: 50})
        };
    }
})();
