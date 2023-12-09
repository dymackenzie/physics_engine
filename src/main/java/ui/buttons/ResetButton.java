package ui.buttons;

import model.PhysicsEngine;

import javax.swing.*;

public class ResetButton extends JButton {

    private PhysicsEngine physicsEngine;

    public ResetButton(String label) {
        super(label);
        this.physicsEngine = null;
        addActionListener(e -> reset());
    }

    // setter
    public void setPhysicsEngine(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
    }

    // MODIFIES: entities
    // EFFECTS: resets and deletes all non-static entities
    private void reset() {
        physicsEngine.resetEntities();
    }

}
