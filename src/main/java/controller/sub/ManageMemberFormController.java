package controller.sub;

import com.jfoenix.controls.JFXTextField;
import dto.MemberDTO;
import exceptions.custom.MemberException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import service.custom.impl.MemberServiceIMPL;
import service.util.ServiceFactory;
import service.util.ServiceTypes;
import tm.MemberTM;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManageMemberFormController {


    private final MemberServiceIMPL service = (MemberServiceIMPL)ServiceFactory.getInstance().getService(ServiceTypes.MEMBER_SERVICE);

    @FXML
    private TableView<MemberTM> tblMember;

    @FXML
    private TableColumn<MemberTM,String> colMemberId;

    @FXML
    private TableColumn<MemberTM,String> colMemberName;

    @FXML
    private TableColumn<MemberTM,String> colMemberAddress;

    @FXML
    private TableColumn<MemberTM,String> colMemberEmail;

    @FXML
    private TableColumn<MemberTM,String> colMemberContact;

    @FXML
    private JFXTextField txtMemberAddress;

    @FXML
    private JFXTextField txtMemberContact;

    @FXML
    private JFXTextField txtMemberEmail;

    @FXML
    private JFXTextField txtMemberId;

    @FXML
    private JFXTextField txtMemberName;

    public void initialize(){
        loadTableData();
        visualizeTable();
    }

    @FXML
    void memberIdOnAction(ActionEvent event) {
        Optional<MemberDTO> search = null;
        try {
            search = service.search(txtMemberId.getText());
        } catch (MemberException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            return;
        }
        if (search.isPresent()){
            setDataToFields(search.get());
        }else{
            new Alert(Alert.AlertType.ERROR,"Member Not Found!").show();
        }
    }

    @FXML
    void btnSaveCustomerOnAction(ActionEvent event) {
        MemberDTO memberDTO = collectData();
        System.out.println("up : "+memberDTO);
        try {
            boolean isSave = service.add(memberDTO);
            if (isSave){
                new Alert(Alert.AlertType.INFORMATION, "Member Saved Successfully!").show();
                loadTableData();
                clearFields();
            }else {
                new Alert(Alert.AlertType.ERROR, "Member Not Saved").show();
            }
        } catch (MemberException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }

    @FXML
    void btnDeleteCustomerOnAction(ActionEvent event) {
        String id = txtMemberId.getText();
        boolean isDelete = false;
        String errorMessage = "User Cancelled - Not Deleted";
        Alert areYouSure = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure!", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = areYouSure.showAndWait();
        if (buttonType.isPresent()){
            ButtonType pressButton = buttonType.get();
            if (pressButton.equals(ButtonType.YES)){
                try {
                    isDelete = service.delete(id);
                    if (!isDelete){
                        errorMessage = "User Not Found - Check ID";
                    }
                } catch (MemberException e) {
                    errorMessage = e.getMessage();
                }
            }
        }

        if (isDelete){
            new Alert(Alert.AlertType.INFORMATION, "Member Deleted Successfully!").show();
            loadTableData();
        }else{
            new Alert(Alert.AlertType.ERROR, errorMessage).show();
        }
    }

    @FXML
    void btnUpdateCustomerOnAction(ActionEvent event) {
        MemberDTO memberDTO = collectData();
        try {
            boolean isUpdated = service.update(memberDTO);
            if (isUpdated){
                new Alert(Alert.AlertType.INFORMATION,"Member Updated Success").show();
                clearFields();
                loadTableData();
            }else{
                new Alert(Alert.AlertType.ERROR,"Member Not Updated").show();
            }
        } catch (MemberException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void visualizeTable() {
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMemberName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMemberAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colMemberEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colMemberContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
    }

    public void loadTableData(){
        try {
            List<MemberTM> list = new ArrayList<>();
            List<MemberDTO> all = service.getAll();
            for (MemberDTO memberDTO : all) {
                MemberTM memberTM = convertMemberDTOtoTM(memberDTO);
                list.add(memberTM);
            }
            ObservableList<MemberTM> memberTMS = FXCollections.observableArrayList(list);
            tblMember.setItems(memberTMS);
        } catch (MemberException e) {
            e.printStackTrace();
        }
    }

    public MemberDTO collectData(){
        String id = txtMemberId.getText();
        String name = txtMemberName.getText();
        String address = txtMemberAddress.getText();
        String email = txtMemberEmail.getText();
        String contact = txtMemberContact.getText();

        MemberDTO memberDTO = new MemberDTO(id,name,address,email,contact);
        System.out.println(memberDTO);
        return memberDTO;
    }

    public void setDataToFields(MemberDTO member){
        txtMemberId.setText(member.getId());
        txtMemberName.setText(member.getName());
        txtMemberAddress.setText(member.getAddress());
        txtMemberEmail.setText(member.getEmail());
        txtMemberContact.setText(member.getContact());
    }

    public void clearFields(){
        txtMemberId.clear();
        txtMemberName.clear();
        txtMemberAddress.clear();
        txtMemberEmail.clear();
        txtMemberContact.clear();
    }

    private MemberTM convertMemberDTOtoTM(MemberDTO memberDTO){
        MemberTM memberTM = new MemberTM();
        memberTM.setId(memberDTO.getId());
        memberTM.setName(memberDTO.getName());
        memberTM.setAddress(memberDTO.getAddress());
        memberTM.setEmail(memberDTO.getEmail());
        memberTM.setContact(memberDTO.getContact());
        return memberTM;
    }


    public void tblMemberOnMouseClick(MouseEvent mouseEvent) {
        MemberTM selectedMember = tblMember.getSelectionModel().getSelectedItem();
        if (selectedMember !=null){
            txtMemberId.setText(selectedMember.getId());
            memberIdOnAction(null);
        }
    }
}
