package com.tixgo.desktop.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tixgo.desktop.Config;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BuktiPemesananView extends VBox {
    
    // UI Components
    private Label namaLabel, emailLabel, keretaLabel, ruteLabel, waktuLabel;
    private Label kursiLabel, kelasLabel, totalLabel, statusLabel, pembayaranLabel;
    
    private int bookingId;

    public BuktiPemesananView(Runnable onShowTicket) {
        this(onShowTicket, -1);
    }
    
    public BuktiPemesananView(Runnable onShowTicket, int bookingId) {
        this.bookingId = bookingId;
        setupView();
        createContent(onShowTicket);
        
        if (bookingId > 0) {
            loadFromDatabase(bookingId);
        }
    }
    
    // ==================== SETUP ====================
    
    private void setupView() {
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #4faad6;");
        this.setPadding(new Insets(40));
    }
    
    private void createContent(Runnable onShowTicket) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(480);
        card.setPadding(new Insets(40, 50, 40, 50));
        card.setStyle(
            "-fx-background-color: white; -fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 10);"
        );
        
        card.getChildren().addAll(
            createHeader(),
            createDataSection(),
            createButton(onShowTicket)
        );
        
        this.getChildren().add(card);
    }
    
    // ==================== HEADER ====================
    
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        
        Label icon = new Label("🎉");
        icon.setStyle("-fx-font-size: 48px;");
        
        Label title = new Label("Pemesanan Berhasil");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        
        Label subtitle = new Label("Terima kasih telah menggunakan TIX-GO");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
        
        header.getChildren().addAll(icon, title, subtitle);
        return header;
    }
    
    // ==================== DATA SECTION ====================
    
    private VBox createDataSection() {
        VBox section = new VBox(8);
        section.setAlignment(Pos.CENTER_LEFT);
        section.setPadding(new Insets(20, 0, 20, 0));
        
        // Initialize labels
        namaLabel = createLabel();
        emailLabel = createLabel();
        keretaLabel = createLabel();
        ruteLabel = createLabel();
        waktuLabel = createLabel();
        kursiLabel = createLabel();
        kelasLabel = createLabel();
        totalLabel = createLabel();
        statusLabel = createLabel("AKTIF");
        pembayaranLabel = createLabel();
        
        // Create rows
        section.getChildren().addAll(
            createRow("Nama", namaLabel),
            createRow("Email", emailLabel),
            createRow("Kereta", keretaLabel),
            createRow("Rute", ruteLabel),
            createRow("Waktu", waktuLabel),
            createRow("Kursi", kursiLabel),
            createRow("Kelas", kelasLabel),
            createRow("Total", totalLabel),
            createRow("Status", statusLabel),
            createRow("Pembayaran", pembayaranLabel)
        );
        
        return section;
    }
    
    private HBox createRow(String label, Label value) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 14px; -fx-text-fill: #333; -fx-min-width: 100;");
        
        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        row.getChildren().addAll(lbl, spacer, value);
        return row;
    }
    
    private Label createLabel() {
        return createLabel("-");
    }
    
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        return label;
    }
    
    // ==================== BUTTON ====================
    
    private Button createButton(Runnable onClick) {
        Button btn = new Button("Tampilkan Tiket");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPadding(new Insets(12, 0, 12, 0));
        btn.setStyle(
            "-fx-background-color: #2b8cc4; -fx-text-fill: white;" +
            "-fx-font-weight: bold; -fx-font-size: 14px;" +
            "-fx-background-radius: 8; -fx-cursor: hand;"
        );
        btn.setOnAction(e -> onClick.run());
        return btn;
    }
    
    // ==================== DATABASE ====================
    
    private void loadFromDatabase(int id) {
        try (Connection conn = Config.connect()) {
            String sql = "SELECT p.nama, p.email, p.kereta, p.metode_bayar, " +
                        "k.rute, k.waktu_keberangkatan, k.kelas, k.harga, b.kursi " +
                        "FROM pemesanan p " +
                        "LEFT JOIN kereta k ON p.kereta = k.nama_kereta " +
                        "LEFT JOIN booking b ON b.id = ? " +
                        "WHERE p.id = ?";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setInt(2, id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                setBookingData(
                    rs.getString("nama"),
                    rs.getString("email"),
                    rs.getString("kereta"),
                    rs.getString("rute"),
                    rs.getString("waktu_keberangkatan"),
                    rs.getString("kursi"),
                    rs.getString("kelas"),
                    rs.getDouble("harga"),
                    rs.getString("metode_bayar")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // ==================== PUBLIC SETTERS ====================
    
    public void setBookingData(String nama, String email, String kereta, String rute,
                              String waktu, String kursi, String kelas,
                              double total, String pembayaran) {
        namaLabel.setText(nama != null ? nama : "-");
        emailLabel.setText(email != null ? email : "-");
        keretaLabel.setText(kereta != null ? kereta : "-");
        ruteLabel.setText(rute != null ? rute : "-");
        waktuLabel.setText(waktu != null ? waktu : "-");
        kursiLabel.setText(kursi != null ? kursi : "-");
        kelasLabel.setText(kelas != null ? kelas : "-");
        totalLabel.setText(String.format("Rp %,.0f", total));
        pembayaranLabel.setText(pembayaran != null ? pembayaran : "-");
    }
    
    // BookingData class untuk compatibility
    public static class BookingData {
        private String nama, email, kereta, rute, waktu, kursi, kelas, pembayaran;
        private double total;
        
        public BookingData() {}
        
        public BookingData(String nama, String email, String kereta, String rute,
                          String waktu, String kursi, String kelas,
                          double total, String pembayaran) {
            this.nama = nama;
            this.email = email;
            this.kereta = kereta;
            this.rute = rute;
            this.waktu = waktu;
            this.kursi = kursi;
            this.kelas = kelas;
            this.total = total;
            this.pembayaran = pembayaran;
        }
        
        // Getters
        public String getNama() { return nama; }
        public String getEmail() { return email; }
        public String getKereta() { return kereta; }
        public String getRute() { return rute; }
        public String getWaktu() { return waktu; }
        public String getKursi() { return kursi; }
        public String getKelas() { return kelas; }
        public double getTotal() { return total; }
        public String getPembayaran() { return pembayaran; }
        
        // Setters
        public void setNama(String nama) { this.nama = nama; }
        public void setEmail(String email) { this.email = email; }
        public void setKereta(String kereta) { this.kereta = kereta; }
        public void setRute(String rute) { this.rute = rute; }
        public void setWaktu(String waktu) { this.waktu = waktu; }
        public void setKursi(String kursi) { this.kursi = kursi; }
        public void setKelas(String kelas) { this.kelas = kelas; }
        public void setTotal(double total) { this.total = total; }
        public void setPembayaran(String pembayaran) { this.pembayaran = pembayaran; }
    }
}