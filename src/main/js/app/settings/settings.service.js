(function () {
    'use strict';

    angular.module('app.settings')
        .service('SettingsService', Service);

    /** @ngInject */
    function Service($q, Torrent, Provider, ProviderT411, ProviderCPB) {
        var service = this;

        service.getProviders = function () {

            var providers = [
                ProviderT411.get(),
                ProviderCPB.get()
            ];

            return $q.all(providers)
                .then(function (providers) {
                        return {t411: providers[0], cpasbien: providers[1]};
                    }
                );
        };

        service.activeProviders = function (providers) {
            return Provider.create({t411: providers.t411.active, cpasbien: providers.cpasbien.active}).$promise;
        };

        service.saveProviders = function (providers) {
            return ProviderT411.update(providers.t411).$promise;
        };

        service.getTorrent = function () {
            return Torrent.get();
        };

        service.saveTorrent = function (torrent) {
            return Torrent.update(torrent).$promise;
        };
    }
})();
