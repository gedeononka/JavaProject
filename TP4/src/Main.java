import java.util.Scanner;

// Main class for testing
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create an Etudiant object
        Etudiant etudiant = new Etudiant();
        System.out.println("Saisie des informations de l'étudiant:");
        etudiant.saisie();
        System.out.println("\nInformations de l'étudiant:");
        etudiant.affiche();

        // Create an Employe object
        Employe employe = new Employe();
        System.out.println("\nSaisie des informations de l'employé:");
        employe.saisie();
        System.out.println("\nInformations de l'employé:");
        employe.affiche();
    }
}
