/*  Name: Baron Ping-Yeh Hsieh
 *  PennKey: bpyhsieh
 *  Execution: N/A, this class is meant to be used by other classes
 *
 *  A class that represents the fish projectile in
 *  Furious Flying Fish. Can update its own position based
 *  on velocity and time, and can compute whether
 *  it overlaps a given Target.
 *
 */

public class Fish {
    // The position, velocity, and radius members of the fish.
    private double xPos, yPos, xVel, yVel, radius;

    /**
     * How many more times the player can throw the fish
     * before losing the game.
     */
    private int numThrowsRemaining;

    /**
     * Initialize the fish's member variables
     * with the same names as the inputs to those values.
     * Initializes the fish's velocity components to 0.
     */
    public Fish(double xPos, double yPos, double radius, int numThrowsRemaining) {
        // TODO
        this.xPos = xPos;
        this.yPos = yPos;
        this.radius = radius;
        this.numThrowsRemaining = numThrowsRemaining;
        xVel = 0;
        yVel = 0;
    }

    /**
     * Draws a Circle centered at the fish's position
     * with a radius equal to the fish's radius.
     * Additionally, draws a triangular fin and a
     * circular eye somewhere on the circle to make
     * the fish look more like a fish. Additional details
     * are up to your discretion.
     * Also draws the fish's remaining throws 0.1 units
     * above its circular body.
     */
    public void draw() {
        PennDraw.setPenColor(PennDraw.BLUE);
        PennDraw.filledCircle(xPos, yPos, radius);
        PennDraw.filledPolygon(xPos, yPos, xPos - 0.3, yPos + 0.3, xPos - 0.3, 
        yPos - 0.3);
        PennDraw.setPenColor(PennDraw.BLACK);
        PennDraw.filledCircle(xPos + 0.1, yPos + 0.1, 0.03);
        PennDraw.text(xPos, yPos + 0.35 , "" + numThrowsRemaining);
        
    }

    /**
     * Draw the line representing the fish's initial velocity
     * when the player is clicking and dragging the mouse.
     */
    public void drawVelocity() {
        PennDraw.line(xPos, yPos, xPos + xVel, yPos + yVel);
    }

    /**
     * Set xPos and yPos to 1.0,
     * set xVel and yVel to 0.0.
     */
    public void reset() {
        xPos = 1.0;
        yPos = 1.0;
        xVel = 0.0;
        yVel = 0.0;
    }

    /**
     * Compute the fish's initial velocity as the
     * vector from the mouse's current position to
     * the fish's current position. This will be used
     * in mouse listening mode to update the launch
     * velocity.
     */
    public void setVelocityFromMousePos() {
        xVel = xPos - PennDraw.mouseX();
        yVel = yPos - PennDraw.mouseY();
    }

    /**
     * Given the change in time, compute the fish's
     * new position and new velocity.
     */
    public void update(double timeStep) {
        xPos = xPos + (xVel * timeStep);
        yPos = yPos + (yVel * timeStep);
        yVel = yVel - (0.25 * timeStep);
        System.out.println(xPos + " " + yPos);
    }

    /**
     * A helper function used to find the distance
     * between two 2D points. Remember to use the
     * Pythagorean Theorem.
     */
    private static double distance(double x1, double y1, double x2, double y2) {
        double pointDistance = Math.sqrt((x2 - x1) * (x2 - x1) + 
        (y2 - y1) * (y2 - y1));
        return pointDistance;
    }

    /**
     * Given a Target, determine if the fish should
     * test for collision against it. If the fish
     * *should* see if it collides with the target,
     * then perform that test. If the fish collides,
     * then decrease the target's HP by 1 set its
     * hitThisShot field to be true.
     */
    public void testAndHandleCollision(Target t) {
        if (t.getHitPoints() > 0) {
            if (distance(xPos, yPos, t.getXPos(), t.getYPos()) < (radius + 
            t.getRadius())) {
                t.setHitThisShot(true);
            }
        }
    }

    // Reduce numThrowsRemaining by 1.
    public void decrementThrows() {
        numThrowsRemaining--;
    }

    /**
     * Getter functions that return a copy
     * of the indicated member variable.
     */
    public double getXpos() {
        return xPos;
    }

    public double getYpos() {
        return yPos;
    }

    public double getRadius() {
        return radius;
    }

    public int getNumThrowsRemaining() {
        return numThrowsRemaining;
    }
}
