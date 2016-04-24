(function() {
	'use strict';

	angular.module('app')
	.service('ExampleListService',Service);

	/** @ngInject */
	function Service(_,$q,ExampleWS, appConstant) {
		var service=this;

		service.getList = function(){
			var defer = $q.defer();

			ExampleWS.getList().then(function(data) {
				// Appel WS
				return data.data.data;
			},function(err){
				defer.reject(err);
			}).then(function(data){
				// metier
				_.each(data, function(activity){
					activity.mark= appConstant.THREE;
				});
				defer.resolve(data);
			});

			return defer.promise;
		};

		service.getInfos = function(){
        			var defer = $q.defer();
        			ExampleWS.getInfos().then(function(data) {
        				// Appel WS
        				defer.resolve(data.data);
        			},function(err){
        				defer.reject(err);
        			});
        			return defer.promise;
        };


        service.getInfosAndList = function(){
            var promises = {
                list : service.getList(),
                infos : service.getInfos()
            };

            return $q.all(promises);
        };
}
})();
