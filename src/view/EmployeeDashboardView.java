package view;

import controller.AuthController;
import controller.TransactionController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.User;
import app.MainApp;

public class EmployeeDashboardView {
    private BorderPane layout;

    public EmployeeDashboardView(User user, MainApp mainApp) {
        Label heading = new Label("💼 Employee Dashboard - " + user.getEmail());
        heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button assistBtn = styledButton("🧑‍💼 Assist Customer");
        Button logoutBtn = styledButton("🚪 Logout");

        assistBtn.setOnAction(e -> openAssistCustomerWindow());
        logoutBtn.setOnAction(e -> mainApp.showLoginScreen());

        VBox controls = new VBox(15, heading, assistBtn, logoutBtn);
        controls.setPadding(new Insets(30));
        controls.setAlignment(Pos.TOP_CENTER);
        controls.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-radius: 10; -fx-background-radius: 10;");

        layout = new BorderPane();
        layout.setCenter(controls);
        layout.setStyle("-fx-background-color: #f0f4f8;");
    }

    private void openAssistCustomerWindow() {
        Stage stage = new Stage();
        stage.setTitle("Assist Customer");

        Label emailLabel = new Label("Enter Customer Email:");
        TextField emailField = new TextField();
        Button fetchBtn = new Button("Fetch Customer");

        Label resultLabel = new Label();
        Label balanceLabel = new Label();

        Button depositBtn = new Button("Deposit");
        Button withdrawBtn = new Button("Withdraw");
        Button resetPassBtn = new Button("🔒 Reset Password");
        Button historyBtn = new Button("📜 View History");
        Button closeAccBtn = new Button("❌ Close Account");
        Button openAccBtn = new Button("✅ Open Account");

        // Initially disabled
        depositBtn.setDisable(true);
        withdrawBtn.setDisable(true);
        resetPassBtn.setDisable(true);
        historyBtn.setDisable(true);
        closeAccBtn.setDisable(true);
        openAccBtn.setDisable(true);

        fetchBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            User customer = AuthController.getUserByEmail(email);

            if (customer != null && customer.getRole().equalsIgnoreCase("customer")) {
                resultLabel.setText("👤 " + customer.getEmail());
                balanceLabel.setText("💰 Balance: $" + String.format("%.2f", customer.getBalance()));

                depositBtn.setDisable(false);
                withdrawBtn.setDisable(false);
                resetPassBtn.setDisable(false);
                historyBtn.setDisable(false);
                closeAccBtn.setDisable(false);
                openAccBtn.setDisable(false);

                depositBtn.setOnAction(ev -> handleEmployeeTransaction(customer, "Deposit", balanceLabel));
                withdrawBtn.setOnAction(ev -> handleEmployeeTransaction(customer, "Withdraw", balanceLabel));

                resetPassBtn.setOnAction(ev -> {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Reset Password");
                    dialog.setHeaderText("New password for " + customer.getEmail());
                    dialog.setContentText("Enter new password:");

                    dialog.showAndWait().ifPresent(newPass -> {
                        if (!newPass.isEmpty()) {
                            AuthController.updatePassword(customer.getEmail(), newPass);
                            showAlert("Password updated.");
                        }
                    });
                });

                historyBtn.setOnAction(ev -> {
                    var list = TransactionController.getAllForUser(customer.getEmail());
                    if (list.isEmpty()) {
                        showAlert("No transactions found.");
                        return;
                    }

                    StringBuilder sb = new StringBuilder("Transactions for " + customer.getEmail() + ":\n\n");
                    for (var t : list) {
                        sb.append(t.getType()).append(" $")
                                .append(t.getAmount()).append(" on ")
                                .append(t.getTimestamp()).append("\n");
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, sb.toString());
                    alert.setTitle("Transaction History");
                    alert.setHeaderText(null);
                    alert.show();
                });

                closeAccBtn.setOnAction(ev -> {
                    AuthController.updateBalance(customer.getEmail(), 0.0);
                    balanceLabel.setText("💰 Balance: $0.00");
                    showAlert("Account closed (balance set to $0).");
                });

                openAccBtn.setOnAction(ev -> {
                    AuthController.updateBalance(customer.getEmail(), 100.0);
                    balanceLabel.setText("💰 Balance: $100.00");
                    showAlert("Account opened (balance set to $100).");
                });

            } else {
                resultLabel.setText("❌ Customer not found.");
                balanceLabel.setText("");
                depositBtn.setDisable(true);
                withdrawBtn.setDisable(true);
                resetPassBtn.setDisable(true);
                historyBtn.setDisable(true);
                closeAccBtn.setDisable(true);
                openAccBtn.setDisable(true);
            }
        });

        VBox layout = new VBox(10,
                emailLabel, emailField, fetchBtn,
                resultLabel, balanceLabel,
                depositBtn, withdrawBtn,
                resetPassBtn, historyBtn,
                closeAccBtn, openAccBtn
        );
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER_LEFT);

        Scene scene = new Scene(layout, 400, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void handleEmployeeTransaction(User customer, String type, Label balanceLabel) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(type + " for Customer");
        dialog.setHeaderText(type + " Amount:");
        dialog.setContentText("Amount:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                double amount = Double.parseDouble(input);
                if (amount <= 0) throw new NumberFormatException();

                if (type.equals("Withdraw") && amount > customer.getBalance()) {
                    showAlert("Insufficient balance.");
                    return;
                }

                if (type.equals("Withdraw")) {
                    customer.setBalance(customer.getBalance() - amount);
                } else {
                    customer.setBalance(customer.getBalance() + amount);
                }

                AuthController.updateBalance(customer.getEmail(), customer.getBalance());
                TransactionController.record(customer.getEmail(), amount, type + " by Employee");

                balanceLabel.setText("💰 Balance: $" + String.format("%.2f", customer.getBalance()));
                showAlert("Transaction successful.");

            } catch (NumberFormatException e) {
                showAlert("Please enter a valid amount.");
            }
        });
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setHeaderText(null);
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
