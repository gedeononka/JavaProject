import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create an Article object and get its information
        Article article = new Article();
        article.saisie();
        System.out.println("\nInformations de l'article:");
        article.affichage();

        // Create a Chemise object and get its information
        Chemise chemise = new Chemise();
        chemise.saisie();
        System.out.println("\nInformations de la chemise:");
        chemise.affichage();

        // Create a Montre object and get its information
        Montre montre = new Montre();
        montre.saisie();
        System.out.println("\nInformations de la montre:");
        montre.affichage();
    }
}
