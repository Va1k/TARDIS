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

}
