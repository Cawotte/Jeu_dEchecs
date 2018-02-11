package echec;

import static echec.Couleur.*; //Permet de faire reconnaitre BLANC/NOIR comme type directement

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;

public class Plateau {

    //Attributs

    //Plateau est un singleton afin que les Pieces puissent y accéder pour des calculs de coups.
    private static Plateau Instance;

    //private static Plateau InstanceVirtuelle; //Utilisé uniquement pour le calcul d'echec et mat.

    private Piece plateau[][] = new Piece[8][8];
        // a...h = 0...7
        // n (1..8) = 0..7
        // [a][n]

    private HashMap posImportantes  = new HashMap();
    /*
        Contient les positions importantes à retenir pour éviter certains parcours de plateau supplémentaire, c'est une hashMap donc fonctionne par couple clé->valeurs :
        BLANC --> Position du Roi Blanc
        NOIR --> Position du Roi Noir
        "enPassant" --> Pion actuellement sensible à une prise "EnPassant" (sinon null)
        "promotion" --> contient les coordonnées du pion pouvant etre promu, est égale à null si il n'y en a aucun.
     */

    //Constructeurs

    /**
     * Génère une instance de Plateau si il n'en existe pas déjà.
     */
    public static Plateau getInstance() {
        if (Instance == null)
            Instance = new Plateau();
        return Instance;
    }

    /**
     * Réinitialise l'instance de Plateau. Est utilisé à chaque début de partie pour le vider de ses anciennes pièces.
     */
    public static Plateau getNewInstance() {
        Instance = new Plateau();
        return Instance;
    }



    //Méthodes

    /**
     * Initialise le tableau avec la configuration de base d'une partie d'echec.
     */
    public void initialiserPlateau() {
        //On place les pions.
        for (int a=0; a<8; a++) {
            this.plateau[a][1] = new Pion(BLANC);
            this.plateau[a][6] = new Pion(NOIR);
        }
        //On place les espaces vides du centre du plateau, meme si facultatif car deja null
        for (int a=0; a<8; a++)
            for (int n=2; n<6; n++)
                this.plateau[a][n] = null;

        //On place les pieces principales des blancs
        this.plateau[0][0] = new Tour(BLANC);
        this.plateau[1][0] = new Cavalier(BLANC);
        this.plateau[2][0] = new Fou(BLANC);
        this.plateau[3][0] = new Reine(BLANC);
        this.plateau[4][0] = new Roi(BLANC);
        posImportantes.put(BLANC, new Position(4,0));
        this.plateau[5][0] = new Fou(BLANC);
        this.plateau[6][0] = new Cavalier(BLANC);

        this.plateau[7][0] = new Tour(BLANC);

        //On place les pieces principales des noirs
        this.plateau[0][7] = new Tour(NOIR);
        this.plateau[1][7] = new Cavalier(NOIR);
        this.plateau[2][7] = new Fou(NOIR);
        this.plateau[3][7] = new Reine(NOIR);
        this.plateau[4][7] = new Roi(NOIR);
        posImportantes.put(NOIR, new Position(4,7));
        this.plateau[5][7] = new Fou(NOIR);
        this.plateau[6][7] = new Cavalier(NOIR);
        this.plateau[7][7] = new Tour(NOIR);

        posImportantes.put("enPassant",null);

    }

