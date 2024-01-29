/*  Name: Baron Ping-Yeh Hsieh
 *  PennKey: bpyhsieh
 *  Execution: N/A, this class is meant to be used by other classes
 *
 *  A class that represents a target to be hit in
 *  Furious Flying Fish. Can update its own position based
 *  on velocity and time.
 */

public class Target {

    // variables for width and height of screen
    private double width, height;

    // Position and radius
    private double xPos, yPos, radius;

    // Velocity components
    private double xVel, yVel;

    /**
     * When a target's hit points reach zero,
     * it has been destroyed by the fish.
     */
    private int hitPoints;

    // Track if target has been hit this shot.
    private boolean hitThisShot;

    /**
     * Given a position, a radius, a velocity, and a number of hit points,
     * construct a Target.
     */
    public Target(double width, double height, double xPos, double yPos,
            double radius, double xVel, double yVel, int hitPoints) {
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
        this.radius = radius;
        this.xVel = xVel;
        this.yVel = yVel;
        this.hitPoints = hitPoints;
        hitThisShot = false;
    }

    /**
     * Draw a circle centered at the target's position
     * with a radius equal to the target's radius.
     * Only draw a Target if it has more than zero
     * hit points.
     */
    public void draw() {
        if (hitPoints > 0) {
            PennDraw.setPenColor(PennDraw.RED);
            PennDraw.filledCircle(xPos, yPos, radius);
            PennDraw.filledPolygon(xPos, yPos + radius, xPos, yPos - radius, 
            xPos + 2 * radius, yPos);
            PennDraw.filledPolygon(xPos, yPos + radius, xPos, yPos - radius, 
            xPos - 2 * radius, yPos);
            PennDraw.setPenColor(PennDraw.ORANGE);
            PennDraw.filledPolygon(xPos + radius, yPos, xPos - radius, yPos, 
            xPos, yPos + 2 * radius);
            PennDraw.filledPolygon(xPos + radius, yPos, xPos - radius, yPos, 
            xPos, yPos - 2 * radius);
            PennDraw.setPenColor(PennDraw.BLACK);
            PennDraw.filledCircle(xPos + radius / 2, yPos + radius / 2, radius / 4);
            PennDraw.filledCircle(xPos - radius / 2, yPos + radius / 2, radius / 4);
            PennDraw.line(xPos - radius / 2, yPos - radius / 2, xPos + radius / 2, 
            yPos - radius / 2);
            PennDraw.text(xPos, yPos, "" + hitPoints);
        }
    }

    /**
     * Given the change in time, update the target's
     * position based on its x and y velocity. When
     * a target is completely offscreen horizontally,
     * its position should wrap back around to the opposite
     * horizontal side. For example, if the target moves off the
     * right side of the screen, its xPos should be set to the
     * left side of the screen minus the target's radius.
     * The same logic should apply to the target's vertical
     * position with respect to the vertical screen boundaries.
     */
    public void update(double timeStep) {
        xPos += xVel * timeStep;
        yPos += yVel * timeStep;
        if (xVel < 0 && xPos + radius <= 0) {
            xPos = width + radius;
        }
        
        if (xVel > 0 && xPos - radius >= width) {
            xPos = 0 - radius;
        }

        if (yVel > 0 && yPos - radius >= height) {
            yPos = 0 - radius;
        }

        if (yVel < 0 && yPos + radius <= 0) {
            yPos = height + radius;
        }
    }

    // Decrement the target's hit points by 1.
    public void decreaseHP() {
        --hitPoints;
    }

    /**
     * Setter function for whether or not target hit this round.
     */
    public void setHitThisShot(boolean hit) {
        hitThisShot = hit;
    }


    /**
     * Return whether or not this target is hit this round.
     */
    public boolean isHit() {
        return hitThisShot;
    }

    /**
     * A function to change the radius of the target that would then be 
     * implemented in the arena update to change the target's radius 
     * (make it smaller) upon collision with fish
     */
    public void changeRadiusCollision() {
            radius /= 1.15;
    }


    /**
     * Getter functions that return a copy of the
     * indicated member variable.
     */
    public int getHitPoints() {
        return hitPoints;
    }

    public double getXPos() {
        return xPos;
    }

    public double getYPos() {
        return yPos;
    }

    public double getRadius() {
        return radius;
    }

}
