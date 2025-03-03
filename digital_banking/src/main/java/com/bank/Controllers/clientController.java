package com.bank.Controllers;

import com.bank.DB;
import com.bank.Models.ClientModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class clientController {
    @FXML
    private TableColumn<ClientModel, String> tc_adr;
    @FXML
    private TableColumn<ClientModel, LocalDate> tc_dn;
    @FXML
    private TableColumn<ClientModel, String> tc_em;
    @FXML
    private TableColumn<ClientModel, Long> tc_id;
    @FXML
    private TableColumn<ClientModel, String> tc_np;
    @FXML
    private TableColumn<ClientModel, String> tc_tel;
    @FXML
    private TableView<ClientModel> tv_clients;
    @FXML
    private TextField tf_ci;
    @FXML
    private TextField tf_adr;
    @FXML
    private DatePicker tf_date;
    @FXML
    private TextField tf_em;
    @FXML
    private TextField tf_np;
    @FXML
    private TextField tf_tel;
    @FXML
    private Button btn_ajouter;
    @FXML
    private Label label;
    @FXML
    private Label labelid;
    @FXML
    private TextField tf_cin;
    @FXML
    private TextField tf_sel_adr;
    @FXML
    private TextField tf_sel_email;
    @FXML
    private TextField tf_sel_tel;

    private ClientModel clt;
    private Connection conn = null;
    private ResultSet rs = null;
    private PreparedStatement pstmt = null;

    @FXML
    public void load_page(ActionEvent event) {
        try {
            DB db = new DB();
            conn = db.getConnection();
            pstmt = conn.prepareStatement("select * from clients");
            rs = pstmt.executeQuery();
            tv_clients.getItems().clear();

            while (rs.next()) {
                tc_id.setCellValueFactory(new PropertyValueFactory<>("id_clt"));
                tc_dn.setCellValueFactory(new PropertyValueFactory<>("date_n"));
                tc_em.setCellValueFactory(new PropertyValueFactory<>("email"));
                tc_np.setCellValueFactory(new PropertyValueFactory<>("nomPrenom"));
                tc_tel.setCellValueFactory(new PropertyValueFactory<>("tel"));
                tc_adr.setCellValueFactory(new PropertyValueFactory<>("adr"));
                ObservableList<ClientModel> data = FXCollections.observableArrayList(
                        new ClientModel(
                                rs.getLong("id_clt"),
                                rs.getString("nomPrenom"),
                                rs.getDate("date_n"),
                                rs.getString("tel"),
                                rs.getString("email"),
                                rs.getString("adr"))
                );
                tv_clients.getItems().addAll(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlertWithHeaderText() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("AJOUT CLIENT");
        alert.setHeaderText("Message: ");
        alert.setContentText("Veuillez confirmer!");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.isPresent()) {
            ButtonType result = option.get();
            if (result == ButtonType.OK) {
                label.setText("Client ajouté ");
            } else if (result == ButtonType.CANCEL) {
                label.setText("Annulé");
            } else {
                label.setText("-");
            }
        }
    }

    @FXML
    public void register(MouseEvent event) {
        clt = new ClientModel();

        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date date1 = Date.from(tf_date.getValue().atStartOfDay(defaultZoneId).toInstant());

        clt.setId_clt(Long.parseLong(tf_ci.getText()));
        clt.setNomPrenom(tf_np.getText());
        clt.setDate_n(date1);
        clt.setTel(tf_tel.getText());
        clt.setEmail(tf_em.getText());
        clt.setAdr(tf_adr.getText());

        java.sql.Date sqlDate1 = new java.sql.Date(date1.getTime());

        try {
            DB db = new DB();
            conn = db.getConnection();
            String sql = "INSERT INTO clients (id_clt, nomPrenom, date_n, tel, email, adr) VALUES (?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, clt.getId_clt());
            pstmt.setString(2, clt.getNomPrenom());
            pstmt.setDate(3, sqlDate1);
            pstmt.setString(4, clt.getTel());
            pstmt.setString(5, clt.getEmail());
            pstmt.setString(6, clt.getAdr());
            pstmt.executeUpdate();
            load_page(null);  // reload clients list after insert
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void load_ligne() {
        try {
            DB db = new DB();
            conn = db.getConnection();
            pstmt = conn.prepareStatement("select * from clients where id_clt = (select max(id_clt) from clients) ");
            rs = pstmt.executeQuery();
            tv_clients.getItems().clear();

            while (rs.next()) {
                tc_id.setCellValueFactory(new PropertyValueFactory<>("id_clt"));
                tc_dn.setCellValueFactory(new PropertyValueFactory<>("date_n"));
                tc_em.setCellValueFactory(new PropertyValueFactory<>("email"));
                tc_np.setCellValueFactory(new PropertyValueFactory<>("nomPrenom"));
                tc_tel.setCellValueFactory(new PropertyValueFactory<>("tel"));
                tc_adr.setCellValueFactory(new PropertyValueFactory<>("adr"));
                ObservableList<ClientModel> data = FXCollections.observableArrayList(
                        new ClientModel(
                                rs.getLong("id_clt"),
                                rs.getString("nomPrenom"),
                                rs.getDate("date_n"),
                                rs.getString("tel"),
                                rs.getString("email"),
                                rs.getString("adr"))
                );
                tv_clients.getItems().addAll(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ajouterclt(ActionEvent event) {
        try {
            URL url = new File("src/main/resources/com/bank/Views/ajouterClient.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            Stage stage = new Stage();
            stage.setTitle("Ajouter client");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnCloseRequest(e -> load_ligne());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteClient(ActionEvent ae) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("SUPPRIMER CLIENT");
        alert.setHeaderText("Resultat:");
        alert.setContentText("Veuillez confirmer la suppression!");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK) {
            try {
                DB db = new DB();
                conn = db.getConnection();
                String sql = "delete from clients where id_clt=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setLong(1, Long.parseLong(labelid.getText()));
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    label.setText("Client supprimé avec succès");
                    load_page(null);  // Reload after deletion
                } else {
                    label.setText("Veuillez vérifier CIN client!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onTableItemSelect(MouseEvent event) {
        clt = tv_clients.getSelectionModel().getSelectedItem();
        if (clt != null) {
            labelid.setText(String.valueOf(clt.getId_clt()));
            tf_sel_tel.setText(clt.getTel());
            tf_sel_email.setText(clt.getEmail());
            tf_sel_adr.setText(clt.getAdr());
        }
    }

    @FXML
    private void onUpdateClt(MouseEvent event) {
        clt.setId_clt(Long.parseLong(labelid.getText()));
        clt.setTel(tf_sel_tel.getText());
        clt.setEmail(tf_sel_email.getText());
        clt.setAdr(tf_sel_adr.getText());
        try {
            DB db = new DB();
            conn = db.getConnection();
            String sql = "UPDATE clients SET tel=?, email=?, adr=? WHERE id_clt=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, clt.getTel());
            pstmt.setString(2, clt.getEmail());
            pstmt.setString(3, clt.getAdr());
            pstmt.setLong(4, clt.getId_clt());
            pstmt.executeUpdate();
            load_page(null);  // Reload clients list after update
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void searchClient(ActionEvent event) {
        Long cin = Long.parseLong(tf_cin.getText());
        try {
            DB db = new DB();
            conn = db.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM clients WHERE id_clt = ?");
            pstmt.setLong(1, cin);
            rs = pstmt.executeQuery();
            tv_clients.getItems().clear();

            while (rs.next()) {
                tc_id.setCellValueFactory(new PropertyValueFactory<>("id_clt"));
                tc_dn.setCellValueFactory(new PropertyValueFactory<>("date_n"));
                tc_em.setCellValueFactory(new PropertyValueFactory<>("email"));
                tc_np.setCellValueFactory(new PropertyValueFactory<>("nomPrenom"));
                tc_tel.setCellValueFactory(new PropertyValueFactory<>("tel"));
                tc_adr.setCellValueFactory(new PropertyValueFactory<>("adr"));
                ObservableList<ClientModel> data = FXCollections.observableArrayList(
                        new ClientModel(
                                rs.getLong("id_clt"),
                                rs.getString("nomPrenom"),
                                rs.getDate("date_n"),
                                rs.getString("tel"),
                                rs.getString("email"),
                                rs.getString("adr"))
                );
                tv_clients.getItems().addAll(data);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void initialize(URL url, ResourceBundle rb) {
        load_page(null);
    }
}
