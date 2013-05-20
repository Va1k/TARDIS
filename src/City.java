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
    private TimeZone timezone;

    public City(String country, String name, TimeZone timezone) {
        //lel
        this.country = country;
        this.name = name;
        this.timezone = timezone;
    }

    public String getCountry() {
        return this.country;
    }

    public String getName() {
        return this.name;
    }

    public TimeZone getTimezone() {
        return this.timezone;
    }

    public String[] convertTime() {
        // Finds out what the current time/date is.
        Date currentTime = new Date();

        // Creates a date format
        DateFormat date = new SimpleDateFormat("dd, MMMM, yyyy");
        DateFormat normal = new SimpleDateFormat("hh:mm:ssa");
        DateFormat military = new SimpleDateFormat("HH:mm:ss");
        normal.setTimeZone(this.timezone);
        military.setTimeZone(this.timezone);

        // Formats the date in the currently selected city's timezone
        // Also returns both the normal & military formatted times in an array.
        return new String[]{date.format(currentTime), normal.format(currentTime), military.format(currentTime)};
    }


}
