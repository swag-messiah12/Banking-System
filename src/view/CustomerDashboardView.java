package view;

import controller.TransactionController;
import controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.User;
import app.MainApp;

public class CustomerDashboardView {
    private BorderPane layout;
    private User user;
    private Label balanceLabel;

    public CustomerDashboardView(User user, MainApp mainApp) {
        this.user = user;

        // Header
        Label heading = new Label("🏦 Welcome, " + user.getEmail());
        heading.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        balanceLabel = new Label("💰 Balance: $" + String.format("%.2f", user.getBalance()));
        balanceLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #333333;");

        // Buttons
        Button depositBtn = styledButton("➕ Deposit");
        Button withdrawBtn = styledButton("➖ Withdraw");
        Button historyBtn = styledButton("📜 History");
        Button logoutBtn = styledButton("🚪 Logout");

        depositBtn.setOnAction(e -> handleTransaction("Deposit"));
        withdrawBtn.setOnAction(e -> handleTransaction("Withdraw"));
        historyBtn.setOnAction(e -> showTransactionHistory());
        logoutBtn.setOnAction(e -> mainApp.showLoginScreen());

        VBox actions = new VBox(10,
                balanceLabel,
                depositBtn,
                withdrawBtn,
                historyBtn,
                logoutBtn
        );
        actions.setPadding(new Insets(20));
        actions.setAlignment(Pos.CENTER_LEFT);
        actions.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 10; -fx-background-radius: 10;");

        VBox centerBox = new VBox(20, heading, actions);
        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setPadding(new Insets(30));

        layout = new BorderPane();
        layout.setCenter(centerBox);
        layout.setStyle("-fx-background-color: #f0f4f8;");
    }

    private void handleTransaction(String type) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(type);
        dialog.setHeaderText(type + " Amount:");
        dialog.setContentText("Amount:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                double amount = Double.parseDouble(input);
                if (amount <= 0) throw new NumberFormatException();

                if (type.equals("Withdraw") && amount > user.getBalance()) {
                    showAlert("Insufficient balance.");
                    return;
                }

                if (type.equals("Withdraw")) {
                    user.setBalance(user.getBalance() - amount);
                } else {
                    user.setBalance(user.getBalance() + amount);
                }

                AuthController.updateBalance(user.getEmail(), user.getBalance());
                TransactionController.record(user.getEmail(), amount, type);

                balanceLabel.setText("💰 Balance: $" + String.format("%.2f", user.getBalance()));
                showAlert(type + " successful.");
            } catch (NumberFormatException e) {
                showAlert("Please enter a valid positive number.");
            }
        });
    }

    private void showTransactionHistory() {
        var list = TransactionController.getAllForUser(user.getEmail());
        StringBuilder sb = new StringBuilder("Transaction History:\n\n");
        for (var t : list) {
            sb.append(t.getType()).append(" $")
                    .append(t.getAmount()).append(" on ")
                    .append(t.getTimestamp()).append("\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, sb.toString());
        alert.setTitle("Transaction History");
        alert.setHeaderText(null);
        alert.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
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
