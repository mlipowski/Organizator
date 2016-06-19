package ch.makery.address.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ch.makery.address.util.LocalDateAdapter;
import ch.makery.address.util.LocalTimeAdapter;


public class Event {

    private final StringProperty place;
    private final StringProperty description;
    private final ObjectProperty<LocalDate> date;
    private final ObjectProperty<LocalTime> time;
    private final IntegerProperty reminder;

    /**
     * Default constructor.
     */
    public Event() {
        this(null, null, null);
    }


    public Event(String place, LocalDate date, LocalTime time) {
        this.place = new SimpleStringProperty(place);
        this.date = new SimpleObjectProperty<LocalDate>(date);
        this.time = new SimpleObjectProperty<LocalTime>(time);
        this.description = new SimpleStringProperty("Opis...");
        this.reminder = new SimpleIntegerProperty();
    }

    public String getPlace() {
        return place.get();
    }

    public void setPlace(String place) {
        this.place.set(place);
    }

    public StringProperty placeProperty() {
        return place;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }
    
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    public LocalTime getTime(){
    	return time.get();
    }
    
    public void setTime(LocalTime time){
    	this.time.set(time);
    }
    
    public ObjectProperty<LocalTime> timeProperty(){
    	return time;
    }
    
    public int getReminder() {
		return reminder.get();
	}

	public void setReminder(int reminder) {
		this.reminder.set(reminder);
	}
	
	public IntegerProperty reminderProperty(){
    	return reminder;
    }
}