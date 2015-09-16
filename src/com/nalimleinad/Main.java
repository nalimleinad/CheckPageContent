package com.nalimleinad;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Main {

    public static void main(String[] args) throws Exception {
        int delay = 5000;
        int period = 5000;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int tryNumber = 1;

            public void run() {
                System.out.println("Pozadavek cislo: " + tryNumber);
                try {
                    checkPageContent("http://www.fakaheda.eu/herni-servery/minecraft-free-server-zdarma");
                    tryNumber++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delay, period);
    }

    public static void checkPageContent(String url) throws Exception {
        // File appDir = new File(System.getProperty("user.dir"));
        // URI uri = new URI(appDir.toURI()+"/raw/failpis.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Main.class.getResource("/raw/failpis.wav"));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);

        try {

            // Parse the URL into various parts: protocol, host, port, filename.
            // First check the URL String
            if (!url.substring(7).contains("/")) {
                url = url.concat("/");
            }
            int end = url.substring(7, url.length()).indexOf("/");
            String host = url.substring(7, end + 7), filename;

            if (url.length() > end + 9) {
                filename = url.substring(end + 7, url.length());
            } else {
                filename = "/";
            }

            // Establish connection
            Socket socket = new Socket(host, 80);

            // Get input and output streams for the socket.
            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            PrintWriter wrServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Send the HTTP GET command to the Web server, specifying the file.
            wrServer.println("GET " + filename + " HTTP/1.1");
            wrServer.println("Host: " + host);
            // Followed by newline.
            wrServer.println("");
            // Send it to the output steam which is connected to the server.
            wrServer.flush();

            String line;
            // Read the html reply.
            while ((line = rd.readLine()) != null) {
                sb.append(line + '\n');
            }

            // Check if the html code contains text of the required format.
            if (sb.toString().toLowerCase().contains("0 / 100")) {
                System.out.println("Zadne volne sloty");
            } else {
                clip.start();
                System.out.println("Volne sloty na serveru");
            }
            socket.close();

        }
        // Handling of exceptions
        catch (Exception e) {
            System.out.println("Chyba!");
            System.err.println(e);
        }
    }
}
