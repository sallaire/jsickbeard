(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('logger', logger);

    /* @ngInject */
    function logger($log, $mdToast) {

        var service = {
            showToasts: true,

            error: error,
            info: info,
            success: success,
            warning: warning,
            debug: debug,

            // straight to console;
            log: $log.log
        };

        return service;
        /////////////////////

        function showSimpleToast(message) {
            $mdToast.show(
                $mdToast.simple()
                    .textContent(message)
                    .position('bottom right')
                    .hideDelay(3000)
            );
        }

        function error(message, data) {
            showSimpleToast(message);
            $log.error('Error: ' + message, data || '');
        }

        function info(message, data) {
            showSimpleToast(message);
            $log.info('Info: ' + message, data || '');
        }

        function success(message, data) {
            showSimpleToast(message);
            $log.info('Success: ' + message, data || '');
        }

        function warning(message, data) {
            showSimpleToast(message);
            $log.warn('Warning: ' + message, data || '');
        }

        function debug(message, data) {
            $log.debug('Debug: ' + message, data || '');
        }
    }
})();