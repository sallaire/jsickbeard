(function () {
    'use strict';

    angular.module('app.search')
        .service('SearchService', Service);

    /** @ngInject */
    function Service($q, _, Metadata, TvShow, TvShowConfig) {
        var service = this;

        service.search = function (lang, titleTvShow) {

            var episodes = [
                Metadata.getAll({name: titleTvShow, lang: lang}).$promise,
                TvShow.getAll().$promise
            ];

            return $q.all(episodes)
                .then(function (episodes) {
                        var metadatas = episodes[0];
                        var tvShows = episodes[1];
                        _.each(metadatas, function (metadata) {
                                var matchId = _.matcher({id: metadata.id});
                                var tvShow = _.filter(tvShows, matchId);
                                metadata.following = tvShow.length > 0 ? true : false;
                            }
                        );
                        return metadatas;
                    }
                );
        };

        service.add = function (tvShowId, quality, lang) {
            return TvShowConfig.create({id: tvShowId, quality: quality, audio: lang}).$promise;
        };

        service.delete = function (tvShowId) {
            return TvShowConfig.delete({id: tvShowId}).$promise;
        };
    }
})();
