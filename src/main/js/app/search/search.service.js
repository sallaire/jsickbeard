(function () {
    'use strict';

    angular.module('app.search')
        .service('SearchService', Service);

    /** @ngInject */
    function Service($q, _, Metadata, Tvshow, TvshowConfig) {
        var service = this;

        service.search = function (lang, titleTvshow) {

            var episodes = [
                Metadata.getAll({name: titleTvshow, lang: lang}).$promise,
                Tvshow.getAll().$promise
            ];

            return $q.all(episodes)
                .then(function (episodes) {
                        var metadatas = episodes[0];
                        var tvshows = episodes[1];
                        _.each(metadatas, function (metadata) {
                                var matchId = _.matcher({id: metadata.id});
                                var tvshow = _.filter(tvshows, matchId);
                                metadata.following = tvshow.length > 0 ? true : false;
                                if(metadata.following) {
                                    _.extend(metadata, tvshow[0]);
                                }
                            }
                        );
                        return metadatas;
                    }
                );
        };

        service.add = function (tvshowId, quality, lang) {
            return TvshowConfig.create({id: tvshowId, quality: quality, audio: lang}).$promise;
        };

        service.delete = function (tvshowId) {
            return TvshowConfig.delete({id: tvshowId}).$promise;
        };

        service.getTvshow = function(tvshowId) {
            return Tvshow.get({id : tvshowId});
        };
    }
})();
