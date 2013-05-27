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

        Panel controls = new Panel(new BorderLayout());
        controls.setBackground(Color.decode("#FAFAFA"));
        controls.add(comboRegion, BorderLayout.NORTH);
        controls.add(comboLocation,BorderLayout.CENTER);
        add(controls, BorderLayout.NORTH);

        Label userTimeZone = new Label (" Your time zone: " + TimeZone.getDefault().getDisplayName() + " | " + TimeZone.getDefault().getID());
        userTimeZone.setForeground(Color.decode("#EEEEEE"));
        add(userTimeZone, BorderLayout.SOUTH);

        Button info = new Button("?");
        controls.add(info, BorderLayout.EAST);

        getCountries();
        getCities();
        getCity();

        comboRegion.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                getCities();
                comboLocation.select(0);
                getCity();
            }
        });

        comboLocation.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                getCity();
            }
        });
    }

    public void start() {
        if (animator == null) {
            animator = new Thread(this);
            animator.start();
        }
    } // End of start

    public void stop() {
        if (animator != null) {
            animator = null;
        }
    } // End of Stop

    public void run() {
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

        // Make some fonts
        Font header = new Font("Trebuchet MS",Font.BOLD, 35);
        Font normal = new Font("Trebuchet MS",Font.PLAIN, 25);
        Font timezone = new Font("Trebuchet MS",Font.PLAIN,25);

        if(selectedCity == null) {
            return;
        }

        g2.setFont(header);
        g2.drawString(selectedCity.getCountry() + ", " + selectedCity.getName(), 10, 90);

        g2.setFont(timezone);
        g2.setColor(Color.decode("#AAAAAA"));
        g2.drawString(selectedCity.getTimezone().getDisplayName(),10, 120);

        g2.setFont(normal);
        g2.setColor(Color.black);
        g2.drawString(date,10,150);
        g2.drawString(normalTime, 10, 180);
        g2.setColor(Color.GRAY);
        g2.drawString(militaryTime,10,210);
    } // End of paint

    public void readDatabase() {
        URL database = this.getClass().getResource("Database.csv");
        /**
         * Creates a new CSVReader via the OpenCSV package http://opencsv.sourceforge.net/
         */
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(database.getPath()));
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
                cities.add(new City(row[0],row[1],TimeZone.getTimeZone(row[3])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * We don't need the csvReader anymore, close it.
         */
        try {
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // End of readDatabase()

    public void getCountries() {
        ArrayList<String> addedCountries = new ArrayList<String>();
        for (City s : cities) {
            if(!addedCountries.contains(s.getCountry())) {
                comboRegion.add(s.getCountry());
                addedCountries.add(s.getCountry());
            }
        }
    } // End of getCountries()

    public void getCities() {
        comboLocation.removeAll();
        for (City s : cities)
            if (s.getCountry().equals(comboRegion.getSelectedItem()))
                comboLocation.add(s.getName());
    } // End of getCities()

    public void getCity() {
        for (City s : cities)
            if (s.getName().equals(comboLocation.getSelectedItem()))
                selectedCity = s;
    } // End of getCities()

}
