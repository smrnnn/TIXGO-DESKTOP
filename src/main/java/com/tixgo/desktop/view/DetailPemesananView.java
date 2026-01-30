package com.tixgo.desktop.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.tixgo.desktop.Config;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DetailPemesananView extends ScrollPane {
    
    // UI Components
    private Label trainNameLabel, routeLabel, timeLabel, seatLabel;
    private Label passengerNameLabel, passengerEmailLabel;
    private TextField contactNameField, contactEmailField, contactPhoneField;
    private ToggleGroup paymentGroup;
    private Stage stage;
    
    // Data
    private String kelas = "Bisnis";
    private double harga = 97500;

    public DetailPemesananView(Runnable onBack, Stage stage) {
        this.stage = stage;
        setupScrollPane();
        this.setContent(createMainContent(onBack));
    }
    
    // ==================== SETUP ====================
    
    private void setupScrollPane() {
        this.setFitToWidth(true);
        this.setFitToHeight(true);
        this.setStyle("-fx-background: linear-gradient(to bottom, #4AADCE, #5BB8D9);");
    }
    
    private VBox createMainContent(Runnable onBack) {
        VBox main = new VBox(20);
        main.setPadding(new Insets(40));
        main.setAlignment(Pos.TOP_CENTER);
        main.setStyle("-fx-background-color: linear-gradient(to bottom, #4AADCE, #5BB8D9);");
        
        VBox content = new VBox(20);
        content.setMaxWidth(1200);
        content.setAlignment(Pos.TOP_CENTER);
        
        content.getChildren().addAll(
            createDataKeretaCard(),
            createKursiCard(),
            createPenumpangCard(),
            createKontakCard(),
            createPaymentCard(),
            createActionButtons(onBack)
        );
        
        // ✅ FIXED: Header hanya ditambahkan sekali
        main.getChildren().addAll(createHeader(), content);
        return main;
    }
    
    // ==================== HEADER ====================
    
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20, 0, 20, 0));
        
        Label title = new Label("Detail Pemesanan Tiket");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Label subtitle = new Label("Periksa kembali data perjalanan dan penumpang Anda");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(255,255,255,0.9);");
        
        header.getChildren().addAll(title, subtitle);
        return header;
    }
    
    // ==================== CARDS ====================
    
    private VBox createCard(String icon, String title) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.setStyle(
            "-fx-background-color: rgba(255,255,255,0.25);" +
            "-fx-background-radius: 15; -fx-border-color: rgba(255,255,255,0.3);" +
            "-fx-border-width: 1; -fx-border-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 24px;");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        header.getChildren().addAll(iconLabel, titleLabel);
        card.getChildren().add(header);
        return card;
    }
    
    private VBox createDataKeretaCard() {
        VBox card = createCard("🚂", "Data Kereta");
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        
        trainNameLabel = createValueLabel();
        routeLabel = createValueLabel();
        timeLabel = createValueLabel();
        
        grid.add(createColumn("Nama Kereta", trainNameLabel), 0, 0);
        grid.add(createColumn("Rute", routeLabel), 1, 0);
        grid.add(createColumn("Waktu", timeLabel), 2, 0);
        
        card.getChildren().add(grid);
        return card;
    }
    
    private VBox createKursiCard() {
        VBox card = createCard("💺", "Kursi Dipilih");
        seatLabel = new Label("-");
        seatLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
        card.getChildren().add(seatLabel);
        return card;
    }
    
    private VBox createPenumpangCard() {
        VBox card = createCard("👤", "Data Penumpang");
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        
        passengerNameLabel = createValueLabel();
        passengerEmailLabel = createValueLabel();
        
        grid.add(createColumn("Nama", passengerNameLabel), 0, 0);
        grid.add(createColumn("Email", passengerEmailLabel), 1, 0);
        
        card.getChildren().add(grid);
        return card;
    }
    
    private VBox createKontakCard() {
        VBox card = createCard("📞", "Kontak Pemesan");
        VBox form = new VBox(12);
        
        contactNameField = createTextField("Nama Pemesan");
        contactEmailField = createTextField("Email Pemesan");
        contactPhoneField = createTextField("No. HP Pemesan");
        
        form.getChildren().addAll(contactNameField, contactEmailField, contactPhoneField);
        card.getChildren().add(form);
        return card;
    }
    
    private VBox createPaymentCard() {
        VBox card = createCard("💳", "Metode Pembayaran");
        HBox container = new HBox(15);
        container.setAlignment(Pos.CENTER);
        
        paymentGroup = new ToggleGroup();
        
        container.getChildren().addAll(
            createPaymentOption("🏦", "Bank", false),
            createPaymentOption("🏪", "Supermarket", false),
            createPaymentOption("📱", "E-Wallet", true)
        );
        
        card.getChildren().add(container);
        return card;
    }
    
    // ==================== COMPONENTS ====================
    
    private VBox createColumn(String label, Label value) {
        VBox col = new VBox(5);
        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(255,255,255,0.8);");
        col.getChildren().addAll(lbl, value);
        return col;
    }
    
    private Label createValueLabel() {
        Label label = new Label("-");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: 600; -fx-text-fill: white;");
        return label;
    }
    
    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle(
            "-fx-background-color: white; -fx-background-radius: 8;" +
            "-fx-padding: 12 15; -fx-font-size: 14px;"
        );
        return field;
    }
    
    private VBox createPaymentOption(String icon, String text, boolean selected) {
        VBox option = new VBox(10);
        option.setAlignment(Pos.CENTER);
        option.setPadding(new Insets(20, 30, 20, 30));
        option.setStyle(
            "-fx-background-color: rgba(255,255,255,0.3);" +
            "-fx-background-radius: 12; -fx-cursor: hand;" +
            "-fx-min-width: 150;"
        );
        HBox.setHgrow(option, Priority.ALWAYS);
        
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 36px;");
        
        RadioButton radio = new RadioButton(text);
        radio.setToggleGroup(paymentGroup);
        radio.setSelected(selected);
        radio.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: 600;");
        
        option.getChildren().addAll(iconLabel, radio);
        option.setOnMouseClicked(e -> radio.setSelected(true));
        
        return option;
    }
    
    private HBox createActionButtons(Runnable onBack) {
        HBox actions = new HBox(15);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(20, 0, 0, 0));
        
        Button btnBack = createButton("Kembali", false);
        btnBack.setOnAction(e -> onBack.run());
        
        Button btnConfirm = createButton("Konfirmasi & Bayar", true);
        btnConfirm.setOnAction(e -> handleConfirm());
        
        actions.getChildren().addAll(btnBack, btnConfirm);
        return actions;
    }
    
    private Button createButton(String text, boolean primary) {
        Button btn = new Button(text);
        btn.setStyle(primary ? 
            "-fx-background-color: #27ae60; -fx-text-fill: white;" +
            "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 15 40;" +
            "-fx-background-radius: 8; -fx-cursor: hand;" :
            "-fx-background-color: rgba(255,255,255,0.3); -fx-text-fill: white;" +
            "-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 15 40;" +
            "-fx-background-radius: 8; -fx-cursor: hand; -fx-border-color: white;" +
            "-fx-border-width: 2; -fx-border-radius: 8;"
        );
        return btn;
    }
    
    // ==================== HANDLERS ====================
    
    private void handleConfirm() {
        // Validasi
        String nama = contactNameField.getText();
        String email = contactEmailField.getText();
        String hp = contactPhoneField.getText();
        
        if (nama.isEmpty() || email.isEmpty() || hp.isEmpty()) {
            showAlert("Error", "Mohon lengkapi semua data!");
            return;
        }
        
        RadioButton selected = (RadioButton) paymentGroup.getSelectedToggle();
        if (selected == null) {
            showAlert("Error", "Pilih metode pembayaran!");
            return;
        }
        
        String payment = selected.getText();
        String kereta = trainNameLabel.getText();
        
        // Simpan ke database
        int id = saveToDatabase(nama, email, hp, payment, kereta);
        
        // Navigasi
        navigateToBukti(id, nama, email, kereta, payment);
    }
    
    private int saveToDatabase(String nama, String email, String hp, String payment, String kereta) {
        try (Connection conn = Config.connect()) {
            String sql = "INSERT INTO pemesanan (nama, email, hp, metode_bayar, kereta) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, nama);
            stmt.setString(2, email);
            stmt.setString(3, hp);
            stmt.setString(4, payment);
            stmt.setString(5, kereta);
            
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
            
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    private void navigateToBukti(int id, String nama, String email, String kereta, String payment) {
        BuktiPemesananView view = new BuktiPemesananView(() -> showTicket(), id);
        
        view.setBookingData(
            nama, email, kereta,
            routeLabel.getText(),
            timeLabel.getText(),
            seatLabel.getText(),
            kelas, harga, payment
        );
        
        Scene scene = new Scene(view, 1200, 900);
        stage.setScene(scene);
        stage.setTitle("Bukti Pemesanan | TIX-GO");
    }
    
    private void showTicket() {
        showAlert("Info", "Fitur tiket segera hadir!");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // ==================== PUBLIC SETTERS ====================
    
    public void setDataKereta(String nama, String rute, String waktu) {
        if (trainNameLabel != null) trainNameLabel.setText(nama != null ? nama : "-");
        if (routeLabel != null) routeLabel.setText(rute != null ? rute : "-");
        if (timeLabel != null) timeLabel.setText(waktu != null ? waktu : "-");
    }
    
    public void setKursi(String kursi) {
        if (seatLabel != null) seatLabel.setText(kursi != null ? kursi : "-");
    }
    
    public void setDataPenumpang(String nama, String email) {
        if (passengerNameLabel != null) passengerNameLabel.setText(nama != null ? nama : "-");
        if (passengerEmailLabel != null) passengerEmailLabel.setText(email != null ? email : "-");
    }
    
    public void setKelasHarga(String kelas, double harga) {
        this.kelas = kelas;
        this.harga = harga;
    }
    
    public void setAllData(String kereta, String rute, String waktu, String kursi, String nama, String email) {
        setDataKereta(kereta, rute, waktu);
        setKursi(kursi);
        setDataPenumpang(nama, email);
    }
}