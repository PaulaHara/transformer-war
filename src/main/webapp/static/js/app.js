'use strict';

var App = angular.module('myApp',[]);

App.controller('TransformerController', ['$scope', 'TransformerService', function($scope, TransformerService) {
    var self = this;
    self.transformer = {id:null, name:'', type:'', strength:0, intelligence:0, speed:0, endurance:0, rank:0, courage:0, firepower:0, skill:0};
    self.transformers = [];
    self.ids = [];
    self.battleScore = "";

    self.submit = submit;
    self.edit = edit;
    self.remove = remove;
    self.battle = battle;

    self.hasStarted = false;

    fetchAllTransformers();

    function fetchAllTransformers(){
        TransformerService.fetchAllTransformers()
        .then(
            function(d) {
                self.transformers = d;
            },
            function(errResponse){
                console.error('Error while fetching Transformers');
            }
        );
    }

    function createTransformer(transformer){
        TransformerService.createTransformer(transformer)
        .then(
            fetchAllTransformers,
            function(errResponse){
                console.error('Error while creating Transformer');
            }
        );
    }

    function updateTransformer(transformer, id){
        TransformerService.updateTransformer(transformer, id)
            .then(
            fetchAllTransformers,
            function(errResponse){
                console.error('Error while updating Transformer');
            }
        );
    }

    function deleteTransformer(id){
        TransformerService.deleteTransformer(id)
            .then(
            fetchAllTransformers,
            function(errResponse){
                console.error('Error while deleting Transformer');
            }
        );
    }

    function deleteAllTransformers(){
        TransformerService.deleteAllTransformers()
            .then(
            fetchAllTransformers,
            function(errResponse){
                console.error('Error while deleting all Transformers');
            }
        );
    }

    function getBattleScore(){
        TransformerService.battleScore(self.ids)
            .then(
            function(d) {
                self.battleScore = d;
            },
            function(errResponse){
                console.error('Error while calculating battle score');
            }
        );
    }

    function submit() {
        if(self.transformer.id === null){
            console.log('Saving New Transformer', self.transformer);
            createTransformer(self.transformer);
        }else{
            updateTransformer(self.transformer, self.transformer.id);
            console.log('Transformer updated with id ', self.transformer.id);
        }
        reset();
    }

    function remove(id){
        console.log('id to be deleted', id);
        if(self.transformer.id === id) {
            reset();
        }
        deleteTransformer(id);
    }

    function edit(id){
        console.log('id to be edited', id);
        for(var i = 0; i < self.transformers.length; i++){
            if(self.transformers[i].id === id) {
                self.transformer = angular.copy(self.transformers[i]);
                break;
            }
        }
    }

    function battle(){
        console.log('Evaluating Transformer Battle');
        getBattleScore();
    }

    function reset(){
        self.transformer = {id:null, name:'', type:'', strength:0, intelligence:0, speed:0, endurance:0, rank:0, courage:0, firepower:0, skill:0};
        $scope.myForm.$setPristine(); //reset Form
    }

}]);