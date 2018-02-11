package echec;

import java.util.Objects;

public class Position {

    //Attributs --------------
    private int a;
    private int n;

    //Constructeur --------------

    public Position() { }

    public Position(int a, int n) {
        this.a = a;
        this.n = n;
    }

    public Position(String coor) {
        if (coor.length() >= 2) {
            int asciiA = (int)coor.charAt(0);
            int asciiN = (int)coor.charAt(1);
            if ( asciiA >= 97 && asciiA <= 104) //Si asciiA est une lettre majuscule.
                this.a = asciiA-97;
            else if ( asciiA >= 65 && asciiA <= 72) //Si asciiA est une lettre minuscule
                this.a = asciiA-65;
            else
                this.a = -1; //ERROR

            if ( asciiN >= 49 && asciiN <= 56 ) //Si asciiN est un chiffre
                this.n = asciiN - 49;
            else
                this.a = -1; //ERROR
        }
        else {
            this.a = -1;
            this.n = -1;
        }
    }

    //Méthodes --------------
    public String positionToCoordonnee() {
        return "" + (char)(getA()+65) + (char)(getN()+49) ;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return a == position.a &&
                n == position.n;
    }
    @Override
    public int hashCode() {
        return Objects.hash(a, n);
    }

    //Booleens --------------
    /*
    public boolean equals(Position pos) {
        if (this.a == pos.getA() && this.n == pos.getN())
            return true;
        else
            return false;
    }*/

    //Accesseurs --------------
    public int getA() {
        return a;
    }
    public int getN() {
        return n;
    }
    public void setA(int a) { this.a = a; }
    public void setN(int n) { this.n = n; }

}