    /**
     * Initialise les cases du Plateau avec les informations présentes dans le fichier donnée.
     */
    public void initialiserPlateau(File fe) {
        try {
            String input, nomP;
            Position pos;
            Couleur couleur = BLANC;
            Scanner scFile= new Scanner(fe);

            while ( scFile.hasNext() ) {
                input = scFile.next();
                if ( input.equals("white") )
                    couleur = BLANC;
                else if ( input.equals("black") )
                    couleur = NOIR;
                else { //Sinon c'est soit une coordonnée ou une piece.
                    nomP = input; //On déplace l'identité de la pièce dans une autre variable pour lire les coordonnées.
                    input = scFile.next();
                    System.out.println(nomP +" : "+ input);
                    pos = new Position(input);
                    if ( pos.getN() >= 0 && pos.getA() >= 0 ) //On vérifie qu'il y a pas le -1 de l'invalidité.
                    switch (nomP.charAt(0)) {
                        case 'P' :
                        case 'p' : // Pion = pawn
                            this.plateau[pos.getA()][pos.getN()] = new Pion(couleur);
                            break;
                        case 'T' :
                        case 't' : // Tour. (Rook) mais Tour.
                            this.plateau[pos.getA()][pos.getN()] = new Tour(couleur);
                            break;
                        case 'C' :
                        case 'c' : // Cavalier
                            this.plateau[pos.getA()][pos.getN()] = new Cavalier(couleur);
                            break;
                        case 'B' :
                        case 'b' : // Fou = Bishop
                            this.plateau[pos.getA()][pos.getN()] = new Fou(couleur);
                            break;
                        case 'Q' :
                        case 'q' : // Reine = Queen
                            this. plateau[pos.getA()][pos.getN()] = new Reine(couleur);
                            break;
                        case 'K' :
                        case 'k' : // Roi = King, Cavalier = Knight.
                            if (nomP.length() == 1) { //Le roi n'a pas de numérale.
                                this.plateau[pos.getA()][pos.getN()] = new Roi(couleur, pos);
                                posImportantes.put(couleur, pos);
                            }
                            else
                                this.plateau[pos.getA()][pos.getN()] = new Cavalier(couleur);
                            break;
                        default :
                    }
                }

            }
            posImportantes.put("enPassant",null);
            scFile.close();
        }
        catch (FileNotFoundException e) {

        }


    }

    /**
     * Affiche le plateau de jeu dans son état actuel dans la console.
     */
    public void afficherPlateau() {
        System.out.println("--|----|----|----|----|----|----|----|----|");
        for (int n = 7; n>=0; n--) {
            System.out.print(" "+(n+1)+"|"); //Numérote les lignes.
            for (int a = 0; a<8; a++) {

                if ( plateau[a][n] != null )
                    System.out.print(" "+plateau[a][n].getSymbole() + plateau[a][n].getCouleur().symb+" ");
                    // ""+char+char permet de concatener des char, sinon ils sont transformée en numérals ascii.
                else
                    System.out.print("    ");

                System.out.print("|");
            }
            System.out.println(); //retour chariot.
            System.out.println("--|----|----|----|----|----|----|----|----|");
        }
        System.out.println("  | A  | B  | C  | D  | E  | F  | G  | H  |"); //'Numérote' les colonnes
    }

    /**
     * Renvoie la piece dans la case plateau[a][n]. Attention, il n'y a pas de vérification InBound !
     */
    public Piece contenuCase(int a, int n) { return plateau[a][n];  }
        // Surcharge de contenuCase pour plus de flexibilité
    public Piece contenuCase(Position pos) { return contenuCase(pos.getA(), pos.getN()); }

