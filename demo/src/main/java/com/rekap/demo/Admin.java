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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Admin {
    @FXML
    private TextField field_Indonesia;
    @FXML
    private TextField field_Inggris;
    @FXML
    private TextField field_IPA;
    @FXML
    private TextField field_MTK;
    @FXML
    private TextField field_Nama;
    @FXML
    private TextField field_NIS;
    @FXML
    private TableColumn<?, ?> dateContent;
    @FXML
    private TableColumn<?, ?> NIS;
    @FXML
    private TableColumn<?, ?> Nama;
    @FXML
    private TableColumn<?, ?> Bahasa_Indonesia;
    @FXML
    private TableColumn<?, ?> Bahasa_Inggris;
    @FXML
    private TableColumn<?, ?> Ilmu_Pengetahuan_Alam;
    @FXML
    private TableColumn<?, ?> Matematika;
    @FXML
    private TableView<ObservableList<String>> tableView;


    @FXML
    void clear(ActionEvent event) {
        field_NIS.clear();
        field_Nama.clear();
        field_Indonesia.clear();
        field_Inggris.clear();
        field_IPA.clear();
        field_MTK.clear();
    }

    @FXML
    void delete(ActionEvent event) {
        try {
            // Mendapatkan data yang dipilih dari tabel
            ObservableList<ObservableList<String>> selectedData = tableView.getSelectionModel().getSelectedItems();

            if (!selectedData.isEmpty()) {
                // Mendapatkan nilai NIS dari data yang dipilih (karena NIS adalah primary key)
                String selectedNIS = selectedData.get(0).get(0);

                try (Connection connection = DatabaseConnection.getConnection();
                     PreparedStatement statement = connection.prepareStatement("DELETE FROM siswa WHERE NIS = ?")) {
                    // Setel nilai parameter NIS
                    statement.setString(1, selectedNIS);

                    // Jalankan pernyataan
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Menampilkan Alert jika penghapusan berhasil
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Sukses");
                        alert.setHeaderText(null);
                        alert.setContentText("Data berhasil dihapus!");
                        alert.showAndWait();

                        // Refresh tabel setelah penghapusan
                        refreshTable();
                    }
                }

            } else {
                // Menampilkan Alert jika tidak ada data yang dipilih
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Peringatan");
                alert.setHeaderText(null);
                alert.setContentText("Pilih data yang ingin dihapus!");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void edit(ActionEvent event) {
        // Mendapatkan baris yang dipilih dari tabel
        ObservableList<String> selectedRow = tableView.getSelectionModel().getSelectedItem();

        // Memeriksa apakah ada baris yang dipilih
        if (selectedRow != null) {
            // Menempatkan data dari baris yang dipilih ke dalam TextField
            field_NIS.setText(selectedRow.get(0));
            field_Nama.setText(selectedRow.get(1));
            field_Indonesia.setText(selectedRow.get(2));
            field_Inggris.setText(selectedRow.get(3));
            field_IPA.setText(selectedRow.get(4));
            field_MTK.setText(selectedRow.get(5));

            // Menonaktifkan pengeditan kolom NIS
            field_NIS.setDisable(true);

        } else {
            // Menampilkan Alert jika tidak ada baris yang dipilih
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Pilih baris yang akan diedit!");
            alert.showAndWait();
        }
    }

    
    @FXML
    void input(ActionEvent event) {
        // Memeriksa apakah data yang dimasukkan sudah ada dalam tabel (berdasarkan NIS)
        if (isDataValid()) {
            boolean isUpdate = isDataExists(field_NIS.getText());

            try (Connection connection = DatabaseConnection.getConnection()) {
                if (isUpdate) {
                    // Jika NIS sudah ada, lakukan operasi pembaruan (UPDATE)
                    try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE siswa SET Nama=?, Indonesia=?, Inggris=?, IPA=?, Matematika=?, Note=NOW() WHERE NIS=?")) {
                        updateStatement.setString(1, field_Nama.getText());
                        updateStatement.setString(2, field_Indonesia.getText());
                        updateStatement.setString(3, field_Inggris.getText());
                        updateStatement.setString(4, field_IPA.getText());
                        updateStatement.setString(5, field_MTK.getText());
                        updateStatement.setString(6, field_NIS.getText());
                        updateStatement.executeUpdate();
                    }

                } else {
                    // Jika NIS belum ada, lakukan operasi penyisipan baru (INSERT)
                    try (PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO siswa (NIS, Nama, Indonesia, Inggris, IPA, Matematika, Note) VALUES (?, ?, ?, ?, ?, ?, NOW())")) {
                        insertStatement.setString(1, field_NIS.getText());
                        insertStatement.setString(2, field_Nama.getText());
                        insertStatement.setString(3, field_Indonesia.getText());
                        insertStatement.setString(4, field_Inggris.getText());
                        insertStatement.setString(5, field_IPA.getText());
                        insertStatement.setString(6, field_MTK.getText());
                        insertStatement.executeUpdate();
                    }
                }

                // Perbarui tabel setelah penyisipan atau pembaruan
                // Panggil suatu method untuk memperbarui data tabel
                refreshTable();

                // Menampilkan Alert jika penyisipan atau pembaruan berhasil
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukses");
                alert.setHeaderText(null);
                alert.setContentText(isUpdate ? "Data berhasil diperbarui!" : "Data berhasil disisipkan!");
                alert.showAndWait();

                // Mengaktifkan kembali pengeditan kolom NIS setelah operasi selesai
                field_NIS.setDisable(false);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            // Menampilkan Alert jika ada data yang kosong
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Semua kolom harus diisi!");
            alert.showAndWait();
        }
    }



    // Method untuk memeriksa apakah data sudah ada dalam tabel berdasarkan NIS
    private boolean isDataExists(String nis) {
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM siswa WHERE NIS=?")) {statement.setString(1, nis);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); //Mengembalikan true jika data sudah ada, false jika belum
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @FXML
    void refresh(ActionEvent event) {
        // Memanggil metode refreshTable untuk meng-update tabel
        refreshTable();
    }



    // Metode untuk memeriksa apakah ada data yang kosong
    private boolean isDataValid() {
        return !field_NIS.getText().isEmpty() && !field_Nama.getText().isEmpty() && !field_Indonesia.getText().isEmpty() && !field_Inggris.getText().isEmpty() && !field_IPA.getText().isEmpty() && !field_MTK.getText().isEmpty();
    }


    // Metode untuk meng-update tabel
    private void loadDataFromDatabase() {
        try (Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM siswa")) {
            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

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


    private void refreshTable() {
        // Membersihkan kolom sebelum menambahkan kolom baru
        tableView.getColumns().clear();

        // Memanggil metode untuk memuat data dari database
        loadDataFromDatabase();
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            // Kode untuk kembali ke menu login
            FXMLLoader fxmlLoader = new FXMLLoader(RekapNilaiApp.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 500);

            // Menampilkan scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Menampilkan informasi apabila berhasil logout
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukses !!!");
        alert.setHeaderText(null);
        alert.setContentText("Anda telah logout");
        alert.showAndWait();
    }
}