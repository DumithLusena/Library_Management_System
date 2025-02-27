package controller.sub;

import com.jfoenix.controls.JFXTextField;
import dto.AuthorDTO;
import dto.PublisherDTO;
import exceptions.ServiceException;
import exceptions.custom.PublisherException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import org.modelmapper.ModelMapper;
import service.custom.AuthorService;
import service.custom.PublisherService;
import service.util.OtherDependancies;
import service.util.ServiceFactory;
import service.util.ServiceTypes;
import tm.AuthorTM;
import tm.PublisherTM;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManageAuthorsAndPublishersFormController {

    @FXML
    private TableView<PublisherTM> tblPublishers;

    @FXML
    private TableColumn<PublisherTM,Integer> colPublisherId;

    @FXML
    private TableColumn<PublisherTM,String> colPublisherName;

    @FXML
    private TableColumn<PublisherTM,String> colPublisherContact;

    @FXML
    private TableView<AuthorTM> tblAuthor;

    @FXML
    private TableColumn<AuthorTM,Integer> colAuthorId;

    @FXML
    private TableColumn<AuthorTM,String> colAuthorName;

    @FXML
    private TableColumn<AuthorTM,String> colAuthorContact;

    @FXML
    private JFXTextField txtAuthorContact;

    @FXML
    private JFXTextField txtAuthorId;

    @FXML
    private JFXTextField txtAuthorName;

    @FXML
    private JFXTextField txtPublisherContact;

    @FXML
    private JFXTextField txtPublisherId;

    @FXML
    private JFXTextField txtPublisherLocation;

    @FXML
    private JFXTextField txtPublisherName;


    private final PublisherService publisherService = (PublisherService)ServiceFactory.getInstance().getService(ServiceTypes.PUBLISHER_SERVICE);
    private final ModelMapper modelMapper = OtherDependancies.getInstance().getModelMapper();
    private final AuthorService authorService = (AuthorService) ServiceFactory.getInstance().getService(ServiceTypes.AUTHOR_SERVICE);

    public void initialize() {
        txtPublisherId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,String oldValue,String newValue) {
                if (!newValue.matches("\\d*")){
                    txtPublisherId.setText(newValue.replaceAll("[^\\d]",""));
                }
            }
        });

        visualizeTableData();
        loadTableData();

        txtAuthorId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,String oldValue,String newValue) {
                if (!newValue.matches("\\d*")){
                    txtAuthorId.setText(newValue.replaceAll("[^\\d]",""));
                }
            }
        });

        visualizeAuthorTableData();
        loadAuthorTable();
    }

    @FXML
    void PublisherIdOnAction(ActionEvent event) {
        PublisherDTO publisherDTO = collectData();
        try {
            Optional<PublisherDTO> search = publisherService.search(publisherDTO.getId());
            if (search.isPresent()){
                setDataToFields(search.get());
            }else{
                new Alert(Alert.AlertType.ERROR,"Publisher Not Found!").show();
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnPublisherSaveOnAction(ActionEvent event) {
        PublisherDTO publisherDTO = collectData();
        try {
            boolean isSave = publisherService.add(publisherDTO);
            if (isSave){
                new Alert(Alert.AlertType.INFORMATION,"Publisher Saved Successfully!").show();
                clearPublisherFields();
                loadTableData();
            }else {
                new Alert(Alert.AlertType.ERROR,"Publisher Not Saved!").show();
            }
        } catch (ServiceException e) {
            if (e instanceof PublisherException){
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnPublisherUpdateOnAction(ActionEvent event) {
        PublisherDTO publisherDTO = collectData();
        try {
            boolean isUpdate = publisherService.update(publisherDTO);
            if (isUpdate){
                new Alert(Alert.AlertType.INFORMATION,"Publisher Updated Successfully!").show();
                clearPublisherFields();
                loadTableData();
            } else {
                new Alert(Alert.AlertType.ERROR,"Publisher Not Updated!").show();
            }
        } catch (ServiceException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnPublisherDeleteOnAction(ActionEvent event) {
        PublisherDTO publisherDTO = collectData();
        if (publisherDTO.getId()==0){
            new Alert(Alert.AlertType.ERROR,"Invalid ID - Please Enter valid ID").show();
            return;
        }

        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure", ButtonType.YES, ButtonType.NO).showAndWait();
        if (buttonType.isPresent()){
            if (buttonType.get().equals(ButtonType.YES)){
                try {
                    boolean delete = publisherService.delete(publisherDTO.getId());
                    if (delete){
                        new Alert(Alert.AlertType.INFORMATION,"Deleted Success").show();
                        loadTableData();
                    }else{
                        new Alert(Alert.AlertType.ERROR,"Not Deleted").show();
                    }
                } catch (ServiceException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                }
            }
        }
    }

    @FXML
    void btnPublisherClearOnAction(ActionEvent event) {
        clearPublisherFields();
    }

    private void clearPublisherFields(){
        txtPublisherId.clear();
        txtPublisherName.clear();
        txtPublisherLocation.clear();
        txtPublisherContact.clear();
    }

    private PublisherDTO collectData() {
        String id = txtPublisherId.getText();
        String name = txtPublisherName.getText();
        String location = txtPublisherLocation.getText();
        String contact = txtPublisherContact.getText();

        int idNum = 0;
        try {
            idNum = Integer.parseInt(id);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        PublisherDTO publisherDTO = new PublisherDTO();
        publisherDTO.setId(idNum);
        publisherDTO.setName(name);
        publisherDTO.setLocation(location);
        publisherDTO.setContact(contact);
        return publisherDTO;
    }

    private void setDataToFields(PublisherDTO publisherDTO) {
        txtPublisherId.setText(String.valueOf(publisherDTO.getId()));
        txtPublisherName.setText(publisherDTO.getName());
        txtPublisherLocation.setText(publisherDTO.getLocation());
        txtPublisherContact.setText(publisherDTO.getContact());
    }

    private void loadTableData(){
        try {
            List<PublisherDTO> all = publisherService.getAll();
            List<PublisherTM> list = new ArrayList<>();
            for (PublisherDTO publisherDTO : all) {
                list.add(convertDTOtoTM(publisherDTO));
            }
            ObservableList<PublisherTM> publisherTMS = FXCollections.observableArrayList(list);
            tblPublishers.setItems(publisherTMS);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private void visualizeTableData() {
        colPublisherId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPublisherName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPublisherContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
    }

    private PublisherTM convertDTOtoTM(PublisherDTO obj){
        return modelMapper.map(obj, PublisherTM.class);
    }


    @FXML
    void AuthorIdOnAction(ActionEvent event) {
        AuthorDTO authorDTO = authorCollectData();
        try {
            Optional<AuthorDTO> search = authorService.search(authorDTO.getId());
            if (search.isPresent()){
                setAuthorDataToFields(search.get());
            }else{
                new Alert(Alert.AlertType.ERROR,"Author Not Found!").show();
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnAuthorSaveOnAction(ActionEvent event) {
        AuthorDTO authorDTO = authorCollectData();
        try {
            boolean isSave = authorService.add(authorDTO);
            if (isSave){
                new Alert(Alert.AlertType.INFORMATION,"Author Saved Success!").show();
                clearAuthorFields();
                loadAuthorTable();
            }else{
                new Alert(Alert.AlertType.ERROR,"Author Not Saved!").show();
            }
        } catch (ServiceException e) {
            if (e instanceof PublisherException){
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnAuthorUpdateOnAction(ActionEvent event) {
        AuthorDTO authorDTO = authorCollectData();
        try {
            boolean isUpdate = authorService.update(authorDTO);
            if (isUpdate){
                new Alert(Alert.AlertType.INFORMATION,"Author Updated Successfully!").show();
                clearAuthorFields();
                loadAuthorTable();
            }else{
                new Alert(Alert.AlertType.ERROR,"Author Not Updated!").show();
            }
        } catch (ServiceException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnAuthorDeleteOnAction(ActionEvent event) {
        AuthorDTO authorDTO = authorCollectData();
        if (authorDTO.getId() == 0) {
            new Alert(Alert.AlertType.ERROR, "Invalid ID - Please Enter valid ID").show();
            return;
        }

        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure", ButtonType.YES, ButtonType.NO).showAndWait();
        if (buttonType.isPresent()) {
            if (buttonType.get().equals(ButtonType.YES)) {
                try {
                    boolean delete = authorService.delete(authorDTO.getId());
                    if (delete) {
                        new Alert(Alert.AlertType.INFORMATION, "Deleted Success").show();
                        loadAuthorTable();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Not Deleted").show();
                    }
                } catch (ServiceException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                }
            }
        }
    }

    @FXML
    void btnAuthorClearOnAction(ActionEvent event) {
        clearAuthorFields();
    }

    private void clearAuthorFields(){
        txtAuthorId.clear();
        txtAuthorName.clear();
        txtAuthorContact.clear();
    }

    private AuthorDTO authorCollectData(){
        String id = txtAuthorId.getText();
        String name = txtAuthorName.getText();
        String contact = txtAuthorContact.getText();

        Integer idNum = 0;
        try{
            idNum = Integer.parseInt(id);
            System.out.println("ex : " +idNum);
        }catch (NumberFormatException e){
            e.printStackTrace();
            System.out.println("print : " +e.getMessage());
        }
        return new AuthorDTO(idNum,name,contact);
    }

    private void setAuthorDataToFields(AuthorDTO authorDTO){
        txtAuthorId.setText(String.valueOf(authorDTO.getId()));
        txtAuthorName.setText(authorDTO.getName());
        txtAuthorContact.setText(authorDTO.getContact());
    }

    private AuthorTM convertAuthorDTOtoTM(AuthorDTO obj){
        return modelMapper.map(obj,AuthorTM.class);
    }

    private void loadAuthorTable(){
        try {
            List<AuthorTM> list = new ArrayList<>();
            List<AuthorDTO> all = authorService.getAll();
            for (AuthorDTO authorDTO : all){
                list.add(convertAuthorDTOtoTM(authorDTO));
            }
            ObservableList<AuthorTM> authorTMS = FXCollections.observableArrayList(list);
            tblAuthor.setItems(authorTMS);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private void visualizeAuthorTableData() {
        colAuthorId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAuthorName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAuthorContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
    }

    public void tblAuthorsOnMouseClick(javafx.scene.input.MouseEvent mouseEvent) {
        AuthorTM selectedAuthor = tblAuthor.getSelectionModel().getSelectedItem();
        if (selectedAuthor != null){
            txtAuthorId.setText(String.valueOf(selectedAuthor.getId()));
            AuthorIdOnAction(null);
        }
    }

    public void tblPublisherOnMouseClick(MouseEvent mouseEvent) {
        PublisherTM selectedPublisher = tblPublishers.getSelectionModel().getSelectedItem();
        if (selectedPublisher != null){
            txtPublisherId.setText(String.valueOf(selectedPublisher.getId()));
            PublisherIdOnAction(null);
        }
    }
}
