(function() {
	'use strict';
	
	describe('Exemple controller', function() {

		var ctrl, $q, deferred, $rootScope ;

		beforeEach(module('app'));

		// Injection des utilitaires
		beforeEach(function() {
		  inject(function(_$q_, _$rootScope_) {
		    $q = _$q_;
		    deferred = $q.defer();
		    $rootScope=_$rootScope_;
		  });
		});

		beforeEach(inject(function(_$controller_,_ExampleListService_) {

			// Préparation des promises
			spyOn(_ExampleListService_, 'getList').and.returnValue(deferred.promise);
			spyOn(_ExampleListService_, 'getInfos').and.returnValue(deferred.promise);

			// Instanciation du controleur.
			ctrl = _$controller_('ExampleListController',{
				ExampleListService:_ExampleListService_
			});

		}));

		it('activate is defined...', function() {
			expect(ctrl.activate).toBeDefined();
		});		

		it('activate runs...', function() {
			deferred.resolve([{ id: 1 }, { id: 2 }]);
			$rootScope.$apply();
			expect(ctrl.list.length).toBe(2);
		});

		it('activate fail...', function() {
			deferred.reject('err');
			$rootScope.$apply();
			expect(ctrl.err).toBe('err');
		});

	});
})();