package echec;


import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.Random;

import static echec.Couleur.*; //Permet de faire reconnaitre BLANC/NOIR comme type directement

public class Jeu {

    //Attributs
    public Scanner sc = new Scanner(System.in);
            //Attribuer un scanner à la classe permet de s'assurer qu'il n'y a pas de bug quand on l'utilise
            //dans différentes fonctions.
    private Joueur JB;
    private Joueur JN;
    private Plateau PlateauJ;


    //Constructeur
    public Jeu() {
        this.JB = new Joueur(BLANC);
        this.JN = new Joueur(NOIR);
    }

    //Méthodes
    public int menu() {
        int choix;
        System.out.println("Bienvenue dans le menu ! Quel action voulez vous réaliser ?");
        System.out.println("1 : Jouer une partie.");
        System.out.println("2 : Charger une configuration puis lancer une partie");
        System.out.println("3 : Activer/Désactiver IA (Désactivée par défaut)");
        System.out.println("4 : Quitter");

        choix = sc.nextInt();
        while (choix != 1 && choix != 2 && choix != 3 && choix != 4)
        {
            System.out.println("Veuillez entrez un choix valide ! (1/2/3/4)");
            choix = sc.nextInt();
        }

        return choix;
    }

    public void jouerUnePartie(boolean avecFichierConfig, boolean avecIA) {
        Couleur gagnant = null;
        Couleur joueurActif = BLANC;
        File f; String nom_f;

        //---- INITIALISATION DU PLATEAU -------
        PlateauJ = Plateau.getNewInstance();
        if (!avecFichierConfig)  //-- CAS DE BASE
            PlateauJ.initialiserPlateau();
        else { //-- CAS AVEC FICHIER DE CONFIGURATION
            System.out.println("Veuillez entrez le nom du fichier de configuration à charger :");
            sc.nextLine(); //consume les leftovers
            nom_f = sc.nextLine();
            f = new File(nom_f);
            if ( !f.exists() ) {
                System.out.println("Ce fichier n'existe pas ! La configuration de base va être chargée.");
                PlateauJ.initialiserPlateau();
            }
            else
                PlateauJ.initialiserPlateau(f);
        }

        //---- DEROULEMENT DE LA PARTIE -------
            //Tant qu'il n'y a pas de gagnant, on fait jouer chaque joueur chacun son tour.
            //On s'appuie sur joueurActif pour savoir qui joue.
        while (gagnant == null) {
            if (joueurActif == BLANC) {
                gagnant = jouerTour(JB);
            }
            else {
                if (!avecIA)
                    gagnant = jouerTour(JN);
                else
                    gagnant = jouerTourIA(NOIR);
            }
            joueurActif = joueurActif.couleurEnnemi();
        }
        System.out.println();
        System.out.println("------------ Le Joueur "+gagnant+" a gagné ! ------------");
        System.out.println();
    }

    public Couleur jouerTour(Joueur J) {
        Position posPiece = new Position();
        Position posCoup;
        Piece piece = null;
        Piece pieceMangee;
        boolean pieceValide = false;

        // 0 --- ON MET A JOUR LE PLATEAU DE JEU
        PlateauJ.update();

        //Si aucun des pieces du joueur ne peut être joué, on le déclare perdant ( Bien que c'est techniquement un Pat )
        if ( PlateauJ.pieceJouables(J.getCouleur()).isEmpty() )
            return J.getCouleur().couleurEnnemi();


        System.out.println("---- C'est au tour du Joueur "+J.getCouleur().name()+" ! ----");

        PlateauJ.afficherPlateau();

        // 1 --- SELECTIONNER UNE PIECE VALIDE.
        System.out.println("Entrez les coordonnées de la pièce que vous voulez jouer ! (Entrez -1 pour abandonner)");
        while ( !pieceValide ) {
            //On demande des coordonnées type LettreNombre, on renvoie une Position lisible par le programme.
            posPiece = coorToPosition();

            if (posPiece == null) //Cas abandon
                return J.getCouleur().couleurEnnemi();

            //On récupère l'objet Piece de la case sélectionné.
            piece = PlateauJ.contenuCase(posPiece);

            //Tant que la case sélectionné est vide ou que ce n'est pas une piece de la couleur du Joueur, on redemande
            // un nouveau choix.
            while ( piece == null || piece.getCouleur() != J.getCouleur()  ) {
                System.out.println("Vous devez sélectionner une de vos pieces !");

                posPiece = coorToPosition();
                if (posPiece == null) //Cas abandon
                    return J.getCouleur().couleurEnnemi();

                piece = PlateauJ.contenuCase(posPiece);

            }

            if (piece.listeCoupIsEmpty()) {
                System.out.println("Cette pièce n'a aucun coup possible ! Entrez les coordonnées d'une autre piece!");
            }
            else
                pieceValide = true;
        }

        // 2 --- JOUER UN COUP.
        // On affiche les coups possibles de la pièce
        piece.afficherCoupsPossibles();

        System.out.println("Entrez les coordonnées du coup que vous voulez jouer ! (Entrez -1 pour abandonner)");
        posCoup = coorToPosition();

        if (posCoup == null) //Cas abandon
            return J.getCouleur().couleurEnnemi();

        //On récapète tant que le coup entrée n'est pas dans la liste de coups possibles
        while ( !piece.contientCoord(posCoup) ) {
            System.out.println("Ce coup n'est pas dans la liste des coups possibles !");
            posCoup = coorToPosition();

            if (posCoup == null) //Cas abandon
                return J.getCouleur().couleurEnnemi();
        }

        //Ensuite on déplace la piece quand on a un choix valide.
        pieceMangee = PlateauJ.deplacerPiece(posPiece, posCoup);

        //CAS PROMOTION DE PION
        posPiece = PlateauJ.getPosImportantes("promotion");
        if (posPiece != null)
            PlateauJ.setCase(  PlateauJ.promotion(PlateauJ.contenuCase(posPiece).getCouleur(), sc), posPiece  );

        //On vérifie si un roi a été mangé.
        if ( pieceMangee != null && pieceMangee instanceof Roi )
            return J.getCouleur(); //On renvoie la couleur du gagnant
        return null; //Si il n'y a pas encore de gagnant, on renvoie null

    }

