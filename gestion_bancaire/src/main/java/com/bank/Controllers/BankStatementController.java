package com.bank.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.util.Date;


public class BankStatementController {

    @FXML
    private TextField accountNumberField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    public void generateStatement() {
        try {
            long accountNumber = Long.parseLong(accountNumberField.getText());
            Date startDate = java.sql.Date.valueOf(startDatePicker.getValue());
            Date endDate = java.sql.Date.valueOf(endDatePicker.getValue());

            com.bank.PDF.BankStatementPDFGenerator.generateBankStatement(accountNumber, startDate, endDate);
            System.out.println("Relevé généré avec succès.");
        } catch (NumberFormatException e) {
            System.err.println("Numéro de compte invalide.");
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du relevé : " + e.getMessage());
            e.printStackTrace();
        }
    }
}