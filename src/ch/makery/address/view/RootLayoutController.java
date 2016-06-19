package ch.makery.address.view;

import java.io.File;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import ch.makery.address.MainApp;
import ch.makery.address.util.DatabaseUtil;

public class RootLayoutController {

    private MainApp mainApp;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    @FXML
    private void handleNew() {
        mainApp.getEventData().clear();
        mainApp.setEventFilePath(null);
    }

    /**
     * Opens a FileChooser to let the user select an address book to load.
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Pliki XML (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadEventDataFromFileXML(file);
        }
    }

    /**
     * Saves the file to the person file that is currently open. If there is no
     * open file, the "save as" dialog is shown.
     */
    @FXML
    private void handleSave() {
        File eventFile = mainApp.getEventFilePath();
        if (eventFile != null) {
            mainApp.saveEventDataToFileXML(eventFile);
        } else {
            handleSaveAs();
        }
    }

    /**
     * Opens a FileChooser to let the user select a file to save to.
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Pliki XML (*.xml)", "*.xml");
        FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter(
                "Pliki ICS (*.ics)", "*.ics");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.getExtensionFilters().add(extFilter2);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null && fileChooser.getSelectedExtensionFilter()==extFilter) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.saveEventDataToFileXML(file);  
        }
        else if (file != null && fileChooser.getSelectedExtensionFilter()==extFilter2) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".ics")) {
                file = new File(file.getPath() + ".ics");
            }
            mainApp.saveEventDataToFileICS(file);  
        }
    }
    
    @FXML
    private void handleImportToDatabase() throws SQLException {
    	
    	FileChooser fileChooser = new FileChooser();
    	// Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Pliki bazy danych Microsoft Access (*.accdb)", "*.accdb");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (file != null) {
            try {
				DatabaseUtil.setList(DatabaseUtil.getConnection(file.getAbsolutePath()), mainApp.getEventData());
				Alert alert = new Alert(AlertType.INFORMATION);
	            alert.setTitle("Sukces");
	            alert.setHeaderText("Zapisano dane");
	            alert.setContentText("Zapisano dane do pliku:\n" + file.getPath());

	            alert.showAndWait();
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
	            alert.setTitle("B³¹d");
	            alert.setHeaderText("Nie mo¿na zapisaæ danych");
	            alert.setContentText("Nie mo¿na zapisaæ danych do pliku:\n" + file.getPath());

	            alert.showAndWait();
			}
        }
    }
    
    @FXML
    private void handleExportFromDatabase() throws SQLException {
    	
    	FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Plik bazy danych Microsoft Access (*.accdb)", "*.accdb");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (file != null) {
            try {
				mainApp.getEventData().setAll(DatabaseUtil.getList(DatabaseUtil.getConnection(file.getAbsolutePath())));
				Alert alert = new Alert(AlertType.INFORMATION);
	            alert.setTitle("Sukces");
	            alert.setHeaderText("Odczytano dane");
	            alert.setContentText("Odczytano dane z pliku:\n" + file.getPath());

	            alert.showAndWait();
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR);
	            alert.setTitle("B³¹d");
	            alert.setHeaderText("Nie mo¿na odczytaæ danych");
	            alert.setContentText("Nie mo¿na odczytaæ danych z pliku:\n" + file.getPath());

	            alert.showAndWait();
			}
        }
    }
    
    @FXML
    private void handleSaveAsICS() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Pliki ICS (*.ics)", "*.ics");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".ics")) {
                file = new File(file.getPath() + ".ics");
            }
            mainApp.saveEventDataToFileICS(file);
        }
    }

   
    @FXML
    private void handleAbout() {
       mainApp.showAboutDialog();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
    
}