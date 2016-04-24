# Netapsys Angular Boilerplate

## Running

* gulp serve : Lancer le serveur avec un back local et le navigateur
* gulp serve:integ : Lancer le serveur avec le back d'integration et le navigateur
* gulp test:auto : Lancer les TU
* gulp build : Lancer la contruction du projet vers le répertoire 'dist'

## Organisation des répertoires

* coverage : 
  * [Résultats des derniers TU](coverage/TESTS-PhantomJS_1.9.8_(Linux_0.0.0).xml)
  * [Résultats de la couverture de test](coverage/PhantomJS 1.9.8 (Linux 0.0.0)/index.html)

* dist : Répertoire cible pour la construction de l'application
* gulp : Répertoire de paramétrage de Gulp
* src : 
  * app : Source de l'application
  * assets : Ressource - images, fonts, ...
  * index.html : Fichier d'entrée

## Configuration du proxy pour accèder à l'API (et autre)

Pour regler les proxies d'accès à l'API (ou autre) : `gulp/conf.js`

