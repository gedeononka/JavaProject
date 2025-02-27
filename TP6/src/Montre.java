import java.util.Scanner;
public class Montre extends Article implements IMontre {
    private String marque;

    public Montre() {}

    public Montre(int id, String lib, String marque) {
        super(id, lib);
        this.marque = marque;
    }

    @Override
    public void saisie() {
        super.saisie();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Saisie des informations de la Montre");
        System.out.print("Entrez la marque: ");
        this.marque = scanner.nextLine();
    }

    @Override
    public void affichage() {
        super.affichage();
        System.out.println("Marque: " + marque);
    }

    // Getters and setters
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
}

