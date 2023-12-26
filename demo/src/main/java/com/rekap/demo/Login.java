package com.rekap.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class Login {

    @FXML
    private CheckBox ShowPassword;
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;

    @FXML
    void initialize() {
        // Mengatur listener untuk mengubah echoChar pada perubahan status CheckBox
        ShowPassword.selectedProperty().addListener((observable, oldValue, newValue) -> {
            // Menampilkan atau menyembunyikan password berdasarkan status CheckBox
            if (newValue) {
                password.setPromptText(null);  // Menghapus prompt text
                password.setText(password.getText());  // Menetapkan teks untuk memicu tampilan
            } else {
                password.setPromptText("Password");  // Menetapkan prompt text untuk masking
                password.setText(password.getText());  // Menetapkan teks untuk memicu tampilan
            }
        });
    }

    @FXML
    void login(ActionEvent event) {
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();

        // Mengecek login untuk admin
        if (isLoginValidForAdmin(enteredUsername, enteredPassword)) {
            openAdminDashboard(event);
        }
        // Mengecek login untuk siswa
        else if (isLoginValidForSiswa(enteredUsername, enteredPassword)) {
            openSiswaDashboard(enteredUsername, event);
        } else {
            // Tampilkan pesan kesalahan jika login tidak berhasil
            showLoginErrorAlert();
        }
    }

    private boolean isLoginValidForAdmin(String username, String password) {
        // Admin statis, gantilah dengan informasi login admin yang sesuai
        return username.equals("admin") && password.equals("admin123");
    }

    private boolean isLoginValidForSiswa(String username, String password) {
        // Implementasi sederhana untuk koneksi ke database
        String url = "jdbc:mysql://localhost:3306/rekapnilai"; // Ganti dengan URL database Anda
        String user = "root"; // Ganti dengan username database Anda
        String pass = ""; // Ganti dengan password database Anda

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            // Query untuk memeriksa keberadaan data siswa di tabel akun
            String query = "SELECT * FROM akun WHERE NIS = ? AND Password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Data siswa ditemukan, login sukses
                        return true;
                    } else {
                        // Data siswa tidak ditemukan, tampilkan pop-up data tidak ditemukan
                        showLoginErrorAlert();
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void openAdminDashboard(ActionEvent event) {
        try {
            // Kode untuk membuka dashboard admin
            FXMLLoader fxmlLoader = new FXMLLoader(RekapNilaiApp.class.getResource("admin.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);

            // Menampilkan scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSiswaDashboard(String username, ActionEvent event) {
        try {
            // Kode untuk membuka dashboard siswa dan mengirimkan data siswa
            FXMLLoader fxmlLoader = new FXMLLoader(RekapNilaiApp.class.getResource("siswa.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);

            // Mengambil controller dari fxmlLoader
            Siswa siswaController = fxmlLoader.getController();

            // Menampilkan scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

            // Panggil metode untuk menampilkan data nilai siswa
            siswaController.showStudentScores(username);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    // Metode untuk menampilkan pop-up kesalahan login
    private boolean loginErrorShown = false;

    private void showLoginErrorAlert() {
        if (!loginErrorShown) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Username atau Password Salah. Silakan Login kembali");
            alert.showAndWait();

            loginErrorShown = true;
        }
    }
}
