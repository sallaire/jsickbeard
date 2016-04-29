(function () {
    'use strict';

    angular.module('app.login')
        .service('LoginService', Service);

    /** @ngInject */
    function Service($q, authorization, Login) {
        var service = this;

        service.login = function (user) {
            var defer = $q.defer();

            var auth = authorization.setCredentials(user.username, user.password);

            Login.login(auth).then(function () {
                // Appel WS
                defer.resolve(user);
            }, function (err) {
                defer.reject(err);
            });

            return defer.promise;
        };
    }
})();
