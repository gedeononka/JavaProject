import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create a Produit object
        Produit produit = new Produit();

        System.out.println("Saisie des informations du produit:");
        produit.saisie();  // Calls saisie method to input product info

        System.out.println("\nInformations du produit:");
        produit.affiche();  // Displays the product information

        // You can also test ProduitImpl
        ProduitImpl produitImpl = new ProduitImpl("001", "Produit A", 100, 29.99);
        System.out.println("\nInformations du produit avec ProduitImpl:");
        produitImpl.affiche();  // Displays the product information from ProduitImpl
    }
}
