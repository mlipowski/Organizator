package ch.makery.address.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeUtil {
	
	private static final String TIME_PATTERN = "HH:mm";

    private static final DateTimeFormatter TIME_FORMATTER = 
            DateTimeFormatter.ofPattern(TIME_PATTERN);

    public static String format(LocalTime time) {
        if (time == null) {
            return null;
        }
        return TIME_FORMATTER.format(time);
    }

    public static LocalTime parse(String timeString) {
        try {
            return TIME_FORMATTER.parse(timeString, LocalTime::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }


    public static boolean validTime(String timeString) {
        return TimeUtil.parse(timeString) != null;
    }

}
