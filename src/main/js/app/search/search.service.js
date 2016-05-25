(function () {
    'use strict';

    angular.module('app.search')
        .service('SearchService', Service);

    /** @ngInject */
    function Service(Metadata) {
        var service = this;

        service.search = function (lang, titleTvShow) {
            return Metadata.query({name: titleTvShow, lang: lang}).$promise;
        };
    }
})();
