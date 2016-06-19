package ch.makery.address.view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import ch.makery.address.MainApp;
import ch.makery.address.model.Event;
import ch.makery.address.model.Reminder;
import ch.makery.address.util.DateUtil;
import ch.makery.address.util.TimeUtil;

public class EventOverviewController {
	
	@FXML
	private TextField filterField;
    @FXML
	private DatePicker filterDatePicker = new DatePicker();
    @FXML
	private DatePicker deleteOlderThanDatePicker = new DatePicker();
    @FXML
	private TextField timeFilterField;
    
    @FXML
    private TableView<Event> eventTable;
    

	public TableView<Event> getEventTable() {
		return eventTable;
	}

	@FXML
    private TableColumn<Event, String> placeColumn;
    @FXML
    private TableColumn<Event, LocalDate> dateColumn;
    @FXML
    private TableColumn<Event, LocalTime> timeColumn;

    @FXML
    private Label placeLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label reminderLabel;
    @FXML
    private TextArea descriptionTextArea;
    
    @FXML
    private SplitPane splitPane;
    @FXML
    private AnchorPane leftPane;
    @FXML
    private AnchorPane rightPane;

    private MainApp mainApp;

    public EventOverviewController() {
    }
    

    
    private void showEventDetails(Event event) {
        if (event != null) {
            
            placeLabel.setText(event.getPlace());
            descriptionTextArea.setText(event.getDescription());
            dateLabel.setText(DateUtil.format(event.getDate()));
            timeLabel.setText(TimeUtil.format(event.getTime()));
            if(event.getReminder() == 0) {
            	reminderLabel.setText("Brak");
            }
            else {
            	reminderLabel.setText("Na " + Integer.toString(event.getReminder()) + " minut przed czasem");
            }
        } else {
            placeLabel.setText("");
            descriptionTextArea.setText("");
            dateLabel.setText("");
            timeLabel.setText("");
            reminderLabel.setText("");
        }
    }
    
    
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Inicjalizacja tabel
        placeColumn.setCellValueFactory(cellData -> cellData.getValue().placeProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        timeColumn.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        
        showEventDetails(null);
        
        // Listener, zmienia prawa czesc okna
           eventTable.getSelectionModel().selectedItemProperty().addListener(
           (observable, oldValue, newValue) -> showEventDetails(newValue));
           
           splitPane.setDividerPositions(0.40);
           rightPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.53));
           descriptionTextArea.setWrapText(true);
           eventTable.setPlaceholder(new Label("Brak elementów w tabeli"));
           leftPane.setBackground(new Background(new BackgroundFill(Color.web("#FFFFE0"), CornerRadii.EMPTY, Insets.EMPTY)));
           rightPane.setBackground(new Background(new BackgroundFill(Color.web("#FFFFE0"), CornerRadii.EMPTY, Insets.EMPTY)));
 
    }
    
   
    
    @FXML
    private void handleNewEvent() {
        Event tempEvent = new Event();
        boolean okClicked = mainApp.showEventEditDialog(tempEvent);
        if (okClicked) {
        	mainApp.getEventData().add(tempEvent);
        }
    }
    
    @FXML
    private void handleDeleteEvent() {
        int selectedIndex = eventTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
        	ObservableList<Reminder> tmp = FXCollections.observableArrayList();
        	
        	mainApp.getReminderData().forEach(reminder -> {
        		if(reminder.getEvent()==eventTable.getSelectionModel().getSelectedItem()){
        			tmp.add(reminder);
        		}
        	});
        	if(tmp.size()!=0) {
    			for(int i=0; i<mainApp.getReminderData().size(); i++){
    				for(int j=0; j<tmp.size(); j++) {
    					if(mainApp.getReminderData().get(i)==tmp.get(j)){
    						mainApp.getReminderData().get(i).getTimer().cancel();
    						mainApp.getReminderData().get(i).getTimer().purge();
    						mainApp.getReminderData().remove(i);
    					}
    				}
    			}
    		}
        	mainApp.getEventData().remove(eventTable.getSelectionModel().getSelectedItem());
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Brak wyboru");
            alert.setHeaderText("Nie wybra³eœ zdarzenia");
            alert.setContentText("Wybierz zdarzenie, jeœli chcesz je usun¹æ");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleEditEvent() {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            boolean okClicked = mainApp.showEventEditDialog(selectedEvent);
            if (okClicked) {
                showEventDetails(selectedEvent);
            }

        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Brak wyboru");
            alert.setHeaderText("Nie wybra³eœ zdarzenia");
            alert.setContentText("Wybierz zdarzenie, jeœli chcesz je edytowaæ");

            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleDeleteOlderThanEvent() {
    	
    	if(deleteOlderThanDatePicker.getValue()!=null) {
    	
    		ObservableList<Event> tmp = FXCollections.observableArrayList();
    	
    		mainApp.getEventData().forEach(event -> {
    		if(event.getDate().isBefore(deleteOlderThanDatePicker.getValue())){
    			tmp.add(event);
    		}
    	});
    		if(tmp.size()!=0) {
    			for(int i=0; i<mainApp.getEventData().size(); i++){
    				for(int j=0; j<tmp.size(); j++) {
    					if(mainApp.getEventData().get(i)==tmp.get(j)){
    						mainApp.getEventData().remove(i);
    					}
    				}
    			}
    		}
    		else {
    			Alert alert = new Alert(AlertType.WARNING);
    			alert.initOwner(mainApp.getPrimaryStage());
    			alert.setTitle("Brak elementów");
    			alert.setHeaderText("Brak elementów do usuniêcia, wybierz inn¹ datê");

    			alert.showAndWait();
    		}
    	}
    	else {
    		Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Brak wyboru");
            alert.setHeaderText("Nie wybra³eœ daty");
            alert.setContentText("Wybierz datê i spróbuj ponownie");

            alert.showAndWait();
    	}
    }
    
    @FXML
    private void handleSetReminderEvent() {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();

        if (selectedEvent == null) {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Brak wybranego zdarzenia");
            alert.setHeaderText("Wybierz zdarzenie");

            alert.showAndWait();
        }
        else if(LocalDateTime.of(selectedEvent.getDate(), selectedEvent.getTime())
        		.isBefore(LocalDateTime.now())) {
        	Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("B³¹d");
            alert.setHeaderText("Nie mo¿na ustawiæ przypomnienia");
            alert.setContentText("Termin zdarzenia up³yn¹³");

            alert.showAndWait();
        }
        else if(selectedEvent.getReminder()!=0){
        	Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("B³¹d");
            alert.setHeaderText("To wydarzenie ma ju¿ przypisane przypomnienie");

            alert.showAndWait();
        }
        else {
        	boolean okClicked = mainApp.showReminderDialog(selectedEvent);
            if (okClicked) {
                showEventDetails(selectedEvent);
            }
        }

    }
    
    @FXML
    private void handleDeleteReminderEvent() {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();

        if (selectedEvent == null) {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Brak wybranego zdarzenia");
            alert.setHeaderText("Wybierz zdarzenie");

            alert.showAndWait();
        }
        else if(selectedEvent.getReminder()==0) {
        	Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("B³¹d");
            alert.setHeaderText("Wybrane zdarzenie nie ma przypisanego przypomnienia");

            alert.showAndWait();
        }
        else {
        	ObservableList<Reminder> tmp = FXCollections.observableArrayList();
        	
        	mainApp.getReminderData().forEach(reminder -> {
        		if(reminder.getEvent()==selectedEvent){
        			tmp.add(reminder);
        		}
        	});
        	
        	if(tmp.size()!=0) {
    			for(int i=0; i<mainApp.getReminderData().size(); i++){
    				for(int j=0; j<tmp.size(); j++) {
    					if(mainApp.getReminderData().get(i)==tmp.get(j)){
    						mainApp.getReminderData().get(i).getTimer().cancel();
    						mainApp.getReminderData().get(i).getTimer().purge();
    						mainApp.getReminderData().remove(i);
    						selectedEvent.setReminder(0);
    					}
    				}
    			}
    		}
        	
        	showEventDetails(selectedEvent);
        }

    }

  
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        
        FilteredList<Event> filteredData = new FilteredList<>(mainApp.getEventData(), p -> true);
        
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(event -> {
                // Jesli puste, wyswietl wszystko
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();

                if (event.getPlace().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filtr pasuje
                }
                return false; // Nie pasuje
            });
        });
        
        
            filterDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(event -> {
               
                if (newValue == null) {
                    return true;
                }
                
                if (DateUtil.format(event.getDate()).contains(DateUtil.format(newValue))) {
                    return true;
                }
                return false;
            });
        });
            
            timeFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(event -> {
                    // Jesli puste, wyswietl wszystko
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    if (TimeUtil.format(event.getTime()).contains(newValue)) {
                        return true; // Filtr pasuje
                    }
                    return false; // Nie pasuje
                });
            });
        
        SortedList<Event> sortedData = new SortedList<>(filteredData);
        
        sortedData.comparatorProperty().bind(eventTable.comparatorProperty());
        
        
        eventTable.setItems(sortedData);
    }
    
}