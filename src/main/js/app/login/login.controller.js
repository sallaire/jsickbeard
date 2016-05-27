(function () {
    'use strict';

    angular
        .module('app.login')
        .controller('LoginController', LoginController);

    /* @ngInject */
    function LoginController($state, logger, LoginService) {
        var ctrl = this;

        ctrl.user = {username: '', password: ''};

        ctrl.login = login;
        //////////////////////////

        function login() {

            LoginService.login(ctrl.user)
                .then(success)
                .catch(failure);

            function success(data) {
                logger.success('success login', data);
                $state.go('overview');
            }

            function failure() {
                logger.error('L\'identifiant ou le mot de passe que vous avez entré est ' +
                    'incorrect.');
            }
        }
    }
})();
