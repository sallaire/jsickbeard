(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('Login', Login);

    /** @ngInject */
    function Login($resource, constant) {
        return {
            login: function (auth) {
                console.log(auth);
                return $resource(constant.baseURI + '/user', {}, {
                    'login': {method: 'GET', headers: {Authorization: 'Basic ' + auth}}
                }).login().$promise;
            }
        }
    }
})();
