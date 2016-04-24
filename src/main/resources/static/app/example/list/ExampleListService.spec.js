(function() {
	'use strict';
	
	describe('ExampleListService', function() {

		var service, $q, deferred, $rootScope ;

		beforeEach(module('app'));

		// Injection des utilitaires
		beforeEach(function() {
		  inject(function(_$q_, _$rootScope_) {
		    $q = _$q_;
		    deferred = $q.defer();
		    $rootScope=_$rootScope_;
		  });
		});

		// Préparation des promises
		beforeEach(inject(function(_ExampleListService_,_ExampleWS_) {

			spyOn(_ExampleWS_, 'getList').and.returnValue(deferred.promise);
			service=_ExampleListService_;

		}));

		it('getExemple is defined...', function() {
			expect(service.getList).toBeDefined();
		});		

		it('getExemple is running...', function() {
			var data = {data : {data :[{ id: 1 }, { id: 2 }]}};
			deferred.resolve(data);
			service.getList().then(function (list) {
           		expect(list.length).toBe(2);
                          		expect(list[0].mark).toBeDefined();
                          		expect(list[0].mark).toBe(3);
               			});
			$rootScope.$apply();
		});

		it('getExemple is failing...', function() {
			var err = {msg:'Err'};
			deferred.reject(err);
			service.getList().then(function () {
			    // rien n'est renvoyer
			}, function (error) {
			       expect(error).toBeDefined();
			       expect(error.msg).toBeDefined();
			       expect(error.msg).toBe('Err');
			});
			
			$rootScope.$apply();
		});		

	});
})();