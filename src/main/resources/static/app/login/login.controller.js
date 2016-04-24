(function () {
    'use strict';

    angular.module('app.login', ['app.core', 'app.authentication']).controller('LoginController', LoginController);

    /* @ngInject */
    function LoginController($state, logger, authorization) {
        var ctrl = this;

        ctrl.user = {
            username: '',
            password: ''
        };

        ctrl.login = login;
        //////////////////////////

        function login() {
            authorization.login(ctrl.user, success, faillure)
                .then(success)
                .catch(faillure);

            function success(data) {
                logger.success('success login', data);
                authorization.setCredentials(user.username, user.password);
                $state.go('home');
            }

            function faillure(response) {
                logger.error('L\'identifiant ou le mot de passe que vous avez entré est ' +
                    'incorrect.');
            }
        }
    }
})();
