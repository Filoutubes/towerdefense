# Castle Protection - PIXEL EDITION


**Note: Ce projet n'est plus maintenu.** 

## ********Informations générales********

Castle Protection - PIXEL EDITION est un jeu, basé sous la librairie StdDraw, qui reprend les principes du Tower Defense.
Implémenté en java en programmation orienté objet, ce jeu reprend de près ou de loin (retouches) certaines images dont voici les sources :

voir LICENSE


## ********Démarrage********

Logiciels nécessaires : Eclipse IDE, ou tout autre IDE (Intellij IDEA, etc : tout ce que vous voulez).
Étapes : 
- Installez le fichier zip.
- Ouvrez votre IDE, cliquez sur File puis Import, "Existing project [...] puis enfin archive file. Recherchez le fichier zip
préalablement installé auparavant sur votre machine, cliquez dessous puis cliquez sur finir. Votre IDE s'occupera du reste.
- Normalement vous voyez le projet dans le package explorer. Allez dans le package warcaftTD puis lancez le Main (clic droit, run as Java App, run).
- Le jeu devrait normalement se lancer.
- Vous pouvez aussi tout simplement lancer le fichier exécutable jar qui vous permettra de jouer à une version allégée du jeu (voir repository towerdefenselight).


## ********Auteurs********

- Léo
- Bahdon


## ********License********
La plupart des images proviennent de près ou de loin (retouches) du site pixilart : nous avons édité la plupart d'entre elles grâce à ce site.
Aucune utilisation commerciale du jeu (comprenant les images reprises) n'est envisagée : ce jeu n'a pas pour but de générer des revenus, mais s'inscrit dans un cadre scolaire.

Images : 

- BaseMonster -> Image issue/qui s'inspire du jeu Pac-Man
- FlyingMonster -> Image issue/qui s'inspire du célèbre jeu Flappy bird.
- Dino -> Image issue de pixilart, animal mascotte de Google issu du jeu Chrome dino / T. Rex Game
- Pterodactyle -> Image issue de ce site -> https://www.vhv.rs/viewpic/hbTToRJ_pixel-art-chick-png-download-transparent-planet-pixel/
				image libre de droit "for personal use only" (pour utilisation non commerciale)
				
- Boule de feu -> Image issue du jeu Fortnite et plus précisément du wiki qui lui est associé (https://fortnite.fandom.com/fr/wiki/Boule_de_Feu)
- Château, Tours,Nuages : construits sur Pixilart par Léo
 

- Thèmes ->
			- Plage : . étoile de mer issue du Muséum - Aquarium de Nancy (http://especeaquatique.museumaquariumdenancy.eu/fiche_espece/188)
					  . Crabe issu du site pinterest, image elle-même issue de pixilart (https://www.pinterest.fr/pin/302656037448923926/)
					  . Fond path et fond world desinnés à la main (pixilart)
			- Neige : image construite à l'aide d'outils proposés par PowerPoint (flocons / fond)
			- Classique : image construite sur Pixilart à l'aide d'éléments proposés par la communauté		  

Les images citées ne sont pas les seules présentes dans le projet, nous avons aussi édité ces images afin d'avoir un gameplay 
intéressant (ex : différentes couleurs que prend le monstre suivant sa jauge de vie).
De plus, les logos présents dans le jeu ont été construits à l'aide du site https://eu2.flamingtext.fr/.

## ********Informations importantes********

L'expérience de jeu est modifiée selon la taille de la fenêtre : plus la fenêtre sera grande plus il y aura de subtilités dans le gameplay.
Les éléments du menu, dont la taille change lors du survol de la souris, sont interactifs.

Certains paramètres de jeu sont modifiables :
- modification du dombre de pièces du joueur
- modification de la vie du joueur
- modification de la vitesse des monstres
- modification de la vie des monstres (s'applique lors de l'apparition d'un nouveau monstre)

Vous pouvez cliquer sur un Boss pour lui asséner des dégats lorsqu'il est devant le château.
Appuyez sur la touche 'echap' pour désélectionner une tour.


*******Informations subsidiaires*****
Les propriétés des tours sont toutes proportionnelles à celles de ArrowTower.
Les propriétés des monstres sont toutes proportionnelles à celles de BaseMonster.
Ces relations ont été contruites dans le but de toujours conserver un certain équilibre de gameplay.

Ce jeu a été conçu progressivement, étape par étape.
Les manières de conceptualiser le jeu ont évolué durant tout le développpement. 
Des méthodes et des classes peuvent ainsi être inutilisées actuellement, remplacées par de nouvelles. 


## ********Informations supplémentaires********

 - Léo : 
 	- implémentation du squelette du World (sous-classe CelluleActive, sous-classe BoutDeChemin,...)
 	- implémentation de la génération automatique d'un chemin
 	- gestion de la partie (début, fin, vague de monstres, pause, etc) et de l'interface
 	- gestion des cheatcodes
 	- gestion de l'échelle/du décalage des objets affichés dans l'interface (hitbox, tours, monstres etc)
 	- implémentation classe Positions Autorisées
 	- implémentation de la classe Chrono
 	- implémentation classe CouplePositions
 	- amélioration classe Monster (notion d'états)
 
 - Bahdon :
	- implémentation hitbox monstres 
	- implémentation de la classe Tower, gestion de leur amélioration (dans le World, dans la classe Tower et dans les sous-classes de Projectile)
	- implémentation des Boss (gestion de leur attaque sur le château dans le World) : Pterodactyle et Dino
	- implémentation LaserTower
	- implémentation classe Projectile
	- implémentation classe Monster
	- amélioration classe CouplePositions (notion de colinéarité, de vecteur)
	- implémentation de la sous-classe JaugeDeVie
	
	
Ainsi que des corrections mutuelles qu'on a pu se faire entre nous!

## ********Problèmes non résolus********
- Lorsque l'on modifie la taille du plateau (!= 1) on peut rencontrer des petits "bugs", notamment au niveau du placement des tours sur le plateau, et, plus grave : certains monstres peuvent sortir du chemin et rester bloqués
- le chargement des images via StdDraw.picture peut causer des problèmes de ralentissement de l'update (cela peut prendre un temps siginicatif) et donc engendrer des souçis au niveau de la vitesse des monstres et même de certains projectiles pouvant même bloquer le jeu complètement ! Cela s'est rencontré lorsque nous avons converti le jeu en .jar, d'où la création d'un mode 'lite' qui remplace les images les + demandées par des figures géométriques
	- EDIT: Cause probable -> à chaque appel de la méthode update(), on recharge entièrement le fond de la map. Or, la construction du fond de la map est inspirée de celle du chemin: on recharge chaque petit carré de la map (suivant une taille fixée préalablement) par une image.

