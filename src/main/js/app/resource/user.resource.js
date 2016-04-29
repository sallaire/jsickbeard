(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('User', User);

    /** @ngInject */
    function User($resource, constant) {
        return $resource(constant.baseURI + '/user', {});
    }
})();
