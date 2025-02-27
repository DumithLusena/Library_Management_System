package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class DashboardFormController implements Initializable {

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblUserName;

    @FXML
    private AnchorPane loadDashboardFormContent;

    @FXML
    void btnBorrowBookRecordsOnAction(ActionEvent event) {
        loadUI("borrow_book_form.fxml");
    }

    @FXML
    void btnManageAuthorsAndPublishersOnAction(ActionEvent event) {
        loadUI("manage_authors_and_publishers_form.fxml");
    }

    @FXML
    void btnManageBooksOnAction(ActionEvent event) {
        loadUI("manage_book_form.fxml");
    }

    @FXML
    void btnManageMembersOnAction(ActionEvent event) {
        loadUI("manage_member_form.fxml");
    }

    @FXML
    void btnReturnBookRecordsOnAction(ActionEvent event) {
        loadUI("return_book_form.fxml");
    }

    @FXML
    void btnHomePageOnAction(ActionEvent event) {
        loadUI("homepage_form.fxml");
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {
        Stage window =(Stage) loadDashboardFormContent.getScene().getWindow();
        window.close();

        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login_form.fxml"))));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDateAndTime();
    }

    public void loadUI(String uiName){
        loadDashboardFormContent.getChildren().clear();

        try {
            Parent load = FXMLLoader.load(getClass().getResource("/view/sub/"+uiName));
            loadDashboardFormContent.getChildren().add(load);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDateAndTime(){
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e ->
                lblTime.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
        ));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    public void setUserName(String username) {
        lblUserName.setText(username);
    }

}