    public Couleur jouerTourIA(Couleur couleur) {

        Position posPiece = new Position();
        Position posCoup;
        Piece pieceJouee;
        Piece pieceMangee;

        Random randy = new Random();
        int i_alea;

        ArrayList<Piece> pieceJouables;
        ArrayList<Position> coupsJouables;

        // 0 --- ON MET A JOUR LE PLATEAU DE JEU
        /* update() mets à jour le plateau de jeu :
            - Chaque pion calcule tout ses coups possibles.
            - Un message est affiché si un roi est en echec
            - Si un pion reçoit une promotion, il renvoie sa position, sinon il renvoie null.
         */
        PlateauJ.update();

        // 1 --- SELECTIONNER UNE PIECE VALIDE.

        //On dresse une liste des pieces du camp de l'IA qui ont des coups possibles.
        pieceJouables = PlateauJ.pieceJouables(couleur);
        //On en prend une au hasard.
        i_alea = randy.nextInt(pieceJouables.size());
        pieceJouee = pieceJouables.get(i_alea);
        //On cherche les coordonnées de la piece en parcourant le tableau :
        for (int a = 0; a < 8; a++ )
            for (int n = 0; n < 8; n++)
                if ( PlateauJ.contenuCase(a,n) == pieceJouee)
                    posPiece = new Position(a, n);


        // 2 --- SELECTIONNER ET JOUER UN COUP.
        //On prend un coup au hasard;
        i_alea = randy.nextInt(pieceJouee.getListeDeCoups().size());
        coupsJouables = pieceJouee.getListeDeCoups();
        posCoup = coupsJouables.get(i_alea);
        //Ensuite on déplace la piece quand on a choisi un coup.
        pieceMangee = PlateauJ.deplacerPiece(posPiece, posCoup);


        posPiece = PlateauJ.getPosImportantes("promotion");
        if (posPiece != null)
            PlateauJ.setCase(new Reine(couleur), posPiece);
        //Si y'a une promotion l'IA prend une reine car elle est pas conne


        //On vérifie si un roi a été mangé.
        if ( pieceMangee != null && pieceMangee instanceof Roi )
            return couleur;


        return null;
    }

    public Position coorToPosition() {
        String coor;
        int nb1, nb2;

        coor = sc.nextLine();
        while (true) {
            if (coor.length() == 2) {

                if (coor.equals("-1")) //Cas abandon
                    return null;

                nb1 = (int) coor.charAt(0); //Recupere le code Ascii du premier character
                nb2 = (int) coor.charAt(1); //Recupere le code Ascii du second character.
                //si le premier caractère est une lettre entre A et H et le second un chiffre entre 1 et 8.
                if ( ( (nb1 >= 97 && nb1 <= 104) || (nb1 >= 65 && nb1 <= 72) ) && (nb2 >= 49 && nb2 <= 56)) {

                    //Le calcul dépend de si la lettre est minuscule/majuscule
                    if (nb1 <= 72)
                        nb1 = nb1 - 65;
                    else
                        nb1 = nb1 - 97;

                    return new Position(nb1, nb2 - 49);

                } else {
                    System.out.println("Veuillez entrer des coordonnées valides ! Une lettre de A à H avec un nombre de 1 à 8.");
                    coor = sc.nextLine();
                }
            }
            else {
                System.out.println("Veuillez entrer des coordonnées valides ! Une lettre de A à H avec un nombre de 1 à 8.");
                coor = sc.nextLine();
            }
        }
    }




}
