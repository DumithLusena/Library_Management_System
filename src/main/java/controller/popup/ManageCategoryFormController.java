package controller.popup;

import com.jfoenix.controls.JFXTextField;
import controller.sub.ManageBookFormController;
import dto.CategoryDTO;
import exceptions.ServiceException;
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
import service.custom.CategoryService;
import service.util.OtherDependancies;
import service.util.ServiceFactory;
import service.util.ServiceTypes;
import tm.CategoryTM;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManageCategoryFormController {
    @FXML
    private TableView<CategoryTM> tblCategories;

    @FXML
    private TableColumn<CategoryTM, Integer> colCategoryId;

    @FXML
    private TableColumn<CategoryTM, String> colCategoryName;

    @FXML
    private JFXTextField txtCategoryId;

    @FXML
    private JFXTextField txtCategoryName;


    private final CategoryService categoryService = (CategoryService) ServiceFactory.getInstance().getService(ServiceTypes.CATEGORY_SERVICE);
    private final ModelMapper modelMapper = OtherDependancies.getInstance().getModelMapper();

    private ManageBookFormController baseController;

    public void initialize(){
        txtCategoryId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")){
                    txtCategoryId.setText(newValue.replaceAll("[^\\d]",""));
                }
            }
        });

        visualizeTable();
        loadDataToTable();
    }

    public void txtCategoryIdOnAction(ActionEvent actionEvent) {
        CategoryDTO categoryDTO = collectData();
        try {
            Optional<CategoryDTO> search = categoryService.search(categoryDTO.getId());
            if (search.isPresent()){
                txtCategoryName.setText(search.get().getName());
            }else {
                new Alert(Alert.AlertType.ERROR,"Invalid ID").show();
            }
        } catch (ServiceException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        CategoryDTO categoryDTO = collectData();
        try {
            boolean isSave = categoryService.add(categoryDTO);
            if (isSave){
                new Alert(Alert.AlertType.INFORMATION,"Category Saved Successfully!").show();
                loadDataToTable();
                clearFields();
            }else {
                new Alert(Alert.AlertType.ERROR,"Category Not Saved!").show();
            }
        } catch (ServiceException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        CategoryDTO categoryDTO = collectData();
        if (categoryDTO.getId()==0){
            new Alert(Alert.AlertType.ERROR,"Invalid ID").show();
            return;
        }
        
        try {
            boolean isUpdate = categoryService.update(categoryDTO);
            if (isUpdate){
                new Alert(Alert.AlertType.INFORMATION,"Category Updated Successfully!").show();
                loadDataToTable();
                clearFields();
            }else {
                new Alert(Alert.AlertType.ERROR,"Category Not Updated!").show();
            }
        } catch (ServiceException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        CategoryDTO categoryDTO = collectData();
        if (categoryDTO.getId()==0){
            new Alert(Alert.AlertType.ERROR,"Invalid ID - Please Enter valid ID").show();
            return;
        }

        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure", ButtonType.YES, ButtonType.NO).showAndWait();
        if (buttonType.isPresent()){
            if (buttonType.get().equals(ButtonType.YES)){
                try {
                    boolean delete = categoryService.delete(categoryDTO.getId());
                    if (delete) {
                        new Alert(Alert.AlertType.INFORMATION, "Deleted Success").show();
                        loadDataToTable();
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
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields(){
        txtCategoryId.clear();
        txtCategoryName.clear();
    }

    private CategoryDTO collectData(){
        String id = txtCategoryId.getText();
        String name = txtCategoryName.getText();

        int idNum = 0;
        try {
            idNum = Integer.parseInt(id);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return new CategoryDTO(idNum,name);
    }

    private void loadDataToTable(){
        try {
            List<CategoryTM> list = new ArrayList<>();
            List<CategoryDTO> all = categoryService.getAll();
            for (CategoryDTO categoryDTO : all) {
                list.add(convertDTOtoTM(categoryDTO));
            }
            ObservableList<CategoryTM> categoryTMS = FXCollections.observableArrayList(list);
            tblCategories.setItems(categoryTMS);
            if (baseController!=null)baseController.loadCategoryData();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private void visualizeTable(){
        colCategoryId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCategoryName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private CategoryTM convertDTOtoTM(CategoryDTO obj){
        return modelMapper.map(obj, CategoryTM.class);
    }


    public void tblCategoryOnMouseClick(MouseEvent mouseEvent) {
        CategoryTM selectedCategory = tblCategories.getSelectionModel().getSelectedItem();
        if (selectedCategory != null){
            txtCategoryId.setText(String.valueOf(selectedCategory.getId()));
            txtCategoryIdOnAction(null);
        }
    }

    public void setBaseController(ManageBookFormController baseController) {
        this.baseController = baseController;
    }

}
