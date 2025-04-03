package view;

import controller.AuthController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.User;
import app.MainApp;
import javafx.stage.Stage;

public class LoginView {
    private VBox layout;
    private TextField emailField;
    private PasswordField passwordField;
    private MainApp mainApp;

    public LoginView(MainApp app) {
        this.mainApp = app;

        Label title = new Label("🔐 Bank Login");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label emailLabel = new Label("Email:");
        emailField = new TextField();
        emailField.setPromptText("Enter email");

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(e -> handleLogin());

        Button registerBtn = new Button("Register as New Customer");
        registerBtn.setOnAction(e -> {
            Stage stage = (Stage) layout.getScene().getWindow();
            RegisterView registerView = new RegisterView(stage);
            stage.setScene(registerView.getScene());
        });

        layout = new VBox(10, title, emailLabel, emailField, passwordLabel, passwordField, loginBtn, registerBtn);
        layout.setPadding(new Insets(20));
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Please fill in both fields.");
            return;
        }

        User user = AuthController.login(email, password);
        if (user != null) {
            mainApp.showDashboard(user);
        } else {
            showAlert("Invalid credentials. Please try again.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setHeaderText(null);
        alert.show();
    }

    public VBox getView() {
        return layout;
    }

    public Scene getViewScene(Stage stage) {
        return new Scene(getView(), 400, 250);
    }
}
