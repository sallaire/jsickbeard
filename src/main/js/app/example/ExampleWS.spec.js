(function() {
	'use strict';
	
	describe('Exemple WS', function() {

		var ws, $q, deferred, $httpBackend, $rootScope ;

		beforeEach(module('app'));

		// Injection des utilitaires
		beforeEach(function() {
		  inject(function(_$q_, _$rootScope_,_$httpBackend_) {
		    $q = _$q_;
		    deferred = $q.defer();
		    $rootScope=_$rootScope_;
		    $httpBackend=_$httpBackend_;
		  });
		});

		beforeEach(inject(function(_ExampleWS_) {

			ws=_ExampleWS_;
		}));

		it('getExemple is defined...', function() {
			expect(ws.getList()).toBeDefined();
		});		
		
		it('getExemple is running...', function() {
			$httpBackend.expect('GET', '/api/publication/24440040400129_NM_VDN_00133/LOC_EQUIPUB_VIE_ASSOCIATIVE_NANTES_STBL/content').respond(201,'');
			ws.getList();
			$httpBackend.flush();
		});		
	});
})();
