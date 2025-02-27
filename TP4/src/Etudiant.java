import java.util.Scanner;
public class Etudiant extends Personne {
    private String matricule;
    private double moyenne;

    public Etudiant() {}

    public Etudiant(String nom, String prenom, String matricule, double moyenne) {
        super(nom, prenom);
        this.matricule = matricule;
        this.moyenne = moyenne;
    }

    @Override
    public void saisie() {
        super.saisie(); // Call parent saisie method
        // Additional Etudiant-specific input logic
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez le matricule: ");
        this.matricule = scanner.nextLine();
        System.out.print("Entrez la moyenne: ");
        this.moyenne = scanner.nextDouble();
    }

    @Override
    public void affiche() {
        super.affiche(); // Call parent affiche method
        System.out.println("Matricule: " + matricule);
        System.out.println("Moyenne: " + moyenne);
    }

    // Getters and setters
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public double getMoyenne() { return moyenne; }
    public void setMoyenne(double moyenne) { this.moyenne = moyenne; }
}
