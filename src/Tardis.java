import au.com.bytecode.opencsv.CSVReader;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Author: Andreas
 * Date  : 18/05/13
 * Time  : 2:37 PM
 */

public class Tardis extends Applet {

    private ArrayList<City> cities = new ArrayList<City>();
    private Choice lCountries = new Choice();
    private Choice lCities = new Choice();
    private Label description = new Label("");
    private City selectedCity;

    public void init() {
        setLayout(new BorderLayout());
        readDatabase();

        Panel controls = new Panel(new BorderLayout());
        controls.add(lCountries,BorderLayout.NORTH);
        controls.add(lCities,BorderLayout.CENTER);
        controls.add(description,BorderLayout.SOUTH);
        add(controls, BorderLayout.NORTH);

        getCountries();

        lCountries.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                getCities();
                description.setText("");
            }
        });

        lCities.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                getCity();
                description.setText(selectedCity.getComment());
                convertTime();
            }
        });


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
    } // End of readDatabase()

    public void getCountries() {
        lCountries.add("Select an area!");
        ArrayList<String> addedCountries = new ArrayList<String>();
        for (City s : cities) {
            if(!addedCountries.contains(s.getCountry())) {
                lCountries.add(s.getCountry());
                addedCountries.add(s.getCountry());
            }
        }
    } // End of getCountries()

    public void getCities() {
        lCities.removeAll();
        lCities.add("Select a city!");
        for (City s : cities)
            if (s.getCountry().equals(lCountries.getSelectedItem()))
                lCities.add(s.getName());
    } // End of getCities()

    public void getCity() {
        for (City s : cities)
            if (s.getName() == lCities.getSelectedItem())
                selectedCity = s;
    } // End of getCities()

    public void convertTime() {
        System.out.println("Timezone Detected:\t\t\t" + TimeZone.getDefault());

        System.out.println(selectedCity.getCountry() + ", " + selectedCity.getName() + "'s " +  "Timezone:\t\t\t" + selectedCity.getTimezone());

    }
}
