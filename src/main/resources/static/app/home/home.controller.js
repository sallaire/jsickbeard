(function() {
	'use strict';

	angular.module('app.home', ['app.core']).controller('HomeController', Controller);

	/** @ngInject */
	function Controller(_) {
		var ctrl = this;

		ctrl.action = {
			isOpen : false
		};

		ctrl.selectedCardIndex = [];
		ctrl.selectCardIndex = function(index) {

			if (_.contains(ctrl.selectedCardIndex, index)) {
				ctrl.selectedCardIndex = _.without(ctrl.selectedCardIndex,
						index);
			} else {
				ctrl.selectedCardIndex.push(index);
			}
		};

		ctrl.isSelect = function(index) {
			return _.contains(ctrl.selectedCardIndex, index);
		};

		// Table
		ctrl.personneTable = {
			selected : [],
			query : {
				sort : 'nom', // default order
				limit : 5,
				page : 1
			},
			options : {
				autoSelect : true,
				rowSelection : true
			},
			onPaginate : onPaginate,
			onReorder : onReorder
		};

		/**
		 * Paginate user's table
		 * 
		 * @param {number}
		 *            page
		 * @param {number}
		 *            limit
		 */
		function onPaginate(page, limit) {
			ctrl.personneTable.query.page = page;
			ctrl.personneTable.query.limit = limit;
		}

		/**
		 * Order user's table
		 * 
		 * @param {string}
		 *            sort
		 */
		function onReorder(sort) {
			ctrl.personneTable.query.sort = sort;
		}

		ctrl.annulerPersonne = annulerPersonne;
		ctrl.effacerRecherche = effacerRecherche;

		ctrl.activate = function() {
			annulerPersonne();
		};

		ctrl.activate();

		ctrl.ajoutPersonne = function() {
			ctrl.nouvellePersonne = {
				nom : '',
				prenom : '',
				enCreation : true
			};
		};

		function annulerPersonne() {
			ctrl.nouvellePersonne = {
				nom : '',
				prenom : '',
				enCreation : false
			};
		}
		;

		function effacerRecherche() {
			ctrl.search = {
				nom : '',
				prenom : '',
				dateNaissance : ''
			}
		}

		ctrl.personnes = [ {
			nom : 'CRESPEL',
			prenom : 'Guillaume',
			mail : 'guillaume.crespel@gmail.com',
			phone : "07 70 40 27 21",
			prestations : [ {
				date : '01/01/2010',
				type : 'Type 1 ',
				reponse : 'Réponse A',
				hebergement : 'hebergement 1'
			}, {
				date : '01/01/2010',
				type : 'Type 1 ',
				reponse : 'Réponse A',
				hebergement : 'hebergement 1'
			}, {
				date : '01/01/2010',
				type : 'Type 1 ',
				reponse : 'Réponse A',
				hebergement : 'hebergement 1'
			} ]
		}, {
			nom : 'CARBONELL',
			prenom : 'Jerome',
			mail : 'jerome.carbonnel@netapsys.fr',
			phone : "07 70 40 27 21",
			prestations : [ {
				date : '01/01/2010',
				type : 'Type 1 ',
				reponse : 'Réponse A',
				hebergement : 'hebergement 1'
			}, {
				date : '01/01/2010',
				type : 'Type 1 ',
				reponse : 'Réponse A',
				hebergement : 'hebergement 1'
			}, {
				date : '01/01/2010',
				type : 'Type 1 ',
				reponse : 'Réponse A',
				hebergement : 'hebergement 1'
			} ]
		}, {
			nom : 'CRESPEL',
			prenom : 'Guillaume',
			mail : 'guillaume.crespel@gmail.com',
			phone : "07 70 40 27 21",
			prestations : [ {
				date : '01/01/2010',
				type : 'Type 1 ',
				reponse : 'Réponse A',
				hebergement : 'hebergement 1'
			}, {
				date : '01/01/2010',
				type : 'Type 1 ',
				reponse : 'Réponse A',
				hebergement : 'hebergement 1'
			}, {
				date : '01/01/2010',
				type : 'Type 1 ',
				reponse : 'Réponse A',
				hebergement : 'hebergement 1'
			} ]
		}, {
			nom : 'CRESPEL',
			prenom : 'Guillaume',
			mail : 'guillaume.crespel@gmail.com',
			phone : "07 70 40 27 21"
		}, {
			nom : 'CRESPEL',
			prenom : 'Guillaume',
			mail : 'guillaume.crespel@gmail.com',
			phone : "07 70 40 27 21"
		}, {
			nom : 'CRESPEL',
			prenom : 'Guillaume',
			mail : 'guillaume.crespel@gmail.com',
			phone : "07 70 40 27 21"
		}, {
			nom : 'CRESPEL',
			prenom : 'Guillaume',
			mail : 'guillaume.crespel@gmail.com',
			phone : "07 70 40 27 21"
		}, {
			nom : 'CRESPEL',
			prenom : 'Guillaume',
			mail : 'guillaume.crespel@gmail.com',
			phone : "07 70 40 27 21"
		}, {
			nom : 'CRESPEL',
			prenom : 'Guillaume',
			mail : 'guillaume.crespel@gmail.com',
			phone : "07 70 40 27 21"
		}, {
			nom : 'CRESPEL',
			prenom : 'Guillaume',
			mail : 'guillaume.crespel@gmail.com',
			phone : "07 70 40 27 21"
		}, {
			nom : 'CRESPEL',
			prenom : 'Guillaume',
			mail : 'guillaume.crespel@gmail.com',
			phone : "07 70 40 27 21"
		} ];

	}
})();
