package com.tixgo.desktop;

import com.tixgo.desktop.view.BuktiPemesananView;
import com.tixgo.desktop.view.CariJadwalView;
import com.tixgo.desktop.view.DashboardView;
import com.tixgo.desktop.view.DetailPemesananView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DesktopApplication extends Application {
    private StackPane root;
    private Stage stage;
    
    // Data booking sementara
    private BookingData bookingData = new BookingData();

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.root = new StackPane();
        
        // Setup window
        stage.setTitle("TIX-GO - Pemesanan Tiket Kereta");
        stage.setMinWidth(420);
        stage.setMinHeight(650);
        stage.setWidth(1000);
        stage.setHeight(750);
        stage.setScene(new Scene(root));
        
        // Show dashboard
        showDashboardView();
        stage.show();
    }

    // ==================== NAVIGASI ====================
    
    public void showDashboardView() {
        root.getChildren().setAll(new DashboardView(this::showJadwalView));
        stage.setTitle("TIX-GO - Dashboard");
    }

    public void showJadwalView() {
        root.getChildren().setAll(new CariJadwalView(this::showDetailView));
        stage.setTitle("TIX-GO - Cari Jadwal");
    }

    public void showDetailView() {
        DetailPemesananView view = new DetailPemesananView(this::showJadwalView, stage);
        
        // Set data jika ada
        if (bookingData.hasData()) {
            view.setAllData(
                bookingData.namaKereta,
                bookingData.rute,
                bookingData.waktu,
                bookingData.kursi,
                bookingData.namaPenumpang,
                bookingData.emailPenumpang
            );
            view.setKelasHarga(bookingData.kelas, bookingData.harga);
        }
        
        root.getChildren().setAll(view);
        stage.setTitle("TIX-GO - Detail Pemesanan");
    }

    public void showBuktiView(int bookingId) {
        BuktiPemesananView view = new BuktiPemesananView(this::showTicketView, bookingId);
        
        // Set data manual jika tidak pakai database
        if (bookingId < 0) {
            view.setBookingData(
                "Demo User",
                "demo@email.com",
                bookingData.getOrDefault("namaKereta", "Argo Parahyangan"),
                bookingData.getOrDefault("rute", "Jakarta - Bandung"),
                bookingData.getOrDefault("waktu", "07:00 - 09:30"),
                bookingData.getOrDefault("kursi", "A3"),
                bookingData.kelas,
                bookingData.harga,
                "E-Wallet"
            );
        }
        
        root.getChildren().setAll(view);
        stage.setTitle("TIX-GO - Bukti Pemesanan");
    }
    
    public void showTicketView() {
        showAlert("Info", "Fitur Tiket akan segera hadir!\nKembali ke Dashboard.");
        showDashboardView();
    }
    
    // ==================== HELPER ====================
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // ==================== DATA MANAGEMENT ====================
    
    public void setDataKereta(String nama, String rute, String waktu, String kelas, double harga) {
        bookingData.namaKereta = nama;
        bookingData.rute = rute;
        bookingData.waktu = waktu;
        bookingData.kelas = kelas;
        bookingData.harga = harga;
    }
    
    public void setKursi(String kursi) {
        bookingData.kursi = kursi;
    }
    
    public void setDataPenumpang(String nama, String email) {
        bookingData.namaPenumpang = nama;
        bookingData.emailPenumpang = email;
    }
    
    public void clearBookingData() {
        bookingData = new BookingData();
    }
    
    // ==================== INNER CLASS ====================
    
    private static class BookingData {
        String namaKereta = "";
        String rute = "";
        String waktu = "";
        String kursi = "";
        String namaPenumpang = "";
        String emailPenumpang = "";
        String kelas = "Bisnis";
        double harga = 97500;
        
        boolean hasData() {
            return !namaKereta.isEmpty();
        }
        
        String getOrDefault(String field, String defaultValue) {
            switch(field) {
                case "namaKereta": return namaKereta.isEmpty() ? defaultValue : namaKereta;
                case "rute": return rute.isEmpty() ? defaultValue : rute;
                case "waktu": return waktu.isEmpty() ? defaultValue : waktu;
                case "kursi": return kursi.isEmpty() ? defaultValue : kursi;
                default: return defaultValue;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}