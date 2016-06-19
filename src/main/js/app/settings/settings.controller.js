(function () {
    'use strict';

    angular
        .module('app.settings')
        .controller('SettingsController', Controller);

    /** @ngInject */
    function Controller(SettingsService, logger) {
        var ctrl = this;
        ctrl.providers = {};

        ctrl.activate = function () {
            SettingsService.getProviders().then(function(providers) {
                ctrl.providers = providers;
                ctrl.providers.t411.active = true;
                ctrl.providers.cpasbien.active = true;
            });

            ctrl.torrent = SettingsService.getTorrent();
        };

        ctrl.activate();

        ctrl.activeProviders = function() {
          SettingsService.activeProviders(ctrl.providers)
              .then(function() {
                  logger.info('Activation d\'un provider');
              });
        };

        ctrl.saveSettings = function() {
            ctrl.saveProviders();
            ctrl.saveTorrent();
        };

        ctrl.saveProviders = function() {
            SettingsService.saveProviders(ctrl.providers)
                .then(function() {
                    logger.info('Sauvegarde des paramètres');
                });
        };

        ctrl.saveTorrent = function() {
            SettingsService.saveTorrent(ctrl.torrent)
                .then(function() {
                    logger.info('Sauvegarde de la configuration du client torrent');
                });
        };
    }
})();
