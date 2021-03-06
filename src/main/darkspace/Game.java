package main.darkspace;

import main.darkspace.display.Display;
import main.darkspace.graphics.ImageLoader;
import main.darkspace.graphics.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game implements Runnable { //Runnable ermoeglicht das benutzten von Threads

    private Display display;
    public int width, height;
    public String title;

    private boolean running = false;
    private Thread thread;

    private BufferStrategy bufferStrategy;
    private Graphics graphics;

    private BufferedImage testImage;
    private SpriteSheet sheet;

    public Game(String title, int width, int height) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    private void init() {   //initialize methode
        display = new Display(title, width, height);
        testImage = ImageLoader.loadImage("../res/textures/Troll3.png");
        sheet = new SpriteSheet(testImage);

    }

    private void tick() {   //tick methode, prueft jeden tick

    }

    private void render() { //rendert Spielinhalte
        bufferStrategy = display.getCanvas().getBufferStrategy();   //Buffers um sachen zu zeichnen
        if(bufferStrategy == null) {
            display.getCanvas().createBufferStrategy(3);    //neue BufferStrategy mit 3 Buffern
            return;
        }

        graphics = bufferStrategy.getDrawGraphics();

        graphics.clearRect(0, 0, width, height);    //Bildschirm leeren

        //--
        //graphics.drawImage(testImage, 100, 100, null);

        graphics.drawImage(sheet.crop(80,0, 50, 50), 10, 10, null);



        //--

        bufferStrategy.show();  //Zeigt aktuelles Bild der BufferStrategy an
        graphics.dispose(); //Entfernt Grafikobjekte



    }

    public void run() { //run methode
        init();

        while(running) {    //while running true
            tick();
            render();
        }
        stop();
    }

    //Startet einen Thread(Hintergrundprogramm für Gameticks)
    public synchronized void start() {
        if(running) {   //wenn Spiel schon läuft, return
            return;
        }
        running = true; //setzt running auf true
        thread = new Thread(this);  //this = gameclass
        thread.start(); //ruft run() auf
    }

    //Stoppt Thread
    public synchronized  void stop() {
        if(!running) {  //wenn Spiel nicht läuft, return
            return;
        }
        running = false;    //setzt running auf false
        try {
            thread.join();  //stoppt thread
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
