package ch.makery.address.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ch.makery.address.model.Event;
import ch.makery.address.util.TimeUtil;

public class EventEditDialogController {

    @FXML
    private TextField placeField;
    @FXML
    private TextField descriptionField;
    @FXML
    private DatePicker dateField = new DatePicker();
    @FXML
    private TextField timeField;



    private Stage dialogStage;
    private Event event;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    	
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEvent(Event event) {
        this.event = event;

        placeField.setText(event.getPlace());
        descriptionField.setText(event.getDescription());
        dateField.setValue(event.getDate());
        timeField.setText(TimeUtil.format(event.getTime()));
        timeField.setPromptText("HH:MM");
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            event.setPlace(placeField.getText());
            event.setDescription(descriptionField.getText());
            event.setDate(dateField.getValue());
            event.setTime(TimeUtil.parse(timeField.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (placeField.getText() == null || placeField.getText().length() == 0) {
            errorMessage += "Nieprawid這we miejsce!\n"; 
        }
        if (descriptionField.getText() == null || descriptionField.getText().length() == 0) {
            errorMessage += "Nieprawid這wy opis!\n"; 
        }

        if (dateField.getValue() == null) {
            errorMessage += "Nieprawid這wa data!\n";
        }
        
        if (timeField.getText() == null || timeField.getText().length() == 0) {
            errorMessage += "Nieprawid這wa godzina!\n";
        } else {
            if (!TimeUtil.validTime(timeField.getText())) {
                errorMessage += "Nieprawid這wa godzina! U篡j formatu HH:MM\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Nieprawid這we pola");
            alert.setHeaderText("Popraw i spr鏏uj ponownie");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}