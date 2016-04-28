(function() {
  'use strict';

  angular
    .module('app.core')
    .run(runBlock);

  /** @ngInject */
  function runBlock(logger) {
    logger.debug('Application démarrée.');
  }

})();
