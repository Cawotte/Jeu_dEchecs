package echec;


import java.util.ArrayList;

import static echec.Couleur.*; //Permet de faire reconnaitre BLANC/NOIR comme type directement

public class Roi extends Piece {

    private boolean moved = false;

    public Roi(Couleur couleur) {
        super(couleur);
        super.name = "Roi";
        super.symbole = 'K';
    }


    //Constructeur utilisé lors de la lecture de fichier (Pour savoir si il a déjà 'bougé')
    public Roi(Couleur couleur, Position pos) {
        super(couleur);
        super.name = "Roi";
        super.symbole = 'K';
        //On définit moved en fonction de si le Roi est sur sa position initiale ou non.
        if ( pos.getA() == 4 && (pos.getN() == 0 && couleur == BLANC) || (pos.getN() == 7 && couleur == NOIR) )
            this.moved = false; //Il est sur une position initiale, il n'as pas bougé.
        else
            this.moved = true;
    }

    public void coupsPossibles(int a, int n) {

        //Le roi ajoute sa position dans la HashMap de plateau, pour les vérification d'echec.
        Plateau.getInstance().setPosImportantes(couleur, new Position(a,n));

        ListeDeCoups.clear();
        //On vérifie que pour chaque case autour du roi si elle est libre où a un ennemi.
        if ( inBound(a,n+1) && caseVideOuEnnemi(a,n+1) )
            ListeDeCoups.add(new Position(a, n+1)); //N

        if ( inBound(a+1,n+1) && caseVideOuEnnemi(a+1,n+1) )
            ListeDeCoups.add(new Position(a+1, n+1)); //NE

        if ( inBound(a+1,n) && caseVideOuEnnemi(a+1,n) )
            ListeDeCoups.add(new Position(a+1, n)); //E

        if ( inBound(a+1,n-1) && caseVideOuEnnemi(a+1,n-1) )
            ListeDeCoups.add(new Position(a+1, n-1)); //SE

        if ( inBound(a,n-1) && caseVideOuEnnemi(a,n-1) )
            ListeDeCoups.add(new Position(a, n-1)); //S

        if ( inBound(a-1,n-1) && caseVideOuEnnemi(a-1,n-1) )
            ListeDeCoups.add(new Position(a-1, n-1)); //SO

        if ( inBound(a-1,n) && caseVideOuEnnemi(a-1,n) )
            ListeDeCoups.add(new Position(a-1, n)); //O

        if ( inBound(a-1,n+1) && caseVideOuEnnemi(a-1,n+1) )
            ListeDeCoups.add(new Position(a-1, n+1)); //NO

        //On vérifie si le roi peut faire un roque.
        if ( peutPetitRoque(a, n, a+3, n) )
            ListeDeCoups.add(new Position(a+2, n));
        if ( peutGrandRoque(a, n, a-4, n) )
            ListeDeCoups.add(new Position(a-2, n));

    }

    //Booleens

    //Vérifie si le roi en (a1,n1) peut faire un petit roque avec une Tour en (a2, n2)
    public boolean peutPetitRoque(int a1, int n1, int a2, int n2) {
        ArrayList<Position> ListeDesCoupsAdverses;

        //On vérifie que le roi n'a jamais bougé, est sur sa position de départ, et que les coordoonées sont cohérentes
        if ( !getMoved() && a1 == 4 && a2 == 7 && n1 == n2 )
            //On vérifie que la Tour est une Tour et est sur sa position initial et si la tour est le roi sont du même camp
            if ( contenuCase(a2, n2) instanceof Tour && !((Tour) contenuCase(a2, n2)).getMoved() && couleur == contenuCase(a2, n2).getCouleur() )
                //On vérifie si les cases entre elles sont vide  sont vides :
                if ( caseVide(5, n1) && caseVide(6, n1) ) {
                    //Seulement si toutes ses conditions sont réunis on vérifie si les cases ne sont pas en echec pour éviter d'utiliser trop de
                    //puissance de calcul.
                    ListeDesCoupsAdverses = Plateau.getInstance().ToutLesCoupsDes(couleur.couleurEnnemi());

                    if ( !ListeDesCoupsAdverses.contains(new Position(a1, n1)) && !ListeDesCoupsAdverses.contains(new Position(5, n1)) && !ListeDesCoupsAdverses.contains(new Position(6, n1)) )
                        return true;
                }
        return false;
    }

    //Vérifie si le roi en (a1,n1) peut faire un grand roque avec une Tour en (a2, n2)
    public boolean peutGrandRoque(int a1, int n1, int a2, int n2) {

        ArrayList<Position> ListeDesCoupsAdverses;

        //On vérifie que le roi n'a jamais bougé, est sur sa position de départ, et que les coordoonées sont cohérentes
        if ( !getMoved() && a1 == 4 && a2 == 0 && n1 == n2 )
            //On vérifie que la Tour est une Tour et est sur sa position initial et si la tour est le roi sont du même camp
            if ( contenuCase(a2, n2) instanceof Tour && !((Tour) contenuCase(a2, n2)).getMoved() && couleur == contenuCase(a2, n2).getCouleur() )
                //On vérifie si les cases entre elles sont vide  sont vides :
                if ( caseVide(1, n1) && caseVide(2, n1) && caseVide(3, n1) ){
                    //Seulement si toutes tes conditions sont réunis on vérifie si les cases ne sont pas en echec pour éviter d'utiliser trop de
                    //puissance de calcul dès le début
                    ListeDesCoupsAdverses = Plateau.getInstance().ToutLesCoupsDes(couleur.couleurEnnemi());

                    if ( !ListeDesCoupsAdverses.contains(new Position(1, n1)) && !ListeDesCoupsAdverses.contains(new Position(2, n1)) && !ListeDesCoupsAdverses.contains(new Position(3, n1)) )
                        return true;
                }
        return false;
    }

    //Accesseurs
    public boolean getMoved() {
        return moved;
    }
    public void setMoved(boolean bool) {
        this.moved = bool;
    }

}