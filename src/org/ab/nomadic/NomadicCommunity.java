package org.ab.nomadic;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.stream.IntStream;

//import processing.video.*;
import processing.core.*;
import ddf.minim.*;

public class NomadicCommunity extends PApplet
{

    private static final Logger log = Logger.getLogger(NomadicCommunity.class
            .getName());
    private static final int DEFAULT_TEXT_SIZE = 20;
    private static final int DEFAULT_BACKGROUND = 255;
    private static final int MIN_TEXT_SIZE = 10;
    private static final int MAX_TEXT_SIZE = 100;
    private static final int MIN_NR_TEXTS = 10;
    private static final int MAX_NR_TEXTS = 40;
    private static final String TEXT_PATH = "../txt/nomadic_text";

    private Minim minim;
    private AudioPlayer player;
    private int WIDTH, HEIGHT;

    private float vol = 0.1f;
    private int IDX = 0;

    private ArrayList<String> nomadicText = new ArrayList<String>();

    public void init()
    {
        if (frame != null)
        {
            frame.removeNotify();
            frame.setResizable(false);
            frame.setUndecorated(true);
            frame.setAlwaysOnTop(true);
            println("frame is at " + frame.getLocation());
            frame.addNotify();
        }
        super.init();
    }
    
    public void initNomadicText()
    {
        try (BufferedReader br = new BufferedReader(new FileReader(TEXT_PATH));)
        {
            for(String line; (line = br.readLine()) != null; ) {
                nomadicText.add(line.trim());
            }
        } catch (IOException e)
        {
            log.severe("Failed to process the text: " + TEXT_PATH);
            e.printStackTrace();
        }
    }

    // public void movieEvent(Movie movie) {
    // mov.read();
    // }

    public void setup()
    {
        initNomadicText();
        // mov = new Movie(this, PATH_TO_SAVE_MOVIE);
        // mov.play();
        // ***** figure out the display environment ****/
        GraphicsEnvironment environment = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsDevice devices[] = environment.getScreenDevices();

        if (devices.length > 1)
        {
            WIDTH = devices[1].getDisplayMode().getWidth();
            HEIGHT = devices[1].getDisplayMode().getHeight();
            println("Adjusting animation size to " + WIDTH + "x" + HEIGHT
                    + " b/c of 2ndary display");
        } else
        {
            WIDTH = devices[0].getDisplayMode().getWidth();
            HEIGHT = devices[0].getDisplayMode().getHeight();
            println("Adjusting animation size to " + WIDTH + "x" + HEIGHT
                    + " to fit primary display");
        }

        size(WIDTH, HEIGHT);
        colorMode(HSB, 360, 100, 100);

        minim = new Minim(this);
        // player = minim.loadFile(PATH_TO_AUDIO);
        // player.shiftVolume(0, 1, 1000);;
        // player.loop(1);

    }

    public boolean sketchFullScreen()
    {
        return true;
    }

    public void keyPressed()
    {

        switch (keyCode)
        {
        case KeyEvent.VK_1:
            IDX = 0;
            break;
        case KeyEvent.VK_2:
            IDX = 1;
            break;
        case KeyEvent.VK_3:
            IDX = 2;
            break;
        case KeyEvent.VK_4:
            IDX = 3;
            break;
        case KeyEvent.VK_5:
            IDX = 4;
            break;
        case KeyEvent.VK_6:
            IDX = 5;
            break;
        case KeyEvent.VK_7:
            IDX = 6;
            break;
        case KeyEvent.VK_8:
            IDX = 7;
            break;
        default:

        }

        if (keyCode == UP)
        {
            IDX = (IDX + 1) % nomadicText.size();
            if (IDX >= nomadicText.size())
                IDX = nomadicText.size() - 1;
            log.info("Up!");
            //player.setVolume(vol);
            vol += 0.1f;
        } else if (keyCode == DOWN)
        {
            IDX = (IDX - 1) % nomadicText.size();
            if (IDX < 0)
                IDX = 0;
            log.info("Down!");
        }

    }

    private String getDisplayText(final String currentText)
    {
        final int substrbegin = (int) random(0, currentText.length() - 1);
        final int substrend = (int) random(substrbegin + 1,
                currentText.length() - 1);
        return currentText.substring(substrbegin, substrend);
    }

    public void draw()
    {
        background(0);

        final String currentText = nomadicText.get(IDX);
        if (keyPressed)
        {
            IntStream.range(MIN_NR_TEXTS, MAX_NR_TEXTS).forEach(i ->
            {
                final float x = random(0, width / 2);
                final float y = random(0, height);
                final float randomSize = random(MIN_TEXT_SIZE, MAX_TEXT_SIZE);
                textSize(randomSize);
                fill(mouseX * 360 / width, mouseY * 100 / height, 100);
                text(getDisplayText(currentText), x, y);
            });

        } else
        {
            cursor(TEXT);
            textSize(DEFAULT_TEXT_SIZE);
            fill(DEFAULT_BACKGROUND);
            text(currentText, width / 2, height / 2, width / 2.7f, height / 2);
        }
    }

    public static void main(String args[])
    {
        final int primary_display = 0;
        int primary_width;
        final GraphicsEnvironment environment = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        final GraphicsDevice devices[] = environment.getScreenDevices();
        String location;
        if (devices.length > 1)
        {
            primary_width = devices[0].getDisplayMode().getWidth();
            location = "--location=" + primary_width + ",0";

        } else
        {
            location = "--location=0,0";
        }

        String display = "--display=" + primary_display + 1;
        PApplet.main(new String[]
        { location, "--hide-stop", display, "org.ab.nomadic.NomadicCommunity" });

    }

    public static void __main(String args[])
    {
        PApplet.main(new String[]
        { "--present", "org.ab.nomadic.NomadicCommunity" });
    }

}
