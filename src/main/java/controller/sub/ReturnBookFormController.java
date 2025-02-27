package controller.sub;

import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import dto.BookRecordsDTO;
import dto.FineDTO;
import exceptions.ServiceException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import service.custom.BorrowBookRecordsService;
import service.custom.FineService;
import service.util.ServiceFactory;
import service.util.ServiceTypes;
import tm.FineTM;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ReturnBookFormController {

    @FXML
    private TableView<FineTM> tblRecords;

    @FXML
    private TableColumn<FineTM, Integer> colRecordID;

    @FXML
    private TableColumn<FineTM, Integer> colBookID;

    @FXML
    private TableColumn<FineTM, String> colMemberID;

    @FXML
    private TableColumn<FineTM, LocalDate> colBorrowDate;

    @FXML
    private TableColumn<FineTM, LocalDate> colReturnDate;

    @FXML
    private Label lblBalance;

    @FXML
    private Label lblFine;

    @FXML
    private Label lblLateDateCount;

    @FXML
    private JFXRadioButton rbBookId;

    @FXML
    private JFXRadioButton rbMemberId;

    @FXML
    private ToggleGroup test;

    @FXML
    private JFXTextField txtEnterKeyWordToSearch;

    @FXML
    private JFXTextField txtFineForOneDay;

    @FXML
    private JFXTextField txtPayment;

    private final FineService fineService = (FineService) ServiceFactory.getInstance().getService(ServiceTypes.FINE_SERVICE);
    private final BorrowBookRecordsService borrowBookRecordsService = (BorrowBookRecordsService) ServiceFactory.getInstance().getService(ServiceTypes.BORROW_BOOK_RECORDS_SERVICE);

    @FXML
    public void initialize() {
        setUpTableColumns();
        loadAllRecords();
    }

    private void setUpTableColumns() {
        colRecordID.setCellValueFactory(new PropertyValueFactory<>("recordId"));
        colBookID.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colMemberID.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowedDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    }

    private void loadAllRecords() {
        try {
            List<BookRecordsDTO> records = borrowBookRecordsService.getAll();
            ObservableList<FineTM> data = FXCollections.observableArrayList();
            for (BookRecordsDTO record : records) {
                data.add(new FineTM(
                        record.getId(),
                        record.getBook_id(),
                        record.getMember_id(),
                        record.getBorrowed_date(),
                        record.getReturnDate()
                ));
            }
            tblRecords.setItems(data);
        } catch (ServiceException e) {
            showAlert(Alert.AlertType.ERROR, "Error loading records: " + e.getMessage());
        }
    }

    @FXML
    void btnLoadTableOnAction(ActionEvent event) {
        loadAllRecords();
    }

    @FXML
    void btnMarkAsReturnedOnAction(ActionEvent event) {
        FineTM selectedRecord = tblRecords.getSelectionModel().getSelectedItem();
        if (selectedRecord == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a record to mark as returned.");
            return;
        }

        try {
            Integer finePerDay = Integer.parseInt(txtFineForOneDay.getText());
            Integer paymentAmount = Integer.parseInt(txtPayment.getText());
            LocalDate dueDate = selectedRecord.getReturnDate();
            Integer daysLate = calculateLateDays(dueDate);

            Integer fineAmount = daysLate * finePerDay;
            Integer balance = paymentAmount - fineAmount;

            lblLateDateCount.setText(String.valueOf(daysLate));
            lblFine.setText(String.valueOf(fineAmount));
            lblBalance.setText(String.valueOf(balance));

            FineDTO fineDTO = new FineDTO(
                    selectedRecord.getRecordId(),
                    selectedRecord.getBookId(),
                    selectedRecord.getMemberId(),
                    daysLate,
                    fineAmount,
                    paymentAmount,
                    balance
            );

            fineService.add(fineDTO);
            borrowBookRecordsService.markAsReturned(selectedRecord.getRecordId());

            showAlert(Alert.AlertType.INFORMATION, "Book marked as returned successfully!");
            loadAllRecords();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Please enter valid numbers for fine and payment.");
        } catch (ServiceException e) {
            showAlert(Alert.AlertType.ERROR, "Error processing return: " + e.getMessage());
        }
    }

    private int calculateLateDays(LocalDate dueDate) {
        LocalDate today = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(dueDate, today);
        return (int) daysBetween;
    }

    @FXML
    void txtEnterKeyWordOnAction(ActionEvent event) {
        String searchKeyWord = txtEnterKeyWordToSearch.getText().trim(); // Trim spaces
        if (searchKeyWord.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please enter a search keyword.");
            return;
        }

        boolean searchByBookId;
        if (rbBookId.isSelected()) {
            searchByBookId = true;
        } else if (rbMemberId.isSelected()) {
            searchByBookId = false;
        } else {
            showAlert(Alert.AlertType.ERROR, "Please select a search criteria.");
            return;
        }

        List<BookRecordsDTO> results = borrowBookRecordsService.searchRecords(searchKeyWord, searchByBookId);

        if (results.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No records found for the given keyword.");
        } else {
            loadSearchResults(results);
        }
    }

    private void loadSearchResults(List<BookRecordsDTO> results) {
        ObservableList<FineTM> data = FXCollections.observableArrayList();
        for (BookRecordsDTO record : results) {
            data.add(new FineTM(
                    record.getId(),
                    record.getBook_id(),
                    record.getMember_id(),
                    record.getBorrowed_date(),
                    record.getReturnDate()
            ));
        }
        tblRecords.setItems(data);
    }


    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.show();
    }
}
