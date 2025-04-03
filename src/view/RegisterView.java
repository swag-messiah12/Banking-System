package view;

import controller.AuthController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

public class RegisterView {
    private VBox layout;
    private Stage stage;

    public RegisterView(Stage stage) {
        this.stage = stage;

        Label title = new Label("🔐 Create New Account");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button registerBtn = new Button("Register");
        registerBtn.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            if (email.isEmpty() || password.isEmpty()) {
                showAlert("Please fill in both fields.");
                return;
            }

            boolean created = AuthController.registerUser(email, password, "customer", 0);
            if (created) {
                showAlert("Account created! You can now login.");
                stage.setScene(new LoginView(new app.MainApp()).getViewScene(stage)); // hacky call back to login
            } else {
                showAlert("Account already exists.");
            }
        });

        layout = new VBox(10, title, emailField, passwordField, registerBtn);
        layout.setPadding(new Insets(20));
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setHeaderText(null);
        alert.show();
    }

    public Scene getScene() {
        return new Scene(layout, 400, 250);
    }
}
