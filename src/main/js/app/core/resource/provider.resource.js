(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('Provider', Resource)
        .factory('ProviderT411', ResourceT411)
        .factory('ProviderCPB', ResourceCPB);

    /** @ngInject */
    function Resource(Resource, constant) {
        return Resource(constant.baseURI + '/settings/provider');
    }

    /** @ngInject */
    function ResourceT411(Resource, constant) {
        return Resource(constant.baseURI + '/settings/provider/t411');
    }

    /** @ngInject */
    function ResourceCPB(Resource, constant) {
        return Resource(constant.baseURI + '/settings/provider/cpasbien');
    }
})();
