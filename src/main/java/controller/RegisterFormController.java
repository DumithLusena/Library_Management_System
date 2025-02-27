package controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import dto.AdminDTO;
import exceptions.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.custom.AdminService;
import service.util.ServiceFactory;
import service.util.ServiceTypes;
import java.io.IOException;

public class RegisterFormController {

    @FXML
    private AnchorPane loadRegisterFormContent;

    @FXML
    private JFXPasswordField txtConfirmPassword;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private JFXCheckBox cbShowConfirmPassword;

    @FXML
    private JFXCheckBox cbShowEnterPassword;

    private final AdminService adminService = (AdminService) ServiceFactory.getInstance().getService(ServiceTypes.ADMIN_SERVICE);


    @FXML
    void btnSignUpOnAction(ActionEvent event) throws IOException {
        AdminDTO adminDTO = collectData();
        if (adminDTO==null){
            return;
        }
        try {
            boolean isSave = adminService.add(adminDTO);
            if (isSave){
                Stage window =(Stage) loadRegisterFormContent.getScene().getWindow();
                window.close();

                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login_form.fxml"))));
                stage.show();

            }
        } catch (ServiceException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private AdminDTO collectData(){
        String userName = txtUserName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Fill All Fields").show();
            return null;
        }
            if (!password.equals(confirmPassword)) {
                new Alert(Alert.AlertType.ERROR, "Password do not same").show();
                return null;
            }

        return new AdminDTO(userName,email,password);
    }

    @FXML
    void changeVisibilityEnterPasswordOnAction(ActionEvent event) {
        if (cbShowEnterPassword.isSelected()) {
            txtPassword.setPromptText(txtPassword.getText());
            txtPassword.setText("");
        } else {
            txtPassword.setText(txtPassword.getPromptText());
            txtPassword.setPromptText("");
        }
    }

    @FXML
    void changeVisibilityConfirmPasswordOnAction(ActionEvent event) {
        if (cbShowConfirmPassword.isSelected()) {
            txtConfirmPassword.setPromptText(txtConfirmPassword.getText());
            txtConfirmPassword.setText("");
        } else {
            txtConfirmPassword.setText(txtConfirmPassword.getPromptText());
            txtConfirmPassword.setPromptText("");
        }
    }

    @FXML
    void backToLoginOnAction(ActionEvent event) throws IOException {
        Stage window =(Stage) loadRegisterFormContent.getScene().getWindow();
        window.close();

        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login_form.fxml"))));
        stage.show();
    }
}
