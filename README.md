# JackBeard

## Running

* gulp serve : Lancer le serveur avec un back local et le navigateur
* gulp serve:integ : Lancer le serveur avec le back d'integration et le navigateur
* gulp test:auto : Lancer les TU
* gulp build : Lancer la contruction du projet vers le répertoire 'dist'

## Organisation des répertoires

* dist : Répertoire cible pour la construction de l'application
* gulp : Répertoire de paramétrage de Gulp
* src : 
  * app : Source de l'application
  * assets : Ressource - images, fonts, ...
  * index.html : Fichier d'entrée

## Configuration du proxy pour accèder à l'API (et autre)
-Dhttp.useProxy=true -Dhttp.proxyHost=HOST_NAME -Dhttp.proxyPort=PORT_NUMBER -Dhttps.proxyHost=HOST_NAME -Dhttps.proxyPort=PORT_NUMBER

Pour regler les proxies d'accès à l'API (ou autre) : `gulp/conf.js`