    /**
     * Déplace la piece en position PosDepart à la position PosFin. Attention, toute les vérifications de légalités du coup doivent etre faites avant !
     * @param posDep Position de la piece a déplacer
     * @param posFin Position de la destination
     * @return Renvoie la pièce mangé ou null si la case était vide
     */
    public Piece deplacerPiece(Position posDep, Position posFin) {
        Piece pieceMangee = plateau[posFin.getA()][posFin.getN()];
        Piece pieceDeplacee = plateau[posDep.getA()][posDep.getN()];

        //CAS PARTICULIER : PION --------------
        //Si la piece est un Pion, on set son attribut premierCoup en false pour désactiver le 'Bond'.
        if (pieceDeplacee instanceof Pion) {

            //Si c'est le premier déplacement du pion
            //if ( !((Pion)pieceDeplacee).getaSprinte() ) {
                if (Math.abs(posDep.getN() - posFin.getN()) == 2) //Si il bouge de deux cases, c'est un sprint, donc il devient sensible au "EnPassant"
                    posImportantes.put("EnPassant", posFin);
                //((Pion) pieceDeplacee).setaSprinte(true);
            //}

            //On vérifie si il mange un autre pion 'en passant'
            //C'est le cas si il change de colonne sans atterrir sur un ennemi.
            if ( posDep.getA() != posFin.getA() && pieceMangee == null) {
                //Deplacement du pion
                plateau[posFin.getA()][posFin.getN()] = plateau[posDep.getA()][posDep.getN()];
                plateau[posDep.getA()][posDep.getN()] = null;
                //Il mange le pion derrière sa case d'arrivé, donc il y a deux cas en fonction de sa couleur
                if ( ((Pion) pieceDeplacee).getCouleur() == BLANC ) {
                    pieceMangee = plateau[posFin.getA()][posFin.getN()-1];
                    plateau[posFin.getA()][posFin.getN()-1] = null; //La case derrière sa case d'arrivé est la case du pion mangée
                }
                else {
                    pieceMangee = plateau[posFin.getA()][posFin.getN()+1];
                    plateau[posFin.getA()][posFin.getN()+1] = null; //La case derrière sa case d'arrivé est la case du pion mangée
                }
                return pieceMangee;
            }

        }
        // FIN CAS PARTICULIER --- PION


        // CAS PARTICULIER --- ROI ( & Ses roques )
        if (pieceDeplacee instanceof Roi ) {
            //Si le roi se déplace de deux case, on déduit qu'il fait un roque.
            if ( Math.abs(posDep.getA() - posFin.getA()) == 2) {
                //On différencie le cas petit et grand roque :
                //Petit Roque (si on voit que le roi se déplace vers la droite) :
                if ( posDep.getA() < posFin.getA() ) {
                    //Deplacement du Roi
                    plateau[6][posFin.getN()] = plateau[posDep.getA()][posDep.getN()]; //On bouge le roi dans sa nouvelle case
                    plateau[4][posDep.getN()] = null; //On vide son ancienne case.
                    //Deplacement de la Tour
                    plateau[5][posFin.getN()] = plateau[7][posFin.getN()];
                    plateau[7][posFin.getN()] = null;
                }
                else { //Grand Roque
                    //Deplacement du Roi
                    plateau[2][posFin.getN()] = plateau[posDep.getA()][posDep.getN()];
                    plateau[4][posDep.getN()] = null;
                    //Deplacement de la Tour
                    plateau[3][posFin.getN()] = plateau[0][posFin.getN()];
                    plateau[0][posFin.getN()] = null;

                }
                ((Roi) pieceDeplacee).setMoved(true);
                return pieceMangee;
            }
            ((Roi) pieceDeplacee).setMoved(true); //Le roi a bougé, il ne peut plus roque.
        }

        if (pieceDeplacee instanceof Tour)
            ((Tour) pieceDeplacee).setMoved(true);

        //La piece à la position de depart prend la place de celle à la position de fin, laissant un vide (null) derrière elle.
        if ( pieceDeplacee instanceof Pion && (posFin.getN() == 0 ||  posFin.getN() == 7 ))
            setPosImportantes("promotion", posFin);

        plateau[posFin.getA()][posFin.getN()] = plateau[posDep.getA()][posDep.getN()];
        plateau[posDep.getA()][posDep.getN()] = null;

        return pieceMangee;
    }

    /**
     update() mets à jour le plateau de jeu :
        - Chaque pion calcule tout ses coups possibles.
        - Un message est affiché si un roi est en echec
        - Si un pion était sensible à une prise en passant, lui retire cet état après le calcul des coups.
     */
    public void update() {
        Piece piece;
        boolean RoiNoirEchec = false;
        boolean RoiBlancEchec = false;
        //On parcours tout le plateau, mettant à jour la liste de coups de toute les pièces.
        for (int a = 0; a<8; a++) {
            for (int n = 0; n<8; n++) {
                piece = plateau[a][n];
                if (piece != null) {
                    piece.coupsPossibles(a, n);
                    //On vérifie si il y a echec :
                    if (piece.getCouleur() == BLANC && piece.contientCoord( (Position)posImportantes.get(NOIR)))
                        RoiNoirEchec = true;

                    else if (plateau[a][n].getCouleur() == NOIR && plateau[a][n].contientCoord((Position)posImportantes.get(BLANC)))
                        RoiBlancEchec = true;

                }
            }
        }

        if ( RoiBlancEchec )
            System.out.println("Le roi blanc a été mis en echec !");
        if ( RoiNoirEchec )
            System.out.println("Le roi noir a été mis en echec !");

        //On déclare le pion qui était sensible au 'En Passant' comme désormais non sensible. Le coup permettant de le manger
        // a déjà été enregistré.
        if (posImportantes.get("EnPassant") != null)
            posImportantes.put("EnPassant", null);

    }

