package view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.User;

public class DashboardView {
    private VBox layout;

    public DashboardView(User user) {
        Label welcome = new Label("Welcome to your dashboard, " + user.getEmail() + "!");
        Label roleLabel = new Label("Your role: " + user.getRole());
        Label balanceLabel = new Label("Your balance: $" + user.getBalance());
        layout = new VBox(10, welcome, roleLabel, balanceLabel);
        layout.setPadding(new Insets(20));
    }

    public VBox getView() {
        return layout;
    }
}
