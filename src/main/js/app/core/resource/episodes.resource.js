(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('EpisodeWanted', ResourceWanted)
        .factory('EpisodeSnatched', ResourceSnatched)
        .factory('EpisodeDownloaded', ResourceDownloaded)
        .factory('EpisodeSkipped', ResourceSkipped)
        .factory('EpisodeUnaired', ResourceUnaired);

    /** @ngInject */
    function ResourceWanted(Resource, constant) {
        return Resource(constant.baseURI + '/episode', {status: 'WANTED'});
    }

    /** @ngInject */
    function ResourceSnatched(Resource, constant) {
        return Resource(constant.baseURI + '/episode', {status: 'SNATCHED'});
    }

    /** @ngInject */
    function ResourceDownloaded(Resource, constant) {
        return Resource(constant.baseURI + '/episode', {status: 'DOWNLOADED'});
    }

    /** @ngInject */
    function ResourceSkipped(Resource, constant) {
        return Resource(constant.baseURI + '/episode', {status: 'SKIPPED'});
    }

    /** @ngInject */
    function ResourceUnaired(Resource, constant) {
        return Resource(constant.baseURI + '/episode', {status: 'UNAIRED'});
    }
})();
