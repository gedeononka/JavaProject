import java.util.Scanner;
public class Chemise extends Article implements IChemise {
    private String couleur;

    public Chemise() {}

    public Chemise(int id, String lib, String couleur) {
        super(id, lib);
        this.couleur = couleur;
    }

    @Override
    public void saisie() {
        super.saisie();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Saisie des informations de la Chemise");
        System.out.print("Entrez la couleur: ");
        this.couleur = scanner.nextLine();
    }

    @Override
    public void affichage() {
        super.affichage();
        System.out.println("Couleur: " + couleur);
    }

    // Getters and setters
    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
}

