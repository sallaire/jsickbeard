(function () {
    'use strict';

    /**
     * Mise en factory d'underscore pour pouvoir l'injecter.
     **/
    angular
        .module('app.core')
        .factory('_', prepare);

    /** @ngInject */
    function prepare($window) {
        return $window._;
    }

})();

