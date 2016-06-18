(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('TvShowConfig', Resource);

    /** @ngInject */
    function Resource(Resource, constant) {
        return Resource(constant.baseURI + '/tvshow/config/:id', {id: '@id'});
    }
})();
