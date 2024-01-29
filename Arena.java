/*  Name: Baroon Ping-Yeh Hsieh
 *  PennKey:  bpyhsieh
 *  Execution: N/A, this class is meant to be used by other classes
 *
 *  A class representing the arena in which the Furious Flying Fish
 *  game takes place. Keeps track of the game's Fish and
 *  Targets and receives the player's input to control the fish.
 *
 */

import java.util.ArrayList;

public class Arena {
    // The width and height of the PennDraw screen
    private double width, height;

    // All the targets in the Arena
    private ArrayList<Target> targets;

    // The one and only fish in the game
    private Fish fish;

    /**
     * Whether the game is currently listening for
     * the player's mouse input, or letting the fish
     * fly. Begins as true.
     */
    private boolean mouseListeningMode;

    /**
     * Tells the program if the user was pressing
     * the mouse in the previous update call. Lets
     * the program know if the user has just released
     * the mouse because mouseWasPressedLastUpdate will
     * be true, but PennDraw.mousePressed() will be false
     * in the current update call. This enables the game
     * to transition from the mouse listening state into
     * the fish flight state.
     */
    private boolean mouseWasPressedLastUpdate;

    /**
     * Given a file that describes the contents of the
     * Arena, parse the file and initialize all member
     * variables of the Arena.
     * The file will be in the following format:
     * numTargets width height
     * fish.numThrows
     * target0.xPos target0.yPos target0.radius target0.xVel target0.yVel
     * target0.hitPoints
     * target1.xPos... etc.
     *
     * Remember to set mouseListeningMode to true to start.
     */
    public Arena(String filename) {
        In inStream = new In(filename);
        // TODO : parse given file and initialize all member variables.
        int numTargets = inStream.readInt();
        width = inStream.readInt();
        height = inStream.readInt();
        PennDraw.setXscale(0, width);
        PennDraw.setYscale(0, height);
        int numThrows = inStream.readInt();
        fish = new Fish(1, 1, 0.25, numThrows);
        targets = new ArrayList<Target>();
        for (int i = 0; i < numTargets; i++) {
            Target currTarget = new Target(width, height, inStream.readDouble(), 
            inStream.readDouble(), inStream.readDouble(), inStream.readDouble(), 
            inStream.readDouble(), inStream.readInt());
            targets.add(currTarget);
        }
        inStream.close();
        mouseListeningMode = true;
        mouseWasPressedLastUpdate = false;
    }

    /**
     * 1. Clear the screen
     * 2. Draw each target
     * 3. Draw the fish
     * 4. If in mouse listening mode and
     * the mouse was pressed last update,
     * draw the fish's velocity as a line.
     * 5. Advance PennDraw.
     */
    public void draw() {
        PennDraw.clear();
        for (int i = 0; i < targets.size(); i++) {
            Target currTarget = targets.get(i);
            currTarget.draw();
        }
        fish.draw();
        if (mouseWasPressedLastUpdate == true && mouseListeningMode == true) {
            fish.drawVelocity();
        }
        PennDraw.advance();
    }

    /**
     * Returns true when all targets' hit points are 0.
     * Returns false in any other scenario.
     */
    private boolean didPlayerWin() {
        int targetsDestroyed = 0;
        for (int i = 0; i < targets.size(); i++) {
            Target currTarget = targets.get(i);
            if (currTarget.getHitPoints() == 0) {
                targetsDestroyed++;
            }
        }
        return targetsDestroyed == targets.size();
    }

    /**
     * Returns true when the fish's remaining throw count is 0
     * when the game is in mouse-listening mode.
     * Returns false in any other scenario.
     */
    private boolean didPlayerLose() {
        return mouseListeningMode && fish.getNumThrowsRemaining() == 0 && 
        !didPlayerWin(); 
    }

    /**
     * Returns true when either the win or lose
     * condition is fulfilled.
     * Win: All targets' hit points are 0.
     * Lose: The fish's remaining throw count reaches 0.
     * Additionally, the game must be in mouse listening
     * mode for the player to have lost so that the fish
     * can finish its final flight and potentially hit
     * the last target(s).
     */
    public boolean gameOver() {
        return didPlayerWin() || didPlayerLose();
    }

    /**
     * Update each of the entities within the arena.
     * 1. Call each Target's update function
     * 2. Check the game state (mouse listening or fish moving)
     * and invoke the appropriate functions for the fish.
     */
    public void update(double timeStep) {
        for (int i = 0; i < targets.size(); i++) {
            Target currTarget = targets.get(i);
            currTarget.update(timeStep);
        }

        if (mouseListeningMode == true) {
            // TODO: Handle mouse-listening mode

            /**
             * If the mouse is currently pressed, then
             * set mouseWasPressedLastUpdate to true, and
             * call fish.setVelocityFromMousePos.
             */
            if (PennDraw.mousePressed() == true) {
                mouseWasPressedLastUpdate = true;
                fish.setVelocityFromMousePos();
            } 

            /**
             * If the mouse is NOT currently pressed, AND
             * mouseWasPressedLastUpdate is currently true,
             * that means the player has just released the
             * mouse button, and the game should transition
             * from mouse-listening mode to fish-flight mode.
             * Decrement the number of throws remaining.
             */
            if (PennDraw.mousePressed() == false && 
            mouseWasPressedLastUpdate == true) {
                mouseWasPressedLastUpdate = false;
                mouseListeningMode = false;
                fish.decrementThrows();
            }

            /**
             * If the mouse is NOT currently pressed, AND
             * mouseWasPressedLastUpdate is currently true,
             * that means the player has just released the
             * mouse button, and the game should transition
             * from mouse-listening mode to fish-flight mode.
             * Decrement the number of throws remaining.
             */

        } else {
            // TODO: Handle fish-flight mode
            System.out.println("working");
            fish.update(timeStep);
            for (int i = 0; i < targets.size(); i++) {
                Target currTarget = targets.get(i);
                fish.testAndHandleCollision(currTarget);
            }

            if (fishIsOffscreen() == true) {
                for (int i = 0; i < targets.size(); i++) {
                    Target currTarget = targets.get(i);
                    if (currTarget.isHit()) {
                        currTarget.decreaseHP();
                        currTarget.changeRadiusCollision();
                        currTarget.setHitThisShot(false);
                    }
                }
                fish.reset();
                mouseListeningMode = true;
            }
        }
    }

    /**
     * A helper function for the Arena class that lets
     * it know when to reset the fish's position and velocity
     * along with the game state.
     * Returns true when the fish is offscreen to the left, right,
     * or bottom. However, the fish is allowed to go above the top
     * of the screen without resetting.
     */
    private boolean fishIsOffscreen() {
        return (fish.getXpos() + fish.getRadius() <= 0) || (fish.getXpos() - 
        fish.getRadius() >= width) || (fish.getYpos() + fish.getRadius() <= 0);
    }

    /**
     * Draws either the victory or loss screen.
     * If all targets have 0 hit points, the player has won.
     * Otherwise they have lost.
     */
    public void drawGameCompleteScreen() {
        PennDraw.clear();
        if (didPlayerLose()) {
            PennDraw.setFontSize(50);
            PennDraw.setFontBold();
            PennDraw.text(0.5 * width, 0.5 * height, "You have lost…");
        }
        if (didPlayerWin()) {
            PennDraw.setFontSize(50);
            PennDraw.setFontBold();
            PennDraw.text(0.5 * width, 0.5 * height, "You Win!");
        }
        PennDraw.advance();
    }
}
