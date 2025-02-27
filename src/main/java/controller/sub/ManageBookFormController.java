package controller.sub;

import cm.CategoryCM;
import cm.PublisherCM;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controller.popup.ManageCategoryFormController;
import dto.BookDTO;
import dto.PublisherDTO;
import exceptions.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.modelmapper.ModelMapper;
import service.custom.AuthorService;
import service.custom.BookService;
import service.custom.CategoryService;
import service.custom.PublisherService;
import service.util.*;
import tm.AuthorTMWithCheckBox;
import tm.BookTM;
import tm.CategoryTMWithCheckBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ManageBookFormController {

    @FXML
    private JFXComboBox<CategoryCM> cmbMainCategory;

    @FXML
    private JFXComboBox<PublisherCM> cmbPublisher;

    @FXML
    private TableView<CategoryTMWithCheckBox> tblSubCategories;

    @FXML
    private TableColumn<CategoryTMWithCheckBox,String> colCategoryName;

    @FXML
    private TableColumn<CategoryTMWithCheckBox, CheckBox> colOption;

    @FXML
    private TableView<AuthorTMWithCheckBox> tblAuthors;

    @FXML
    private TableColumn<AuthorTMWithCheckBox, String> colAuthorName;

    @FXML
    private TableColumn<AuthorTMWithCheckBox, CheckBox> colAuthorOption;

    @FXML
    private TableView<BookTM> tblBooks;

    @FXML
    private TableColumn<BookTM, Integer> colBookId;

    @FXML
    private TableColumn<BookTM, String> colBookName;

    @FXML
    private TableColumn<BookTM, String> colISBN;

    @FXML
    private TableColumn<BookTM, Double> colPrice;

    @FXML
    private JFXTextField txtBookId;

    @FXML
    private JFXTextField txtISBN;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPrice;


    private final BookService bookService = (BookService) BookServiceFactory.getInstance().getBookService(BookServiceTypes.BOOK_SERVICE);
    private final PublisherService publisherService = (PublisherService) ServiceFactory.getInstance().getService(ServiceTypes.PUBLISHER_SERVICE);
    private final CategoryService categoryService = (CategoryService) ServiceFactory.getInstance().getService(ServiceTypes.CATEGORY_SERVICE);
    private final AuthorService authorService = (AuthorService) ServiceFactory.getInstance().getService(ServiceTypes.AUTHOR_SERVICE);


    private final ModelMapper modelMapper = OtherDependancies.getInstance().getModelMapper();

    public void initialize(){
        addConverterToComboBox();
        setCellValueFactory();
        try {
            ArrayList<PublisherCM> list = new ArrayList<>();
            List<PublisherDTO> all = publisherService.getAll();
            for (PublisherDTO publisherDTO : all) {
                list.add(convertPublisherDTOtoCM(publisherDTO));
            }
            ObservableList<PublisherCM> publisherCMS = FXCollections.observableArrayList(list);
            cmbPublisher.setItems(publisherCMS);

            loadCategoryData();
            loadAuthorData();

        } catch (ServiceException e) {
            System.out.println(e);
        }

        loadBookTableData();

    }

    public void loadCategoryData() throws ServiceException {
        List<CategoryCM> categoryCMList = categoryService.getAll().stream().map(e -> CategoryCM.builder().id(e.getId()).name(e.getName()).build()).collect(Collectors.toList());
        cmbMainCategory.setItems(FXCollections.observableArrayList(categoryCMList));


        List<CategoryTMWithCheckBox> list = categoryCMList.stream().map(e -> CategoryTMWithCheckBox.builder().id(e.getId()).name(e.getName()).checkBox(new CheckBox()).build()).collect(Collectors.toList());
        tblSubCategories.setItems(FXCollections.observableArrayList(list));
    }

    private void loadAuthorData() throws ServiceException {
        List<AuthorTMWithCheckBox> authorList = authorService.getAll().stream().map(e -> AuthorTMWithCheckBox.builder().id(e.getId()).name(e.getName()).checkBox(new CheckBox()).build()).collect(Collectors.toList());
        tblAuthors.setItems(FXCollections.observableArrayList(authorList));
    }

    public void setCellValueFactory(){
        colCategoryName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("checkBox"));

        colAuthorName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAuthorOption.setCellValueFactory(new PropertyValueFactory<>("checkBox"));

        colBookId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    private PublisherCM convertPublisherDTOtoCM(PublisherDTO obj){
        return modelMapper.map(obj, PublisherCM.class);
    }


    public void addConverterToComboBox(){
        cmbPublisher.setConverter(new StringConverter<PublisherCM>() {
            @Override
            public String toString(PublisherCM publisherCM) {
                return publisherCM.getName();
            }

            @Override
            public PublisherCM fromString(String s) {
                return null;
            }
        });


        cmbMainCategory.setConverter(new StringConverter<CategoryCM>() {
            @Override
            public String toString(CategoryCM categoryCM) {
                return categoryCM.getName();
            }

            @Override
            public CategoryCM fromString(String s) {
                return null;
            }
        });
    }

    @FXML
    void BookIdOnAction(ActionEvent event) {
        try {
            Integer bookId = Integer.parseInt(txtBookId.getText());
            Optional<BookDTO> search = bookService.search(bookId);
            if (search.isPresent()){
                setBookData(search.get());
            }else {
                new Alert(Alert.AlertType.ERROR,"To be Implemented").show();
            }

        }catch (NumberFormatException e){

        } catch (ServiceException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }

    private void setBookData(BookDTO bookDTO) {
        clearFields();
        txtBookId.setText(String.valueOf(bookDTO.getId()));
        txtName.setText(bookDTO.getName());
        txtISBN.setText(bookDTO.getIsbn());
        txtPrice.setText(String.valueOf(bookDTO.getPrice()));

        ObservableList<PublisherCM> publisherCMS = cmbPublisher.getItems();
        for (int i = 0; i < publisherCMS.size(); i++) {
            if (publisherCMS.get(i).getId() == bookDTO.getPublisherId()){
                cmbPublisher.getSelectionModel().select(i);
                break;
            }
        }

        ObservableList<CategoryCM> categoryCMB = cmbMainCategory.getItems();
        for (int i = 0; i < categoryCMB.size(); i++) {
            if (categoryCMB.get(i).getId() == bookDTO.getMainCategoryId()){
                cmbMainCategory.getSelectionModel().select(i);
                break;
            }
        }

        ObservableList<CategoryTMWithCheckBox> subCategories = tblSubCategories.getItems();
        for (int i = 0; i < subCategories.size(); i++) {
            Integer id = subCategories.get(i).getId();
            if (bookDTO.getSubCategoryIds().contains(id)){
                subCategories.get(i).getCheckBox().setSelected(true);
            }
        }
        tblSubCategories.refresh();

        ObservableList<AuthorTMWithCheckBox> authors = tblAuthors.getItems();
        for (int i = 0; i < authors.size(); i++) {
            Integer id = authors.get(i).getId();
            if (bookDTO.getAuthorIds().contains(id)){
                authors.get(i).getCheckBox().setSelected(true);
            }
        }
        tblAuthors.refresh();
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        BookDTO bookDTO = collectData();
        System.out.println(bookDTO);
        try {
            boolean add = bookService.add(bookDTO);
            if (add){
                System.out.println(add);
                new Alert(Alert.AlertType.INFORMATION,"Book Saved Successfully!").show();
                clearFields();
                loadBookTableData();
            }
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadBookTableData() {
        try {
            List<BookTM> list = new ArrayList<>();
            List<BookDTO> all = bookService.getAll();
            for (BookDTO bookDTO : all) {
                list.add(convertBookDTOtoTM(bookDTO));
            }
            tblBooks.setItems(FXCollections.observableArrayList(list));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }



    private BookTM convertBookDTOtoTM(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, BookTM.class);
    }

    private void clearFields() {
        txtBookId.clear();
        txtName.clear();
        txtISBN.clear();
        txtPrice.clear();
        cmbPublisher.getSelectionModel().clearSelection();
        cmbMainCategory.getSelectionModel().clearSelection();
        tblSubCategories.getItems().forEach(e-> e.getCheckBox().setSelected(false));
        tblAuthors.getItems().forEach(e-> e.getCheckBox().setSelected(false));
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        BookDTO bookDTO = collectData();
        if (bookDTO.getId()==0){
            new Alert(Alert.AlertType.ERROR,"Invalid ID").show();
            return;
        }

        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure", ButtonType.YES, ButtonType.NO).showAndWait();
        if (buttonType.isPresent()) {
            if (buttonType.get().equals(ButtonType.YES)) {
                try {
                    boolean delete = bookService.delete(bookDTO.getId());
                    if (delete) {
                        new Alert(Alert.AlertType.INFORMATION, "Deleted Success").show();
                        clearFields();
                        loadBookTableData();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Not Deleted").show();
                    }
                } catch (ServiceException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        BookDTO bookDTO = collectData();
        try {
            boolean update = bookService.update(bookDTO);
            if (update){
                new Alert(Alert.AlertType.INFORMATION,"Book Updated Successfully!").show();
                clearFields();
                loadBookTableData();
            }else {
                new Alert(Alert.AlertType.ERROR,"Book Not Updated!").show();
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnManageCategoryOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/popup/manage_category_form.fxml"));
            Parent load = loader.load();
            ManageCategoryFormController controller = loader.getController();
            controller.setBaseController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(load));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(txtBookId.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BookDTO collectData(){
        Integer id = 0;
        String name = txtName.getText();
        String isbn = txtISBN.getText();
        Double price = 0.0;
        Integer publisherId = 0;
        Integer mainCategoryId = 0;
        Integer count = 0;

        try {
            id = Integer.parseInt(txtBookId.getText());
        }catch (NumberFormatException e){

        }

        try {
            price = Double.parseDouble(txtPrice.getText());
        }catch (NumberFormatException e){
            new Alert(Alert.AlertType.ERROR,"Invalid Price").show();
            return null;
        }

        try {
            publisherId = cmbPublisher.getSelectionModel().getSelectedItem().getId();
            count++;
            mainCategoryId = cmbMainCategory.getSelectionModel().getSelectedItem().getId();
        }catch (NullPointerException e){
            String er = count == 0 ? "Publisher" : "Category";
            new Alert(Alert.AlertType.ERROR,"Select "+er).show();
        }

        List<Integer> authorIds = tblAuthors.getItems().stream().filter(e -> e.getCheckBox().isSelected()).map(e -> e.getId()).collect(Collectors.toList());
        List<Integer> subCategoryIds = tblSubCategories.getItems().stream().filter(e -> e.getCheckBox().isSelected()).map(e -> e.getId()).collect(Collectors.toList());

        return BookDTO.builder().id(id).name(name).isbn(isbn).price(price).publisherId(publisherId).mainCategoryId(mainCategoryId).authorIds(authorIds).subCategoryIds(subCategoryIds).build();
    }

}
