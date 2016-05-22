(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('User', Resource);

    /** @ngInject */
    function Resource(Resource, constant) {
        return Resource(constant.baseURI + '/user', {});
    }
})();
