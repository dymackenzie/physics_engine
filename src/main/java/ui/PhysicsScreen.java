package ui;

import model.Entity;
import model.PhysicsEngine;

import javax.swing.*;
import java.awt.*;

/**
 *  A panel for displaying the main physics interface and where all the physics happens.
 */
public class PhysicsScreen extends JPanel {

    private PhysicsEngine physicsEngine;
    private JLabel info;

    // EFFECTS: constructor for this screen.
    //          sets background and info label.
    public PhysicsScreen() {
        super();
        setBackground(Color.white);
        info = new JLabel();
        add(info);
    }

    // setter
    public void setPhysicsEngine(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;
    }

    // EFFECTS: paints all entities in drawing and sets the info
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        info.setText("right-click for circle - left-click for box ---- "
                + "number of Entities: " + physicsEngine.getEntities().size());
        for (Entity entity : physicsEngine.getEntities()) {
            drawEntity(entity, g); // draws all entities
        }
    }

    // MODIFIES: this
    // EFFECTS: draws either the circle or the rectangle.
    private void drawEntity(Entity entity, Graphics g) {
        Graphics2D g2 = (Graphics2D)g.create();
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHints(rh);
        g2.setColor(Color.black);
        switch (entity.getEntityType()) {
            case CIRCLE:
                g2.setColor(Color.darkGray);
                g2.drawOval(
                        (int)entity.getPosition().getComponentX(),
                        (int)entity.getPosition().getComponentY(),
                        (int)entity.getRadius() * 2,
                        (int)entity.getRadius() * 2);
                break;
            case BOX:
                g2.drawRect(
                        (int)entity.getPosition().getComponentX(),
                        (int)entity.getPosition().getComponentY(),
                        (int)entity.getWidth(),
                        (int)entity.getHeight());
                break;
        }
    }

}
