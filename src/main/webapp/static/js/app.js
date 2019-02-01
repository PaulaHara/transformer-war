'use strict';

var App = angular.module('myApp',[]);

App.controller('TransformerController', ['$scope', 'TransformerService', function($scope, TransformerService) {
    var self = this;
    self.transformer = {id:null, name:"", type:"", strength:null, intelligence:null, speed:null, endurance:null,
                        rank:null, courage:null, firepower:null, skill:null};
    self.transformers = [];
    self.ids = [];
    self.battleScore = "";
    self.typeA = false;
    self.typeD = false;

    self.submit = submit;
    self.edit = edit;
    self.remove = remove;
    self.battle = battle;
    self.updateTransformerType = updateTransformerType;

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
        self.updateTransformerType();

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

                if(self.transformer.type == "A") {
                    self.typeA = true;
                    self.typeD = false;
                } else {
                    self.typeD = true;
                    self.typeA = false;
                }

                break;
            }
        }
    }

    function battle(){
        console.log('Evaluating Transformer Battle');
        getBattleScore();
    }

    function reset(){
        self.transformer = {id:null, name:"", type:"", strength:null, intelligence:null, speed:null, endurance:null,
                                                   rank:null, courage:null, firepower:null, skill:null};
        self.typeA = false;
        self.typeD = false;
        $scope.myForm.$setPristine(); //reset Form
    }

    function updateTransformerType(){
        if(self.typeA) {
            self.transformer.type = "A";
        } else if(self.typeD) {
            self.transformer.type = "D";
        }
    }

}]);