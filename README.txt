********Informations g�n�rales********

Castle Protection - PIXEL EDITION

Castle Protection - PIXEL EDITION est un jeu, bas� sous la librairie StdDraw, qui reprend les principes du Tower Defense.
Impl�ment� en java en programmation orient� objet, ce jeu reprend de pr�s ou de loin (retouches) certaines images dont voici les sources :

voir LICENCE


********D�marrage********

Logiciels n�cessaires : Eclipse IDE
�tapes : 
- Installez le fichier zip.
- Ouvrez l'IDE Eclipse, cliquez sur File puis Import, "Existing project [...] puis enfin archive file. Recherchez le fichier zip
pr�alablement install� auparavant sur votre machine, cliquez dessous puis cliquez sur finir. Eclipse s'occupera du reste.
- Normalement vous voyez le projet dans le package explorer. allez dans le package warcaftTD puis lancez le Main (clic droit, run as, run).
- Le jeu devrait normalement se lancer.

-Vous pouvez aussi tout simplement lancer le fichier ex�cutable jar qui vous permettra de jouer � une version all�g�e du jeu.


********Auteurs********

Leo FILOCHE
Bahdon BARKHAD


********License********
La plupart des images proviennent de pr�s ou de loin (retouches) du site pixilart : nous avons �dit� la plupart d'entre elles gr�ce � ce site.
Aucune utilisation commerciale du jeu (comprenant les images reprises) n'est envisag�e : ce jeu n'a pas pour but de g�n�rer des revenus,
mais s'inscrit dans un cadre scolaire.

Images : 

- BaseMonster -> Image issue/qui s'inspire du jeu Pac-Man
- FlyingMonster -> Image issue/qui s'inspire du c�l�bre jeu Flappy bird.
- Dino -> Image issue de pixilart, animal mascotte de Google issu du jeu Chrome dino / T. Rex Game
- Pterodactyle -> Image issue de ce site -> https://www.vhv.rs/viewpic/hbTToRJ_pixel-art-chick-png-download-transparent-planet-pixel/
				image libre de droit "for personal use only" (pour utilisation non commerciale)
				
- Boule de feu -> Image issue du jeu Fortnite et plus pr�cis�ment du wiki qui lui est associ� (https://fortnite.fandom.com/fr/wiki/Boule_de_Feu)
- chateau, tours,nuage : constuit sur Pixilart par L�o
 

- Th�mes ->
			- Plage : . �toile de mer issue du Mus�um - Aquarium de Nancy (http://especeaquatique.museumaquariumdenancy.eu/fiche_espece/188)
					  . crabe issu du site pinterest, image elle-m�me issue de pixilart (https://www.pinterest.fr/pin/302656037448923926/)
					  . Fond path et fond world desinn�s � la main (pixilart)
			- Neige : image construite � l'aide d'outils propos�s par PowerPoint (flocons / fond)
			- Classique : image construite sur Pixilart � l'aide d'�l�ments propos�s par la communaut�		  

les images cit�es ne sont pas les seules pr�sentes dans le projet, nous avons aussi �dit� ces images afin d'avoir un gameplay 
int�ressant (ex : diff�rentes couleurs que prend le monstre suivant son �tat de sant�)
De plus, les logos pr�sents dans le jeu ont �t� construits � l'aide du site https://eu2.flamingtext.fr/

********Informations importantes********

L'exp�rience de jeu est modifi�e selon la taille de la fen�tre : plus la fen�tre sera grande plus il y aura de subtilit�s dans le gameplay
les �l�ments du menu dont la taille change lors du survol de la souris sont interactifs

Certains param�tres de jeu sont modifiables :
- modification du dombre de pi�ces du joueur
- modification de la vie du joueur
- modification de la vitesse des monstres
- modification de la vie des monstres (s'applique lors de l'apparition d'un nouveau monstre)

Vous pouvez cliquer sur un Boss pour lui ass�ner des d�gats lorsqu'il est devant le chateau
Appuyez sur 'echap' pour d�selectionner une tour


*******Informations subsidiaires*****
Les propri�t�s des tours sont toutes proportionnelles � celles de ArrowTower.
Les propori�t�s des monstres sont toutes proportionnelles � celles de BaseMonster.
Ces relations ont �t� contruites dans le but de toujours conserver un certain �quilibre de gameplay 

Ce jeu a �t� con�u progressivement, �tape par �tape.
Les mani�res de r�fl�chir le jeu ont �volu� durant tout le d�velopppement. 
Des m�thodes et des classes peuvent ainsi �tre inutilis�es actuellement, remplac�es par de nouvelles. 


********Qui a fait quoi ?********

 L�o : 
 	- impl�mentation du squelette du World (sous-classe CelluleActive, sous-classe BoutDeChemin,...)
 	- impl�mentation de la g�n�ration automatique d'un chemin
 	- gestion de la partie (d�but, fin, vague de monstres, pause, etc) et de l'interface
 	- gestion des cheatcodes
 	- gestion de l'�chelle/du d�calage des objets affich�s dans l'interface (hitbox, tours, monstres etc)
 	- impl�mentation classe Positions Autoris�es
 	- impl�mentation de la classe Chrono
 	- impl�mentation classe CouplePositions
 	- am�lioration classe Monster (notion d'�tats)
 
 Bahdon :
	- impl�mentation hitbox monstres 
	- impl�mentation de la classe Tower, gestion de leur am�lioration (dans le World, dans la classe Tower et dans les sous-classes de Projectile)
	- impl�mentation des Boss (gestion de leur attaque sur le ch�teau dans le World) : Pterodactyle et Dino
	- impl�mentation LaserTower
	- impl�mentation classe Projectile
	- impl�mentation classe Monster
	- am�lioration classe CouplePositions (notion de colin�arit�, de vecteur)
	- impl�mentation de la sous-classe JaugeDeVie
	
	
Ainsi que des corrections mutuelles qu'on a pu se faire entre nous !

******* Probl�mes non r�solus****************
      - Lorsque l'on modifie la taille du plateau (!= 1) on peut rencontrer des petits "bugs", 
      		notamment au niveau du placement des tours sur le plateau, et, plus grave : certains monstres peuvent sortir du chemin et rester bloqu�s
      - le chargement des images via StdDraw.picture peut causer des probl�mes de ralentissement de l'update (cela peut prendre un temps siginicatif)
        et donc engendrer des sou�is au niveau de la vitesse des monstres et m�me de certains projectiles pouvant m�me bloquer le jeu compl�tement !
        cela s'est rencontr� lorsque nous avons converti le jeu en .jar, d'o� la cr�ation d'un mode 'lite' qui remplace les images les + demand�es par des figures g�om�triques 
 

