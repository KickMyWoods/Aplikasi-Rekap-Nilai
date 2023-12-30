package com.rekap.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class Siswa {
    @FXML
    private TableView<ObservableList<String>> tableView;

    private String currentNIS;

    public void showStudentScores(String nis) {
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM siswa WHERE NIS=?")) {
            statement.setString(1, nis);
            try (ResultSet resultSet = statement.executeQuery()) {
                ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

                int columnCount = resultSet.getMetaData().getColumnCount();
                double columnWidth = 100.0 / columnCount;

                // Menambahkan kolom dinamis sesuai dengan metadata kolom
                for (int i = 1; i <= columnCount; i++) {
                    final int j = i - 1;
                    TableColumn<ObservableList<String>, String> col = new TableColumn<>(resultSet.getMetaData().getColumnName(i));
                    col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j)));

                    // Menetapkan lebar relatif untuk setiap kolom
                    col.prefWidthProperty().bind(tableView.widthProperty().multiply(columnWidth));

                    tableView.getColumns().add(col);
                }

                // Memuat data dari ResultSet ke dalam ObservableList
                while (resultSet.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(resultSet.getString(i));
                    }
                    data.add(row);
                }

                // Menetapkan data ke dalam TableView
                tableView.setItems(data);

                // Mengatur agar kolom dapat diresize secara merata
                tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                tableView.setTableMenuButtonVisible(true);

                // Menyimpan NIS yang sedang digunakan
                currentNIS = nis;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logout(ActionEvent event) {
        // Menampilkan konfirmasi dialog sebelum logout
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Apakah anda yakin ingin logout?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Kode untuk kembali ke menu login
                FXMLLoader fxmlLoader = new FXMLLoader(RekapNilaiApp.class.getResource("login.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 800, 500);

                // Menampilkan scene
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

                // Menampilkan informasi apabila berhasil logout
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukses !!!");
                alert.setHeaderText(null);
                alert.setContentText("Anda telah logout");
                alert.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void refresh(ActionEvent event) {
        // Memanggil method untuk memuat data dari database
        refreshTable();
    }

    // Method untuk memuat data dari database ke dalam tabel
    private void refreshTable() {
        // Membersihkan kolom sebelum menambahkan kolom baru
        tableView.getColumns().clear();

        // Memanggil method untuk memuat data dari database berdasarkan NIS yang sedang digunakan
        loadDataFromDatabase(currentNIS);
    }

    // Method untuk load tabel
    private void loadDataFromDatabase(String nis) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM siswa WHERE NIS=?")) {
            statement.setString(1, nis);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

            // Membersihkan kolom sebelum menambahkan kolom baru
            tableView.getColumns().clear();

            // Menambahkan kolom dinamis sesuai dengan metadata kolom
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                final int j = i - 1;
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(resultSet.getMetaData().getColumnName(i));
                col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j)));
                tableView.getColumns().addAll(col);
            }

            // Memuat data dari ResultSet ke dalam ObservableList
            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    row.add(resultSet.getString(i));
                }
                data.add(row);
            }

            // Menetapkan data ke dalam TableView
            tableView.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}