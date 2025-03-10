package com.bank.Controllers;

import com.bank.DB;
import com.bank.Models.CompteModel;
import com.bank.Models.TypeCompte;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class compteController implements Initializable {

    // Zone de recherche
    @FXML
    private TextField tf_cin;
    @FXML
    private Button btn_search;

    // TableView et colonnes
    @FXML
    private TableView<CompteModel> tv_comptes;
    @FXML
    private TableColumn<CompteModel, Long> tc_num;
    @FXML
    private TableColumn<CompteModel, String> tc_ty;
    @FXML
    private TableColumn<CompteModel, Float> tc_sol;
    @FXML
    private TableColumn<CompteModel, Long> tc_cin;
    @FXML
    private TableColumn<CompteModel, Boolean> tc_etat;
    @FXML
    private TableColumn<CompteModel, Date> tc_date_ouverture;

    // Boutons de contrôle
    @FXML
    private Button btn_activ;
    @FXML
    private Button btn_load;

    // Zone d'ouverture de compte
    @FXML
    private TextField tf_ci_add;
    @FXML
    private ComboBox<TypeCompte> cb_type;
    @FXML
    private Button btn_valider;

    // Filtres (RadioButtons)
    @FXML
    private RadioButton rb_activ;
    @FXML
    private RadioButton rb_desactiv;
    @FXML
    private RadioButton rb_both;

    // Compte sélectionné dans le TableView
    private CompteModel selectedCompte;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation des colonnes du TableView
        tc_num.setCellValueFactory(new PropertyValueFactory<>("num_c"));
        tc_ty.setCellValueFactory(new PropertyValueFactory<>("type_c"));
        tc_sol.setCellValueFactory(new PropertyValueFactory<>("solde_c"));
        tc_cin.setCellValueFactory(new PropertyValueFactory<>("id_c"));
        tc_etat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        tc_date_ouverture.setCellValueFactory(new PropertyValueFactory<>("date_ouverture"));

        // Initialisation du ComboBox avec les types de compte
        cb_type.getItems().setAll(TypeCompte.values());

        // Configuration du ToggleGroup pour les RadioButtons
        ToggleGroup toggleGroup = new ToggleGroup();
        rb_activ.setToggleGroup(toggleGroup);
        rb_desactiv.setToggleGroup(toggleGroup);
        rb_both.setToggleGroup(toggleGroup);
        rb_both.setSelected(true);

        // Configuration des actions des boutons
        btn_load.setOnAction(e -> loadData());
        btn_search.setOnAction(e -> searchCINCompte());
        btn_valider.setOnAction(e -> {
            if (confirmAccountCreation()) {
                ouvrirCompte();
                loadData();
            }
        });
        btn_activ.setOnAction(e -> {
            if (selectedCompte != null) {
                toggleAccountStatus();
                loadData();
            } else {
                showAlert(Alert.AlertType.WARNING, "Aucun compte sélectionné", "Veuillez sélectionner un compte.");
            }
        });

        // Configuration des filtres par état via les RadioButtons
        rb_activ.setOnAction(e -> searchActivCompte());
        rb_desactiv.setOnAction(e -> searchDesactivCompte());
        rb_both.setOnAction(e -> loadData());

        // Écouteur sur la sélection d'un compte dans le TableView
        tv_comptes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedCompte = newSelection;
        });

        // Chargement initial des données
        loadData();
    }

    // Méthode qui charge tous les comptes depuis la base de données
    private void loadData() {
        try {
            DB db = new DB();
            Connection conn = db.getConnection();
            String sql = "SELECT * FROM comptes";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ObservableList<CompteModel> comptes = FXCollections.observableArrayList();
            while (rs.next()) {
                CompteModel compte = new CompteModel(
                        rs.getLong("num_c"),
                        TypeCompte.valueOf(rs.getString("type_c")),
                        rs.getFloat("solde_c"),
                        rs.getLong("id_c"),
                        rs.getBoolean("etat"),
                        rs.getDate("date_ouverture")
                );
                comptes.add(compte);
            }
            tv_comptes.setItems(comptes);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des comptes.");
        }
    }

    // Recherche d'un compte par CIN
    private void searchCINCompte() {
        String cinStr = tf_cin.getText();
        if (cinStr == null || cinStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champ vide", "Veuillez entrer un CIN pour la recherche.");
            return;
        }
        try {
            long cin = Long.parseLong(cinStr);
            DB db = new DB();
            Connection conn = db.getConnection();
            String sql = "SELECT * FROM comptes WHERE id_c = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setLong(1, cin);
            ResultSet rs = pst.executeQuery();
            ObservableList<CompteModel> comptes = FXCollections.observableArrayList();
            while (rs.next()) {
                CompteModel compte = new CompteModel(
                        rs.getLong("num_c"),
                        TypeCompte.valueOf(rs.getString("type_c")),
                        rs.getFloat("solde_c"),
                        rs.getLong("id_c"),
                        rs.getBoolean("etat"),
                        rs.getDate("date_ouverture")
                );
                comptes.add(compte);
            }
            tv_comptes.setItems(comptes);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le CIN doit être un nombre.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la recherche du compte.");
        }
    }

    private void ouvrirCompte() {
        String cinStr = tf_ci_add.getText();

        if (cinStr == null || cinStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champ vide", "Veuillez entrer un CIN pour ouvrir un compte.");
            return;
        }

        try {
            long cin = Long.parseLong(cinStr);
            TypeCompte type = cb_type.getSelectionModel().getSelectedItem();

            if (type == null) {
                showAlert(Alert.AlertType.WARNING, "Champ vide", "Veuillez sélectionner un type de compte.");
                return;
            }

            DB db = new DB();
            Connection conn = db.getConnection();

            // Générer un numéro de compte unique à 3 chiffres (100-999)
            long numCompte = genererNumeroCompte(conn);

            // Insérer le nouveau compte avec le numéro généré
            String sql = "INSERT INTO comptes(num_c, type_c, solde_c, id_c, etat,date_ouverture) VALUES (?, ?, 0.0, ?, 1,CURRENT_DATE)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setLong(1, numCompte);  // num_c : numéro de compte généré
            pst.setString(2, type.toString());  // type_c : type du compte
            pst.setLong(3, cin);  // id_c : numéro CIN du client

            int rowsInserted = pst.executeUpdate();

            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte ouvert avec succès. Numéro de compte : " + numCompte);
            } else {
                showAlert(Alert.AlertType.ERROR, "Échec", "L'ouverture du compte a échoué.");
            }

            pst.close();
            conn.close();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le CIN doit être un nombre valide.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de l'ouverture du compte.");
        }
    }

    /**
     * Génère un numéro de compte unique à 3 chiffres (100-999)
     */
    private long genererNumeroCompte(Connection conn) throws SQLException {
        Random rand = new Random();
        long numCompte;

        do {
            numCompte = 100 + rand.nextInt(900); // Génère un nombre entre 100 et 999
        } while (verifierNumeroExiste(conn, numCompte)); // Vérifier si le numéro existe déjà

        return numCompte;
    }

    /**
     * Vérifie si un numéro de compte existe déjà dans la base
     */
    private boolean verifierNumeroExiste(Connection conn, long numCompte) throws SQLException {
        String sql = "SELECT COUNT(*) FROM comptes WHERE num_c = ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setLong(1, numCompte);
        ResultSet rs = pst.executeQuery();

        boolean existe = false;
        if (rs.next()) {
            existe = rs.getInt(1) > 0;
        }

        rs.close();
        pst.close();
        return existe;
    }


    // Confirmation de la création d'un compte
    private boolean confirmAccountCreation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ouvrir compte");
        alert.setHeaderText("Confirmer l'ouverture");
        alert.setContentText("Voulez-vous vraiment ouvrir ce compte ?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // Bascule de l'état (activation/désactivation) du compte sélectionné
    private void toggleAccountStatus() {
        if (selectedCompte == null) {
            showAlert(Alert.AlertType.WARNING, "Aucun compte sélectionné", "Veuillez sélectionner un compte.");
            return;
        }
        try {
            DB db = new DB();
            Connection conn = db.getConnection();
            String sql = "UPDATE comptes SET etat = NOT etat WHERE num_c = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setLong(1, selectedCompte.getNum_c());
            pst.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'état du compte a été modifié.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification de l'état du compte.");
        }
    }

    // Recherche des comptes activés
    private void searchActivCompte() {
        try {
            DB db = new DB();
            Connection conn = db.getConnection();
            String sql = "SELECT * FROM comptes WHERE etat = 1";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ObservableList<CompteModel> comptes = FXCollections.observableArrayList();
            while (rs.next()) {
                CompteModel compte = new CompteModel(
                        rs.getLong("num_c"),
                        TypeCompte.valueOf(rs.getString("type_c")),
                        rs.getFloat("solde_c"),
                        rs.getLong("id_c"),
                        rs.getBoolean("etat"),
                        rs.getDate("date_ouverture")
                );
                comptes.add(compte);
            }
            tv_comptes.setItems(comptes);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la recherche des comptes activés.");
        }
    }

    // Recherche des comptes désactivés
    private void searchDesactivCompte() {
        try {
            DB db = new DB();
            Connection conn = db.getConnection();
            String sql = "SELECT * FROM comptes WHERE etat = 0";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            ObservableList<CompteModel> comptes = FXCollections.observableArrayList();
            while (rs.next()) {
                CompteModel compte = new CompteModel(
                        rs.getLong("num_c"),
                        TypeCompte.valueOf(rs.getString("type_c")),
                        rs.getFloat("solde_c"),
                        rs.getLong("id_c"),
                        rs.getBoolean("etat"),
                        rs.getDate("date_ouverture")
                );
                comptes.add(compte);
            }
            tv_comptes.setItems(comptes);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la recherche des comptes désactivés.");
        }
    }

    // Méthode utilitaire pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
