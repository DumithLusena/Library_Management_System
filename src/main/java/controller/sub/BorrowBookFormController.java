package controller.sub;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import dto.*;
import exceptions.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.modelmapper.ModelMapper;
import service.custom.BorrowBookRecordsService;
import service.custom.BookService;
import service.custom.CategoryService;
import service.custom.MemberService;
import service.util.*;
import tm.BorrowBookRecordsTM;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BorrowBookFormController {

    @FXML
    private TableView<BorrowBookRecordsTM> tblCart;

    @FXML
    private TableColumn<BorrowBookRecordsTM, Integer> colRecordID;

    @FXML
    private TableColumn<BorrowBookRecordsTM, Integer> colBookID;

    @FXML
    private TableColumn<BorrowBookRecordsTM, String> colMemberID;

    @FXML
    private TableColumn<BorrowBookRecordsTM, LocalDate> colBorrowDate;

    @FXML
    private TableColumn<?, ?> colReturnDate;

    @FXML
    private DatePicker dpBorrowedDate;

    @FXML
    private DatePicker dpReturnDate;

    @FXML
    private JFXTextField txtBookCategory;

    @FXML
    private JFXTextField txtBookISBN;

    @FXML
    private JFXTextField txtBookId;

    @FXML
    private JFXTextField txtBookName;

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

    private final BorrowBookRecordsService bookRecordsService = (BorrowBookRecordsService) ServiceFactory.getInstance().getService(ServiceTypes.BORROW_BOOK_RECORDS_SERVICE);
    private final BookService bookService = (BookService) BookServiceFactory.getInstance().getBookService(BookServiceTypes.BOOK_SERVICE);
    private final MemberService memberService = (MemberService) ServiceFactory.getInstance().getService(ServiceTypes.MEMBER_SERVICE);
    private final CategoryService categoryService = (CategoryService) ServiceFactory.getInstance().getService(ServiceTypes.CATEGORY_SERVICE);

    private final ModelMapper modelMapper = OtherDependancies.getInstance().getModelMapper();

    ObservableList<BorrowBookRecordsTM> borrowBookRecordsTMS = FXCollections.observableArrayList();

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
        if (validateFields()) {
            Integer id = 0;
            Integer bookId = Integer.parseInt(txtBookId.getText());
            String memberId = txtMemberId.getText();
            LocalDate borrowedDate = dpBorrowedDate.getValue();
            LocalDate returnDate = dpReturnDate.getValue();

            borrowBookRecordsTMS.add(new BorrowBookRecordsTM(id, bookId, memberId, borrowedDate, returnDate));
            tblCart.setItems(borrowBookRecordsTMS);
        }
    }

    @FXML
    void btnConfirmOnAction(ActionEvent event) {
        BookRecordsDTO bookRecordsDTO = collectData();
        if (bookRecordsDTO != null) {
            try {
                bookRecordsService.add(bookRecordsDTO);
                new Alert(Alert.AlertType.INFORMATION, "Book Borrowed Successfully").show();
                loadTableData();
                visualizeTableData();
                clearData();
            } catch (ServiceException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    @FXML
    void txtMemberIdOnAction(ActionEvent event) {
        String memberId = txtMemberId.getText();
        if (memberId.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a Member ID").show();
            return;
        }

        try {
            Optional<MemberDTO> memberOpt = memberService.search(memberId);
            if (memberOpt.isPresent()) {
                MemberDTO memberDTO = memberOpt.get();
                txtMemberName.setText(memberDTO.getName());
                txtMemberAddress.setText(memberDTO.getAddress());
                txtMemberContact.setText(memberDTO.getContact());
                txtMemberEmail.setText(memberDTO.getEmail());
            } else {
                new Alert(Alert.AlertType.ERROR, "Member not found").show();
            }
        } catch (ServiceException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtBookIdOnAction(ActionEvent event) {
        String bookIdText = txtBookId.getText();

        if (bookIdText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a Book ID").show();
            return;
        }

        try {
            Integer bookId = Integer.parseInt(bookIdText);
            Optional<BookDTO> search = bookService.search(bookId);

            if (search.isPresent()) {
                BookDTO bookDTO = search.get();
                setBookData(bookDTO);

                Integer mainCategoryId = bookDTO.getMainCategoryId();
                Optional<CategoryDTO> mainCategorySearch = categoryService.search(mainCategoryId);

                if (mainCategorySearch.isPresent()) {
                    txtBookCategory.setText(mainCategorySearch.get().getName());
                } else {
                    txtBookCategory.setText("Category Not Found");
                }
            } else {
                clearBookFields();
                new Alert(Alert.AlertType.ERROR, "Book not found!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid Book ID!").show();
        } catch (ServiceException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    private void setBookData(BookDTO bookDTO) {
        txtBookId.setText(String.valueOf(bookDTO.getId()));
        txtBookName.setText(bookDTO.getName());
        txtBookISBN.setText(bookDTO.getIsbn());
    }

    private void clearBookFields() {
        txtBookId.clear();
        txtBookName.clear();
        txtBookISBN.clear();
        txtBookCategory.clear();
    }

    private void clearData() {
        txtMemberId.clear();
        txtMemberName.clear();
        txtMemberAddress.clear();
        txtMemberContact.clear();
        txtMemberEmail.clear();
        txtBookId.clear();
        txtBookName.clear();
        txtBookISBN.clear();
        txtBookCategory.clear();
        dpBorrowedDate.getEditor().clear();
        dpReturnDate.getEditor().clear();
    }

    public void initialize() {
        visualizeTableData();
        loadTableData();
    }

    private BookRecordsDTO collectData() {
        BookRecordsDTO dto = new BookRecordsDTO();
        LocalDate borrowedDate = dpBorrowedDate.getValue();
        LocalDate returnDate = dpReturnDate.getValue();

        if (borrowedDate == null) {
            new Alert(Alert.AlertType.ERROR, "Invalid Borrowed Date").show();
            return null;
        }

        if (txtBookId.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter a book ID").show();
            return null;
        }

        if (txtMemberId.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please enter a member ID").show();
            return null;
        }

        dto.setId(0);
        dto.setBorrowed_date(borrowedDate);
        dto.setIsReturned(false);
        dto.setReturnDate(returnDate);

        dto.setBook_id(Integer.valueOf(txtBookId.getText()));
        dto.setMember_id(txtMemberId.getText());

        return dto;
    }

    private void visualizeTableData() {
        colRecordID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBookID.setCellValueFactory(new PropertyValueFactory<>("book_Id"));
        colMemberID.setCellValueFactory(new PropertyValueFactory<>("member_Id"));
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    }

    private void loadTableData() {
        try {
            List<BookRecordsDTO> all = bookRecordsService.getAll();
            List<BorrowBookRecordsTM> list = new ArrayList<>();
            for (BookRecordsDTO bookRecordsDTO : all) {
                list.add(convertDTOtoTM(bookRecordsDTO));
            }
            ObservableList<BorrowBookRecordsTM> borrowBookRecordsTMS = FXCollections.observableArrayList(list);
            tblCart.setItems(borrowBookRecordsTMS);
        } catch (ServiceException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading table data: " + e.getMessage()).show();
        }
    }

    private BorrowBookRecordsTM convertDTOtoTM(BookRecordsDTO obj) {
        return modelMapper.map(obj, BorrowBookRecordsTM.class);
    }

    @FXML
    public void tblDetailOnMouseClick(MouseEvent mouseEvent) {
        BorrowBookRecordsTM selectedItem = tblCart.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            txtBookId.setText(String.valueOf(selectedItem.getBook_Id()));
            txtBookIdOnAction(null);
            txtMemberId.setText(selectedItem.getMember_Id());
            txtMemberIdOnAction(null);
            dpBorrowedDate.setValue(selectedItem.getBorrowedDate());
            dpReturnDate.setValue(selectedItem.getReturnDate());
        }
    }

    private boolean validateFields() {
        if (txtBookId.getText().isEmpty() || txtMemberId.getText().isEmpty() || dpBorrowedDate.getValue() == null || dpReturnDate.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "All fields must be filled").show();
            return false;
        }
        return true;
    }

    public void btnPrintReportOnAction(ActionEvent actionEvent) {
        try {
            JasperDesign design = JRXmlLoader.load("src/main/resources/report/borrow_book_record_report.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, DBConnection.getInstance().getConnection());

            String reportPath = "D:/JavaFX ICET/Library Management System Project/Reports/borrow_book_record_report.pdf";

            JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath);
            JasperViewer.viewReport(jasperPrint,false);

        } catch (JRException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
