import java.util.Scanner;

public class Personne implements IPersonne {
    private String nom;
    private String prenom;

    public Personne() {}

    public Personne(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public void saisieP() {
        // Method for input of Personne
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez le nom: ");
        this.nom = scanner.nextLine();
        System.out.print("Entrez le prénom: ");
        this.prenom = scanner.nextLine();
    }

    @Override
    public void saisie() {
        saisieP(); // Calls specific saisieP method
    }

    @Override
    public void affiche() {
        System.out.println("Nom: " + nom);
        System.out.println("Prénom: " + prenom);
    }

    // Getters and setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
}

