package ui.buttons;

import model.Entity;
import model.PhysicsEngine;
import persistence.JsonReader;
import ui.PhysicsUI;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class LoadButton extends JButton {

    private final JsonReader jsonReader;
    private PhysicsEngine physicsEngine;

    public LoadButton(String label) {
        super(label);
        this.jsonReader = new JsonReader(PhysicsUI.JSON_STORE);
        this.physicsEngine = null;
        addActionListener(e -> loadFile());
    }

    // setter
    public void setPhysicsEngine(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
    }

    // MODIFIES: entities
    // EFFECTS: reads JSON file and loads all non-static entities into environment
    private void loadFile() {
        try {
            ArrayList<Entity> entities = jsonReader.read();
            entities.addAll(PhysicsUI.staticEntities); // adds all non-static entities to the static base
            physicsEngine.getEntities().clear();
            for (Entity entity : entities) {
                physicsEngine.getEntities().add(entity);
            }
            System.out.println("Loaded state from: " + PhysicsUI.JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + PhysicsUI.JSON_STORE);
        }
    }

}
