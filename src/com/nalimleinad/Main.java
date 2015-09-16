package com.nalimleinad;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Main {

    public static void main(String[] args) throws Exception {
        int delay = 0;
        int period = 5000;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int tryNumber = 1;

            public void run() {
                System.out.println("Pozadavek cislo: " + tryNumber);
                try {
                    checkPageContent(new URL("http://www.fakaheda.eu/herni-servery/minecraft-free-server-zdarma"));
                    tryNumber++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delay, period);
    }

    public static void checkPageContent(URL url) throws Exception {
        // File appDir = new File(System.getProperty("user.dir"));
        // URI uri = new URI(appDir.toURI()+"/raw/failpis.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Main.class.getResource("/raw/failpis.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);

        try {

            // Get input stream for the url.
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();

            String line;
            // Read the html reply.
            while ((line = br.readLine()) != null) {
                sb.append(line + '\n');
            }

            // Check if the html code contains text of the required format.
            if (sb.toString().toLowerCase().contains("0 / 100")) {
                System.out.println("Zadne volne sloty");
            } else {
                clip.start();
                System.out.println("Volne sloty na serveru");
            }

        }
        // Handling of exceptions
        catch (Exception e) {
            System.out.println("Chyba!");
            System.err.println(e);
        }
    }
}
