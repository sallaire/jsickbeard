(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('TvShow', Resource);

    /** @ngInject */
    function Resource(Resource, constant) {
        return Resource(constant.baseURI + '/tvshows', {});
    }
})();
