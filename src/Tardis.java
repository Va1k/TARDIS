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

public class Tardis extends Applet implements Runnable{

    private ArrayList<City> cities = new ArrayList<City>();
    private Choice comboRegion = new Choice();
    private Choice comboLocation = new Choice();
    private City selectedCity;
    private String normalTime = "";
    private String militaryTime = "";
    private String date = "";
    private Thread animator;

    public void init() {
        setLayout(new BorderLayout());
        readDatabase();

        // Panel for combo-boxes and other controls.
        Panel controls = new Panel(new BorderLayout());
        controls.setBackground(Color.decode("#FAFAFA"));
        controls.add(comboRegion, BorderLayout.NORTH);
        controls.add(comboLocation,BorderLayout.CENTER);
        Button info = new Button("?");
        controls.add(info, BorderLayout.EAST);
        add(controls, BorderLayout.NORTH);

        // Small label down the bottom to tell the user what their timezone is.
        Label userTimeZone = new Label (" Your time zone: " + TimeZone.getDefault().getDisplayName() + " | " + TimeZone.getDefault().getID());
        userTimeZone.setForeground(Color.decode("#EEEEEE"));
        add(userTimeZone, BorderLayout.SOUTH);

        // Runs the collection methods for the first time to populate the applet with data.
        getCountries();
        getCities();
        getCity();

        /**
         * Action listener in a subclass for the region combo-box.
         *
         * Calls:
         *      getCities() - populates the location combo-box with cities within the selected country.
         *      getCity() - The first city is already selected, now use the getCity method to obtain the city's variables.
         */
        comboRegion.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                getCities();
                getCity();
            }
        });

        /**
         * Action listener in a subclass for the location combo-box.
         *
         * Calls:
         *      getCity() - Whenever the location combo-box is changed, get the new city's variables.
         */
        comboLocation.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                getCity();
            }
        });
    } // End of init.

    public void start() {
        // When the applet is started, if the animator thread doesn't exist, create it and start it running.
        if (animator == null) {
            animator = new Thread(this);
            animator.start();
        }
    } // End of start

    public void stop() {
        // When the applet is stopped, if the animator exists, destroy it.
        if (animator != null) {
            animator = null;
        }
    } // End of Stop

    public void run() {
        /**
         * The run() method is called and managed by the 'animator' thread.
         *
         * While the animator is running
         *      If a city has been selected
         *          Grab the date, normal time and military time using the object's convertTime() method
         *          Call the repaint(); method to refresh the applet.
         *
         * Then do nothing for 250ms.
         */
        while(animator.isAlive()) {
            if(selectedCity != null) {
                date = selectedCity.convertTime()[0];
                normalTime = selectedCity.convertTime()[1];
                militaryTime = selectedCity.convertTime()[2];
                repaint();
            }

            try {Thread.sleep(250);} catch (InterruptedException e) {}
        }
    } // End of run

    public void paint(Graphics g) {
        // No more fuzzy text! Anti-aliasing on!
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        // Define some fonts.
        Font header = new Font("Trebuchet MS",Font.BOLD, 35);
        Font normal = new Font("Trebuchet MS",Font.PLAIN, 25);
        Font timezone = new Font("Trebuchet MS",Font.PLAIN,25);

        // To prevent errors the first time the program is run, end the method if there isn't a city selected.
        if(selectedCity == null) {
            return;
        }

        // Header, typically the country.
        g2.setFont(header);
        g2.drawString(selectedCity.getRegion() + ", " + selectedCity.getName(), 10, 90);

        // Timezone of the selected city.
        g2.setFont(timezone);
        g2.setColor(Color.decode("#AAAAAA"));
        g2.drawString(selectedCity.getTimezone().getDisplayName(),10, 120);

        // The date, normal time & military time of the selected country.
        g2.setFont(normal);
        g2.setColor(Color.black);
        g2.drawString(date,10,150);
        g2.drawString(normalTime, 10, 180);
        g2.setColor(Color.GRAY);
        g2.drawString(militaryTime,10,210);
    } // End of paint

    public void readDatabase() {
        // Gets the relative URL to the .csv file with countries, etc entered into it.
        URL database = this.getClass().getResource("Database.csv");

        // Creates a new CSVReader object via the OpenCSV package http://opencsv.sourceforge.net/
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(database.getPath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // Error handling if the file doesn't exist.
        }

        /**
         * Creates an 'ArrayList' of cities that is populated from the .csv file via a while loop.
         *
         * Now that we effectively have an array full of cities and their details the rest is pretty simple.
         */

        // Creates a string array, as the .csv file is incremented through the array is populated with the cells in the row.
        String[] row;

        try {
            while((row = csvReader.readNext()) != null) { // While there are still rows
                cities.add(new City(row[0],row[1],TimeZone.getTimeZone(row[3]))); // Add a new city object to the cities ArrayList, create it by using data from the .csv
            }
        } catch (IOException e) {
            e.printStackTrace(); // Error handling if it can't read the file.
        }

        /**
         * We don't need the csvReader anymore, close it.
         */
        try {
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();  // Error handling
        }
    } // End of readDatabase()

    public void getCountries() {
        /**
         * It's likely that locations will share a region. It doesn't really make sense to have the same region more than once in the drop-down list.
         *
         * So I made an ArrayList which holds regions that have already been added.
         *
         * A for loop then iterates through all the city objects and if the AddedRegions doesn't already contain it, it adds each city's region to the region drop-down list.
         */
        ArrayList<String> addedRegions = new ArrayList<String>();
        for (City s : cities) {
            if(!addedRegions.contains(s.getRegion())) {
                comboRegion.add(s.getRegion());
                addedRegions.add(s.getRegion());
            }
        }
    } // End of getCountries()

    public void getCities() {
        // First clear out all the locations currently in the drop-down list.
        // This needs to be done because if there's a region change the previously selected region's locations would be left behind.
        comboLocation.removeAll();
        /**
         * Iterates through all the city objects, if a city's region variable matches the selected region it's added to the location drop-down list.
         */
        for (City s : cities)
            if (s.getRegion().equals(comboRegion.getSelectedItem()))
                comboLocation.add(s.getName());
    } // End of getCities()

    public void getCity() {
        /**
         * It's important to actually find the city object so its relevant methods can be used
         *
         * All the cities are iterated through in order to find the actual city object that's selected.
         *
         * That city object is then put into the empty selectedCity 'City' object which is used throughout the rest of the code, particularly paint().
         */
        for (City s : cities)
            if (s.getName().equals(comboLocation.getSelectedItem()))
                selectedCity = s;
    } // End of getCities()

}
