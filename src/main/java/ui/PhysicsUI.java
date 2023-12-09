package ui;

import model.Entity;
import model.PhysicsEngine;
import model.helpers.Vector2;
import model.logs.Event;
import model.logs.EventLog;
import ui.buttons.LoadButton;
import ui.buttons.ResetButton;
import ui.buttons.SaveButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Main UI class that handles both user inputs and rendering of the physics
 * Help taken from the DrawingPlayer class for the Swing graphics
 */
public class PhysicsUI extends JFrame {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;
    public static final String JSON_STORE = "./data/entities.json";
    public static final ArrayList<Entity> staticEntities = new ArrayList<>();
    private PhysicsEngine physicsEngine;


    // EFFECTS: constructs PhysicsScreen and runs application
    public PhysicsUI() {
        super("Physics Engine");
        initializeValues();
        initializeGraphics();
    }

    // MODIFIES: this
    // EFFECTS:  sets isRunning to true and sets jsonWriter and jsonReader to the file path
    private void initializeValues() {
        generateStaticEntities();
        this.physicsEngine = new PhysicsEngine(new ArrayList<>(staticEntities));
    }

    // MODIFIES: this
    // EFFECTS: a helper method for generating all static bodies on the screen
    private void generateStaticEntities() {
        Entity b; // initialize demo physics
        b = Entity.createBox(new Vector2(10, 600), 0, 0, 960, 10);
        b.setStatic();
        staticEntities.add(b);
    }

    // MODIFIES: this
    // EFFECTS:  draws the JFrame window where this will operate, and populates the tools to be used
    //           to manipulate this engine
    private void initializeGraphics() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            setLayout(new BorderLayout());
            setMinimumSize(new Dimension(WIDTH, HEIGHT));
            createButtons();
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    JFrame frame = (JFrame)e.getSource();
                    EventLog el = EventLog.getInstance();
                    for (Event next : el) {
                        System.out.println(next);
                    }
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            });
            setLocationRelativeTo(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: a helper method that declares and instantiates all buttons for saving, loading, and resetting.
    private void createButtons() {
        JPanel buttonArea = new JPanel(); // set and load the button area
        buttonArea.setLayout(new GridLayout(1,3));
        buttonArea.setSize(new Dimension(0, 0));
        add(buttonArea, BorderLayout.SOUTH);

        // button for loading file
        LoadButton load = new LoadButton("Load File");
        load.setPhysicsEngine(physicsEngine);
        buttonArea.add(load);

        // button for saving file
        SaveButton save = new SaveButton("Save");
        save.setPhysicsEngine(physicsEngine);
        buttonArea.add(save);

        // button for resetting the instance
        ResetButton reset = new ResetButton("Reset");
        reset.setPhysicsEngine(physicsEngine);
        buttonArea.add(reset);
    }

    // MODIFIES: this
    // EFFECTS: initializes the demo entities and begins the physics engine
    public void start() {
        initializeScreen();
        tick(); // begin physics engine
    }

    // MODIFIES: this
    // EFFECTS: initializes the screen where the physics happens
    private void initializeScreen() {
        MouseListener ml = new MouseListener();
        PhysicsScreen physicsScreen = new PhysicsScreen();
        physicsScreen.setPhysicsEngine(physicsEngine);
        physicsScreen.addMouseListener(ml);
        physicsScreen.addMouseMotionListener(ml);
        add(physicsScreen, BorderLayout.CENTER);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: this is the main loop to handle all physics
    private void tick() {

        float startTime = System.nanoTime() * 0.000000001f; // units are now in seconds
        float accumulator = 0f;
        boolean isRunning = true;

        // this ensures that every action is done in discrete, fixed steps
        while (isRunning) {

            float currentTime = System.nanoTime() * 0.000000001f; // units are now in seconds
            accumulator += currentTime - startTime; // store the time elapsed since the last frame
            startTime = currentTime; // reassign starting frame as a reset
            // clamps the number of times updatePhysics will be called within a game loop,
            // avoids the domino effect of the accumulator getting higher and higher
            if (accumulator > 0.1) {
                accumulator = 0.1f;
            }

            while (accumulator >= PhysicsEngine.DT) {
                physicsEngine.updatePhysics(); // UPDATES PHYSICS
                accumulator -= PhysicsEngine.DT;
            }

            repaint();
        }
    }

    // EFFECTS: if left mouse button is clicked, generate an AABB.
    //          if right mouse button is clicked, generate a circle.
    private void handleMouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            this.physicsEngine.addEntity(Entity.EntityType.BOX, e.getX(), e.getY());
        } else if (SwingUtilities.isRightMouseButton(e)) {
            this.physicsEngine.addEntity(Entity.EntityType.CIRCLE, e.getX(), e.getY());
        }
        repaint();
    }

    private class MouseListener extends MouseAdapter {
        // EFFECTS: forward mouse clicked event to handleMouseClicked
        public void mouseClicked(MouseEvent e) {
            handleMouseClicked(e);
        }
    }

}
