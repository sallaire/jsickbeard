(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('EpisodeWanted', ResourceWanted)
        .factory('EpisodeSnatched', ResourceSnatched)
        .factory('EpisodeDownloaded', ResourceDownloaded)
        .factory('EpisodeUpcoming', ResourceUpcoming);

    /** @ngInject */
    function ResourceWanted(Resource, constant) {
        return Resource(constant.baseURI + '/episodes/wanted', {});
    }

    /** @ngInject */
    function ResourceSnatched(Resource, constant) {
        return Resource(constant.baseURI + '/episodes/snatched', {});
    }

    /** @ngInject */
    function ResourceDownloaded(Resource, constant) {
        return Resource(constant.baseURI + '/episodes/downloaded', {});
    }

    /** @ngInject */
    function ResourceUpcoming(Resource, constant) {
        return Resource(constant.baseURI + '/episodes/upcoming', {});
    }
})();
