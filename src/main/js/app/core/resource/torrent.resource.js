(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('Torrent', Resource);

    /** @ngInject */
    function Resource(Resource, constant) {
        return Resource(constant.baseURI + '/settings/client');
    }
})();
