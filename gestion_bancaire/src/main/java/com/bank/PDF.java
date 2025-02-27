package com.bank;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDF {
    public static class BankStatementPDFGenerator {

        public static void generateBankStatement(long accountNumber, Date startDate, Date endDate) {
            try {
                String fileName = "releve_compte_" + accountNumber + ".pdf";
                PdfWriter writer = new PdfWriter(new FileOutputStream(fileName));
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph("Relevé de compte bancaire").setFontSize(18).setBold());
                document.add(new Paragraph("Compte : " + accountNumber));
                document.add(new Paragraph("Période : " + new SimpleDateFormat("dd/MM/yyyy").format(startDate) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(endDate)));

                // Récupérer les transactions de la base de données
                Table table = createTransactionsTable(accountNumber, startDate, endDate);
                document.add(table);

                document.close();
                System.out.println("Relevé généré avec succès : " + fileName);
            } catch (FileNotFoundException e) {
                System.err.println("Erreur : Fichier PDF non trouvé.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("Erreur SQL : " + e.getMessage());
                e.printStackTrace();
            }
        }

        private static Table createTransactionsTable(long accountNumber, Date startDate, Date endDate) throws SQLException {
            Table table = new Table(new float[]{4, 4, 4, 4}); // 4 colonnes


            // En-têtes de colonnes
            table.addHeaderCell("Date");
            table.addHeaderCell("Type");
            table.addHeaderCell("Montant");
            table.addHeaderCell("Description");

            // Récupérer les transactions de la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/votre_base_de_données", "votre_utilisateur", "votre_mot_de_passe");
            PreparedStatement statement = connection.prepareStatement("SELECT date_op, type_op, mnt_op, description FROM transactions WHERE num_c_em = ? AND date_op BETWEEN ? AND ?");
            statement.setLong(1, accountNumber);
            statement.setDate(2, new java.sql.Date(startDate.getTime()));
            statement.setDate(3, new java.sql.Date(endDate.getTime()));
            ResultSet resultSet = statement.executeQuery();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            while (resultSet.next()) {
                table.addCell(dateFormat.format(resultSet.getDate("date_op")));
                table.addCell(resultSet.getInt("type_op") == 1 ? "Dépôt" : resultSet.getInt("type_op") == 2 ? "Retrait" : "Virement");
                table.addCell(String.valueOf(resultSet.getDouble("mnt_op")));
                table.addCell(resultSet.getString("description"));
            }

            connection.close();
            return table;
        }
    }
}
