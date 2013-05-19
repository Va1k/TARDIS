import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created with IntelliJ IDEA.
 * Author: Andreas
 * Date  : 18/05/13
 * Time  : 2:57 PM
 */
public class City {
    private String country;
    private String name;
    private String comment;
    private TimeZone timezone;

    public City(String country, String name, String comment, TimeZone timezone) {
        //lel
        this.country = country;
        this.name = name;
        this.comment = comment;
        this.timezone = timezone;
    }

    public String getCountry() {
        return this.country;
    }

    public String getName() {
        return this.name;
    }

    public String getComment() {
        return this.comment;
    }

    public TimeZone getTimezone() {
        return this.timezone;
    }

    public String[] convertTime() {
        // Finds out what the current time/date is.
        Date currentTime = new Date();

        // Creates a date format
        DateFormat normal = new SimpleDateFormat("dd-MMMM-yyyy hh:mm:ssa");
        DateFormat military = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss");
        normal.setTimeZone(this.timezone);
        military.setTimeZone(this.timezone);

        // Formats the date in the currently selected city's timezone
        // Also returns both the normal & military formatted times in an array.
        return new String[]{normal.format(currentTime), military.format(currentTime)};
    }


}
