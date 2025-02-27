package controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import dto.AdminDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.custom.AdminService;
import service.util.ServiceFactory;
import service.util.ServiceTypes;
import java.io.IOException;
import java.net.URL;

public class LoginFormController {
    @FXML
    private JFXCheckBox cbShowPassword;

    @FXML
    private AnchorPane loadLoginFormContent;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXTextField txtEmail;


    private final AdminService adminService = (AdminService) ServiceFactory.getInstance().getService(ServiceTypes.ADMIN_SERVICE);


    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Login Failed, Fill All Fields").show();
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid email format").show();
            return;
        }

        AdminDTO adminDTO = null;
        try {
            adminDTO = adminService.login(email, password);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Login Failed, An unexpected error occurred").show();
            e.printStackTrace();
            return;
        }

        if (adminDTO == null) {
            new Alert(Alert.AlertType.ERROR, "Login Failed, Invalid Email or Password").show();
            return;
        }

        Stage window = (Stage) loadLoginFormContent.getScene().getWindow();
        window.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
        Parent root = loader.load();

        DashboardFormController dashboardController = loader.getController();
        dashboardController.setUserName(adminDTO.getUserName());

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void changeVisibilityOnAction(ActionEvent event) {
        if (cbShowPassword.isSelected()) {
            txtPassword.setPromptText(txtPassword.getText());
            txtPassword.setText("");
        } else {
            txtPassword.setText(txtPassword.getPromptText());
            txtPassword.setPromptText("");
        }
    }


    @FXML
    void hyperSignUpOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/register_form.fxml");

        assert resource != null;

        Parent load = FXMLLoader.load(resource);

        loadLoginFormContent.getChildren().clear();
        loadLoginFormContent.getChildren().add(load);

    }

}
