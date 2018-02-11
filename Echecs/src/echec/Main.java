package echec;

public class Main {


    public static void main(String[] args) {

        int choix;
        boolean quitter = false;
        boolean avecIA = false;

        Jeu leJeu = new Jeu();
        while (!quitter) {
            choix = leJeu.menu();
            switch(choix) {
                case 1:
                    leJeu.jouerUnePartie(false, avecIA);
                    break;
                case 2 :
                    leJeu.jouerUnePartie(true, avecIA);
                    break;
                case 3 :
                    if (avecIA) {
                        avecIA = false;
                        System.out.println("L'IA a été désactivée !");
                    }
                    else {
                        avecIA = true;
                        System.out.println("L'IA a été activée !");
                    }
                    break;
                case 4 :
                    System.out.println("Au revoir!");
                    quitter = true;
                    break;
                default:
                    System.out.println("Cas impossible ! On aborte !");
                    quitter = true;
                    break;
            }
        }
        leJeu.sc.close();

    }
}
