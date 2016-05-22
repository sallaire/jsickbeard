(function () {
    'use strict';

    angular.module('app.search')
        .service('SearchService', Service);

    /** @ngInject */
    function Service(Metadata) {
        var service = this;

        service.search = function (titleTvShow) {
            return Metadata.query({name: titleTvShow, lang: 'fr'}).$promise;
        };
    }
})();
