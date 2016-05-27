(function () {
    'use strict';

    angular.module('app', [
        /* Shared modules */
        'app.core',
        /* Feature areas */
        'app.login',
        'app.layout',
        'app.overview',
        'app.search'
    ]);

})();