package ch.makery.address.view;


import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage; 

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import ch.makery.address.MainApp;
import ch.makery.address.model.Event;
import ch.makery.address.model.Reminder;

public class ReminderDialogController {
	
	class LimitedTextField extends TextField {

	    private final int limit;

	    public LimitedTextField(int limit) {
	        this.limit = limit;
	    }

	    @Override
	    public void replaceText(int start, int end, String text) {
	        super.replaceText(start, end, text);
	        verify();
	    }

	    @Override
	    public void replaceSelection(String text) {
	        super.replaceSelection(text);
	        verify();
	    }

	    private void verify() {
	        if (getText().length() > limit) {
	            setText(getText().substring(0, limit));
	        }

	    }
	};

    @FXML
    private Label placeLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private TextField reminderTimeField = new LimitedTextField(1000);

    private Reminder reminder;
    private Stage dialogStage;
    private Event event;
    private MainApp mainApp;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEvent(Event event) {
        this.event = event;

        placeLabel.setText(event.getPlace());
        dateLabel.setText(event.getDate().toString());
        timeLabel.setText(event.getTime().toString());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
        	int intReminderTime = Integer.parseInt(reminderTimeField.getText());
        	event.setReminder(intReminderTime);
        	reminder = new Reminder(event);
        	mainApp.getReminderData().add(reminder);
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
        
        
    try {
        if(reminderTimeField.getText() == null || reminderTimeField.getText().length() == 0) {
        	errorMessage += "Nic nie wpisa�e�!\n";	
        } else {
            if (!(reminderTimeField.getText().matches("[0-9]*")) || Integer.parseInt(reminderTimeField.getText())==0) {
                errorMessage += "W polu musi znale�� si� dodatnia, ca�kowita warto�� liczbowa!\n";
            } else {
                if(LocalDateTime.now().isAfter(LocalDateTime.of(event.getDate(), event.getTime())
                		.minusMinutes(Integer.parseInt(reminderTimeField.getText())))) {
                	errorMessage += "Za du�a ilo�� czasu! Ustaw przypomnienie tak, by znalaz�o si� w przedziale czasowym mi�dzy dat� zdarzenia, a obecn�.\n";
                } 
                	  
            }
        }
    } catch (NumberFormatException e) {
    	Alert alert = new Alert(AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("Za du�a warto��!");
        alert.setHeaderText("Podana warto�� nie mie�ci si� w skali typu Integer");
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }
        
        

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Niepoprawny czas!");
            alert.setHeaderText("Wpisz poprawny czas");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
    
