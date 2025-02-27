import java.util.Scanner;

public class Produit implements IProduit {
    private String ref;
    private String libelle;
    private double quantite;
    private double prix;

    // Constructors
    public Produit() {}

    public Produit(String ref, String libelle, double quantite, double prix) {
        this.ref = ref;
        this.libelle = libelle;
        this.quantite = quantite;
        this.prix = prix;
    }

    // Method to get input for the product
    @Override
    public void saisie() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Saisie des informations du Produit");

        System.out.print("Entrez la référence: ");
        this.ref = scanner.nextLine();

        System.out.print("Entrez le libellé: ");
        this.libelle = scanner.nextLine();

        System.out.print("Entrez la quantité: ");
        this.quantite = scanner.nextDouble();

        System.out.print("Entrez le prix: ");
        this.prix = scanner.nextDouble();
    }

    // Method to display product information
    @Override
    public void affiche() {
        System.out.println("Référence: " + ref);
        System.out.println("Libellé: " + libelle);
        System.out.println("Quantité: " + quantite);
        System.out.println("Prix: " + prix);
    }

    // Getters and setters
    public String getRef() { return ref; }
    public void setRef(String ref) { this.ref = ref; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public double getQuantite() { return quantite; }
    public void setQuantite(double quantite) { this.quantite = quantite; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
}
