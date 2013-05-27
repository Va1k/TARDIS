import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created with IntelliJ IDEA.
 * Author: Andreas
 * Date  : 18/05/13
 * Time  : 2:57 PM
 *
 * City class for 'city' objects.
 *
 * Does a bulk of the work throughout the application.
 */
public class City {
    private String region;
    private String name;
    private TimeZone timezone;

    // City constructor
    public City(String region, String name, TimeZone timezone) {
        this.region = region;
        this.name = name;
        this.timezone = timezone;
    }

    // Returns the location's region
    public String getRegion() {
        return this.region;
    }

    // Returns the location's name
    public String getName() {
        return this.name;
    }

    // Returns the timezone
    public TimeZone getTimezone() {
        return this.timezone;
    }

    // Converts time
    public String[] convertTime() {
        // Finds out what the current time/date is.
        Date currentTime = new Date();

        // Creates date formats for use
        DateFormat date = new SimpleDateFormat("dd, MMMM, yyyy");
        DateFormat normal = new SimpleDateFormat("hh:mm:ssa");
        DateFormat military = new SimpleDateFormat("HH:mm:ss");

        // Sets the timezone of the date formats to this' (the object's timezone)
        date.setTimeZone(this.timezone);
        normal.setTimeZone(this.timezone);
        military.setTimeZone(this.timezone);

        // Returns both the normal & military formatted times as well as the date in an array.
        return new String[]{date.format(currentTime), normal.format(currentTime), military.format(currentTime)};
    }


}
