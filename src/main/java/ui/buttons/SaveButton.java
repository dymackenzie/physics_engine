package ui.buttons;

import model.Entity;
import model.PhysicsEngine;
import persistence.JsonWriter;
import ui.PhysicsUI;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SaveButton extends JButton {

    private final JsonWriter jsonWriter;
    private PhysicsEngine physicsEngine;

    public SaveButton(String label) {
        super(label);
        this.jsonWriter = new JsonWriter(PhysicsUI.JSON_STORE);
        this.physicsEngine = null;
        addActionListener(e -> save());
    }

    // setter
    public void setPhysicsEngine(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
    }

    // EFFECTS: saves all non-static entities and quits the engine, closing the window
    private void save() {
        try {
            jsonWriter.open();
            // only keeps the non-static entities
            ArrayList<Entity> entityArrayList = new ArrayList<>();
            for (Entity entity : physicsEngine.getEntities()) {
                if (!entity.isStatic()) {
                    entityArrayList.add(entity);
                }
            }
            jsonWriter.writeEntities(entityArrayList);
            jsonWriter.close();
            System.out.println("Saved state to: " + PhysicsUI.JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + PhysicsUI.JSON_STORE);
        }
    }

}
