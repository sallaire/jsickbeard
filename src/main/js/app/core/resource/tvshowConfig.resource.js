(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('TvshowConfig', Resource);

    /** @ngInject */
    function Resource(Resource, constant) {
        return Resource(constant.baseURI + '/tvshow/config/:id', {id: '@id'});
    }
})();
