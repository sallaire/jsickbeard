(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('Login', Resource);

    /** @ngInject */
    function Resource(Resource, constant) {
        return {
            login: function (auth) {
                return Resource(constant.baseURI + '/user', {}, {
                    'login': {method: 'GET', headers: {Authorization: 'Basic ' + auth}}
                }).login().$promise;
            }
        }
    }
})();
