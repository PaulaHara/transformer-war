'use strict';

angular.module('myApp').factory('TransformerService', ['$http', '$q', function($http, $q){

    var REST_SERVICE_URI = 'http://localhost:8080/transformer/';

    var factory = {
        fetchAllTransformers: fetchAllTransformers,
        createTransformer: createTransformer,
        updateTransformer: updateTransformer,
        deleteTransformer: deleteTransformer,
        deleteAllTransformers: deleteAllTransformers,
        battleScore: battleScore
    };

    return factory;

    function fetchAllTransformers() {
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI + 'all')
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while fetching Transformers');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }

    function createTransformer(transformer) {
        var deferred = $q.defer();
        $http.post(REST_SERVICE_URI + 'create', transformer)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while creating Transformer');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }

    function updateTransformer(transformer, id) {
        var deferred = $q.defer();
        $http.put(REST_SERVICE_URI + 'update/' + id, transformer)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while updating Transformer');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }

    function deleteTransformer(id) {
        var deferred = $q.defer();
        $http.delete(REST_SERVICE_URI + 'delete/' + id)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while deleting Transformer');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }

    function deleteAllTransformers(id) {
        var deferred = $q.defer();
        $http.delete(REST_SERVICE_URI + 'deleteAll')
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while deleting all Transformers');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }

    function battleScore(ids){
        var deferred = $q.defer();
        $http.get(REST_SERVICE_URI + 'battle/' + ids)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while evaluating battle');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }
}]);