(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('Metadata', Resource);

    /** @ngInject */
    function Resource(Resource, constant) {
        return Resource(constant.baseURI + '/metadata/tvshow', {});
    }
})();
