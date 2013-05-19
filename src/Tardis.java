import au.com.bytecode.opencsv.CSVReader;

import java.applet.Applet;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created with IntelliJ IDEA.
 * Author: Andreas
 * Date  : 18/05/13
 * Time  : 2:37 PM
 */

public class Tardis extends Applet {

    public void init() {
        setLayout(new BorderLayout());
        readDatabase();

        Choice lCountries = new Choice();
        Choice lCities = new Choice();

        Panel controls = new Panel(new BorderLayout());
        controls.add(lCountries,BorderLayout.NORTH);
        controls.add(lCities,BorderLayout.SOUTH);
        add(controls,BorderLayout.NORTH);



    }

    public void paint(Graphics g) {
        // No more fuzzy text! Anti-aliasing on!
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public void readDatabase() {
        URL database = this.getClass().getResource("Database.csv");
        /**
         * Creates a new CSVReader via the OpenCSV package http://opencsv.sourceforge.net/
         */
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(database.getFile()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /**
         * Slight bit of magic.
         *
         * Creates an 'ArrayList' of cities that is populated by a while loop with all the cities from the .csv file.
         *
         * Now that we effectively have an array full of cities and their details the rest is pretty simple.
         */
        String[] row;
        ArrayList<City> cities = new ArrayList<City>();
        try {
            while((row = csvReader.readNext()) != null) {
                cities.add(new City(row[0],row[1],row[2],TimeZone.getTimeZone(row[3])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * We don't need the csvReader anymore, closes it.
         */
        try {
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
