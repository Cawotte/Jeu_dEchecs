package echec;

import java.lang.Enum;

public enum Couleur {
    BLANC('B'),
    NOIR('N');

    public char symb;

    //Constructeur
    Couleur(char symb) {
        this.symb = symb;
    }

    //Méthodes
    public Couleur couleurEnnemi() {
        if (this == BLANC)
            return NOIR;
        return BLANC;
    }

}
