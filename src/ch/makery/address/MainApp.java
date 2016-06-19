package ch.makery.address;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ch.makery.address.model.Event;
import ch.makery.address.model.EventListWrapper;
import ch.makery.address.model.Reminder;
import ch.makery.address.util.CalendarUtil;
import ch.makery.address.view.AboutDialogController;
import ch.makery.address.view.EventEditDialogController;
import ch.makery.address.view.EventOverviewController;
import ch.makery.address.view.ReminderDialogController;
import ch.makery.address.view.RootLayoutController;
import javafx.scene.layout.BackgroundFill;



public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Event> eventData = FXCollections.observableArrayList();
    private ObservableList<Reminder> reminderData = FXCollections.observableArrayList();

    public MainApp() {
    	eventData.add(new Event("Papie¿", LocalDate.of(2016, 10, 23), LocalTime.of(21, 37)));
        eventData.add(new Event("Katowice", LocalDate.of(2015, 9, 30), LocalTime.of(13, 45)));
        eventData.add(new Event("Radom", LocalDate.of(2016, 8, 13), LocalTime.of(18, 15)));
        eventData.add(new Event("Ko³obrzeg", LocalDate.of(2012, 1, 27), LocalTime.of(17, 23)));
        eventData.add(new Event("Wroc³aw", LocalDate.of(2016, 2, 23), LocalTime.of(8, 7)));
        eventData.add(new Event("Toruñ", LocalDate.of(2015, 3, 11), LocalTime.of(12, 59)));
        eventData.add(new Event("Zakopane", LocalDate.of(2013, 4, 7), LocalTime.of(0, 30)));
        eventData.add(new Event("Sosnowiec", LocalDate.of(2014, 5, 18), LocalTime.of(9, 10)));
        eventData.add(new Event("Uczelnia", LocalDate.of(2016, 12, 31), LocalTime.of(23, 35)));
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Organizer");
        this.primaryStage.setResizable(false);

        initRootLayout();
        showEventOverview();

        setEventFilePath(null);
    }

    //Inicjalizacja RootLayout

    public void initRootLayout() {
        try {
            // Wczytanie RootLayout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Pokazanie sceny
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        

    }

    // Pokazuje EventOverview w Root Layout

    public void showEventOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/EventOverview.fxml"));
            AnchorPane eventOverview = (AnchorPane) loader.load();

            // Wycentrowanie
            rootLayout.setCenter(eventOverview);

            // Daje dostep do MainApp
            EventOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showEventEditDialog(Event event) {
        try {
            // Laduje plik xml i tworzy scene dla okna dialogowego
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/EventEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Tworzenie sceny
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Dodawanie/edycja");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            EventEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setEvent(event);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showAboutDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/AboutDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("O programie");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            AboutDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean showReminderDialog(Event event) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ReminderDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Przypomnienie");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            // Set the person into the controller.
            ReminderDialogController controller = loader.getController();
            controller.setMainApp(this);
            controller.setDialogStage(dialogStage);
            controller.setEvent(event);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Returns the person file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     * 
     * @return
     */
    public File getEventFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     * 
     * @param file the file or null to remove the path
     */
    public void setEventFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("Organizer - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("Organizer");
        }
    }
    
    public void loadEventDataFromFileXML(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(EventListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            EventListWrapper wrapper = (EventListWrapper) um.unmarshal(file);

            eventData.clear();
            eventData.addAll(wrapper.getEvents());

            // Save the file path to the registry.
            setEventFilePath(file);
            


        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("B³¹d");
            alert.setHeaderText("Nie mo¿na wczytaæ danych");
            alert.setContentText("Nie mo¿na wczytaæ danych z pliku:\n" + file.getPath());

            alert.showAndWait();
        }
    }
    
    public void saveEventDataToFileXML(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(EventListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our person data.
            EventListWrapper wrapper = new EventListWrapper();
            wrapper.setEvents(eventData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setEventFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("B³¹d");
            alert.setHeaderText("Nie mo¿na zapisaæ danych");
            alert.setContentText("Nie mo¿na zapisaæ danych do pliku:\n" + file.getPath());

            alert.showAndWait();
        }
    }
    
    public void saveEventDataToFileICS(File file){

        try {

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(CalendarUtil.ConvertToiCalendarFormat(eventData));
            bw.close();

        } catch (Exception e) {
        	Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("B³¹d");
            alert.setHeaderText("Nie mo¿na zapisaæ danych");
            alert.setContentText("Nie mo¿na zapisaæ danych do pliku:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ObservableList<Event> getEventData() {
        return eventData;
    }
    
    public ObservableList<Reminder> getReminderData() {
        return reminderData;
    }
    
    public void setEventData(ObservableList<Event> eventData) {
        this.eventData=eventData;
    }



    public static void main(String[] args) {
        launch(args);
    }
}