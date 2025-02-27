import java.util.Scanner;
public class Article implements IArticle {
    private int id;
    private String lib;

    public Article() {}

    public Article(int id, String lib) {
        this.id = id;
        this.lib = lib;
    }

    @Override
    public void saisie() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Saisie des informations de l'Article");

        System.out.print("Entrez l'ID: ");
        this.id = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over
        System.out.print("Entrez le libellé: ");
        this.lib = scanner.nextLine();
    }

    @Override
    public void affichage() {
        System.out.println("ID: " + id);
        System.out.println("Libellé: " + lib);
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getLib() { return lib; }
    public void setLib(String lib) { this.lib = lib; }
}

