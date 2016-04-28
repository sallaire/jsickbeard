(function () {
    'use strict';

    angular.module('app.login', ['app.core', 'app.authentication']).controller('LoginController', LoginController);

    /* @ngInject */
    function LoginController($state, logger, authorization) {
        var ctrl = this;

        ctrl.user = {username: '', password: ''};

        ctrl.login = login;
        //////////////////////////

        function login() {

            authorization.setCredentials(ctrl.user.username, ctrl.user.password);

            authorization.login(ctrl.user)
                .then(success)
                .catch(failure);

            function success(data) {
                logger.success('success login', data);
                $state.go('home');
            }

            function failure(response) {
                authorization.clearCredentials(ctrl.user.username, ctrl.user.password);
                logger.error('L\'identifiant ou le mot de passe que vous avez entré est ' +
                    'incorrect.');
            }
        }
    }
})();
