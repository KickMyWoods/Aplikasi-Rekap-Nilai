package com.rekap.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Siswa {
    @FXML
    private TableView<ObservableList<String>> tableView;

    // Metode untuk menampilkan data nilai siswa berdasarkan NIS
    public void showStudentScores(String nis) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM siswa WHERE NIS=?")) {
            statement.setString(1, nis);
            try (ResultSet resultSet = statement.executeQuery()) {
                ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

                // Menambahkan kolom dinamis sesuai dengan metadata kolom
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    final int j = i - 1;
                    TableColumn<ObservableList<String>, String> col = new TableColumn<>(resultSet.getMetaData().getColumnName(i));
                    col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j)));
                    tableView.getColumns().add(col);
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

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    }
}