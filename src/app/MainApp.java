package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.AuthController;
import model.DBUtil;
import model.User;
import view.*;

public class MainApp extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        DBUtil.initializeDatabase();
        AuthController.createDefaultAdmin();
        AuthController.createSampleUsers(); // optional test users

        showLoginScreen();
    }

    public void showLoginScreen() {
        LoginView loginView = new LoginView(this);
        Scene scene = new Scene(loginView.getView(), 400, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bank Login");
        primaryStage.show();
    }

    public void showDashboard(User user) {
        Scene scene;
        switch (user.getRole().toLowerCase()) {
            case "admin" -> scene = new Scene(new AdminDashboardView(user, this).getView(), 800, 600);
            case "employee" -> scene = new Scene(new EmployeeDashboardView(user, this).getView(), 800, 600);
            default -> scene = new Scene(new CustomerDashboardView(user, this).getView(), 800, 600);
        }
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dashboard - " + user.getRole());
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
