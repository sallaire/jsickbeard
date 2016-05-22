(function () {
    'use strict';
    angular
        .module('app.core')
        .factory('Resource', Resource);

    /**
     * Resource, permet de différencier les créations des modifications.
     * @param $resource
     * @returns {Function}
     * @constructor
     */
    function Resource($resource) {
        return function (url, params, methods) {
            var defaults = {
                update: {
                    method: 'PUT',
                    isArray: false,
                    interceptor: {response: parseResponseDates}
                },
                create: {method: 'POST', interceptor: {response: parseResponseDates}},
                get: {method: 'GET', interceptor: {response: parseResponseDates}}
            };

            methods = angular.extend(defaults, methods);

            var resource = $resource(url, params, methods);

            resource.prototype.$save = function () {
                if (!this.id) {
                    return this.$create();
                }
                else {
                    return this.$update();
                }
            };

            return resource;
        };
    }

    function parseResponseDates(input) {
        if (!_.isEmpty(input) && !_.isEmpty(input._embedded)) {
            var regexIso8601 = /^(\d{4}|\+\d{6})(?:-(\d{2})(?:-(\d{2})(?:T(\d{2}):(\d{2}):(\d{2})\.(\d{1,})(Z|([\-+])(\d{2}):(\d{2}))?)?)?)?$/;
            // Ignore things that aren't objects.
            if (typeof input !== 'object') {
                return input;
            }
            for (var key in input) {
                if (!input.hasOwnProperty(key)) {
                    continue;
                }

                var value = input[key];
                var match;
                // Check for string properties which look like dates.
                if (typeof value === 'string' && (match = value.match(regexIso8601))) {
                    var milliseconds = Date.parse(match[0]);
                    if (!isNaN(milliseconds)) {
                        input[key] = new Date(milliseconds);
                    }
                } else if (typeof value === 'object') {
                    // Recurse into object
                    parseResponseDates(value);
                }
            }
        }
    }
})();