package ch.makery.address.model;

import java.awt.Toolkit;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Reminder {
	
	private Timer timer;
	
	public Timer getTimer() {
		return timer;
	}

	private Event event;
	
	public Event getEvent() {
		return event;
	}
	
	public Reminder(Event event){
		this.event = event;
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(event.getDate().getYear(), event.getDate().getMonthValue(), 
				event.getDate().getDayOfMonth(), event.getTime().getHour(), 
				event.getTime().getMinute(), 0);
		calendar.add(Calendar.MINUTE, -event.getReminder());
		calendar.add(Calendar.MONTH, -1);
		Date time = calendar.getTime();

		timer = new Timer();
		timer.schedule(new RemindTask(), time);
	}
	

    class RemindTask extends TimerTask {
        public void run() {
        	Platform.runLater(new Runnable() {
        		public void run() {
        			Toolkit.getDefaultToolkit().beep();
        			Alert alert = new Alert(AlertType.INFORMATION);
        			alert.setTitle("Przypomnienie!");
        			alert.setHeaderText(null);
        			alert.setContentText("Przypomnienie o wydarzeniu w miejscu: " + event.getPlace() + "\nData i czas: "
        			+ event.getDate().toString() + " " + event.getTime().toString() + "\nOpis: " + event.getDescription());
            
        			alert.showAndWait();
        			timer.cancel();
        			event.setReminder(0);
        		}
        	});
        }
    }
}