    /**
     * Utilise la couleur de la Piece passé en argument et le scanner donné afin de récupérer une entrée utilisateur,
     * et renvoyer la piece en laquelle le pion doit être promu.
     */
    public Piece promotion(Couleur coul, Scanner sc) {

        String input;
        boolean loop = true;
        Piece newPiece = null;

        System.out.println("Votre pion a obtenu une promotion ! Choississez un type de piece en tapant la lettre associée.");
        System.out.println("Tour(T) - Cavalier(C) - Fou(F) - Reine(R) :");

        input = sc.nextLine();
        //On loop tant que l'entrée est pas valide.
        while (loop) {
            loop = false;
            switch (input.charAt(0)) {
                case 'T' :
                case 't' :
                    newPiece = new Tour(coul);
                    break;
                case 'C' :
                case 'c' :
                    newPiece = new Cavalier(coul);
                    break;
                case 'F' :
                case 'f' :
                    newPiece = new Fou(coul);
                    break;
                case 'R' :
                case 'r' :
                    newPiece = new Reine(coul);
                    break;
                default :
                    System.out.println("Veuillez entrer un choix valide !");
                    input = sc.nextLine();
                    loop = true;
            }
        }
        //La promotion est quasiment effectué (Elle se termine juste après être sortie de cette méthode)
        //donc on retire la position de la piece promu de posImportantes
        posImportantes.put("promotion", null);
        return newPiece;
    }

    /**
     * Renvoie un ArrayList avec tout les coups possibles de toute les pièces du camp de la couleur 'coul'
     */
    public ArrayList<Position> ToutLesCoupsDes(Couleur coul) {
        Piece piece;
        ArrayList<Position> ListeDeCoupsDesPieces = new ArrayList();
        for (int a = 0; a<8; a++) {
            for (int n = 0; n<8; n++) {
                piece = plateau[a][n];
                if (piece != null && !(piece instanceof Roi) && piece.getCouleur() == coul) {
                    piece.coupsPossibles(a, n);
                    ListeDeCoupsDesPieces.addAll( piece.getListeDeCoups() );
                }
            }
        }
        return ListeDeCoupsDesPieces;
    }

    /**
     * Renvoie un ArrayList avec tout pieces du camp de couleur 'coul' qui peuvent bouger.
     * Utilisé par l'IA pour choisir aléatoirement une pièce à jouer.
     */
    public ArrayList<Piece> pieceJouables(Couleur coul) {
        Piece piece;
        ArrayList<Piece> pieceJouables = new ArrayList();
        for (int a = 0; a<8; a++) {
            for (int n = 0; n<8; n++) {
                piece = plateau[a][n];
                if (piece != null && piece.getCouleur() == coul && !piece.listeCoupIsEmpty() ) {
                    pieceJouables.add(piece);
                }
            }
        }
        return pieceJouables;
    }

    //Accesseurs
    public void setCase(Piece piece, int a, int n) {
        plateau[a][n] = piece;
    }
    public void setCase(Piece piece, Position pos) {
        plateau[pos.getA()][pos.getN()] = piece;
    }

    //Ajoute la valeur V à la HashMap posImportantes avec la clé K. Modifie la valeur si le couple clé/valeur existé déjà.
    public <K,V> void setPosImportantes( K key, V value) {
        posImportantes.put(key, value);
    }
    public <K,V> V getPosImportantes(K key) {
        return (V)posImportantes.get(key);
    }

}

