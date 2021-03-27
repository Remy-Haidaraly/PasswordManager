Titre de l'application : Password Manager

Description : Application permettant de stocker ses identifiants et de générer des mots de passe.
Elle nécessite une connexion/inscription qui se fait avec un email/mot de passe ou avec son compte Google.

Auteurs : HAIDARALY Rémy, DARRAS Noélie

Logiciel et API utilisés : <br/>
Réalisée sous Android Studio, en Java, avec Firebase pour la base de données et Google Play services pour
l'authentification avec Google.

Fichiers externes : <br/>
Vous trouverez dans le document nommé "Fichiers externes" la maquette que nous avions réalisé au début du
projet, notre MCD ainsi que le logo pour l'îcone l'application que nous avons fait.

-----------------------------------------------------------------------------------------------------------

Fonctionnement de l'application :<br/>
Au démarrage, elle emmène sur la page de connexion. Celle-ci permet soit de se connecter, soit redirige
vers une activité permettant de s'inscrire, soit de récupérer son mot de passe via l'envoie d'un mail.
Une fois connecté, il est possible de voir la liste des sites précédemment enregistré, d'en rajouter de
nouveaux ou de se déconnecter.
Lorsque l'on ajoute un site, il est possible de faire générer un mot de passe selon plusieurs critères :
sa taille (entre 8 et 30) et ce qui le compose (uniquement des chiffres ou des lettres majuscules,
minuscules, des caractères spéciaux et des chiffres).
Appuyer sur un site de cette liste permet de le modifier ou de le supprimer.
Depuis la page d'accueil, il est possible de se déconnecter en appuyant sur le bouton prévu à cet effet, 
mais également en appuyant deux fois sur le bouton retour. Pour quitter l'application également il est
nécessaire d'appuyer deux fois sur le bouton retour du téléphone.


Choix du design :</br>
Le design de l'application est volontairement extrêmement basique afin de la rendre la plus simple
d'utilisation possible, mais également de conserver ses modes clair et sombre qui dépendent du mode du
téléphone lui-même.


Compte ayant permis de faire la plupart des tests :</br>
Identifiant : nono3@gmail.com</br>
Mot de passe : Nonoo%%2</br>
Les tests ont été réalisé en testant le projet via un simulateur et un appareil physique, en essayant de 
faire planter l'application.


Difficultés rencontrées :</br>
La plus grosse difficulté concernait Firebase et la manipulation de la base de données. Nous familiariser
avec, la comprendre et comprendre comment faire ce que nous voulions s'est révélé compliqué, surtout
concernant la récupération et l'affichage de données provenant de la base.


Amélioration possible :</br>
L'application est ce que nous avions imaginé au début, par rapport à ses fonctionnalités, mais nous
envisageons de continuer à y apporter des modifications, et principalement de créer des règles de sécurités
sur Firebase, ce qui a été un gros souci lors du développement de l'application et nous menait à des
plantages permanent. De plus, nous aimerions hacher les mots de passe stockés dans la base de données, 
relatifs au sites web entrés par l'utilisateur.

