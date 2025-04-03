package view;

import controller.AuthController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.User;
import app.MainApp;

import java.util.List;

public class AdminDashboardView {
    private BorderPane layout;

    public AdminDashboardView(User admin, MainApp mainApp) {
        Label heading = new Label("👑 Admin Panel - " + admin.getEmail());
        heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // 🔹 Table for report
        TableView<User> userTable = new TableView<>();
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRole()));

        TableColumn<User, String> balanceCol = new TableColumn<>("Balance");
        balanceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f", data.getValue().getBalance())));

        userTable.getColumns().addAll(emailCol, roleCol, balanceCol);

        Button refreshBtn = styledButton("🔁 Refresh Users");
        refreshBtn.setOnAction(e -> {
            List<User> users = AuthController.getAllUsers();
            userTable.setItems(FXCollections.observableArrayList(users));
        });

        // 🔹 Add Employee section
        TextField emailField = new TextField();
        emailField.setPromptText("Employee Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button addEmpBtn = styledButton("➕ Add Employee");
        addEmpBtn.setOnAction(e -> {
            boolean created = AuthController.registerUser(emailField.getText(), passwordField.getText(), "employee", 0);
            String msg = created ? "✅ Employee added!" : "⚠️ Account exists.";
            new Alert(Alert.AlertType.INFORMATION, msg).show();
        });

        VBox addForm = new VBox(10, new Label("Add New Employee"), emailField, passwordField, addEmpBtn);
        addForm.setPadding(new Insets(10));
        addForm.setStyle("-fx-border-color: #bbb; -fx-border-radius: 8; -fx-background-color: #ffffff;");
        addForm.setAlignment(Pos.CENTER);

        // 🔹 Logout
        Button logoutBtn = styledButton("🚪 Logout");
        logoutBtn.setOnAction(e -> mainApp.showLoginScreen());

        VBox leftBox = new VBox(15, heading, refreshBtn, userTable, logoutBtn);
        leftBox.setPadding(new Insets(20));
        leftBox.setAlignment(Pos.TOP_CENTER);

        layout = new BorderPane();
        layout.setLeft(leftBox);
        layout.setRight(addForm);
        layout.setStyle("-fx-background-color: #f0f4f8;");
    }

    private Button styledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-font-size: 14px;
            -fx-background-color: #007acc;
            -fx-text-fill: white;
            -fx-background-radius: 6;
            -fx-padding: 8 20 8 20;
        """);
        return btn;
    }

    public Pane getView() {
        return layout;
    }
}
