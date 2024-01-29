/*  Name: Baron Ping-Yeh Hsieh
 *  PennKey: bpyhsieh
 *
 *  Execution: java Furious Flying Fish [filename]
 *
 *  Represents the Furious Flying Fish game. Takes a level
 *  description text file and initializes a game arena
 *  with that data, then runs the game until the player
 *  wins or loses.
 *
 */


public class FuriousFish {

    public static void main(String[] args) {
        // set width and height
        PennDraw.setCanvasSize(1000, 500);
        // The game runs at 30 frames per second
        PennDraw.enableAnimation(30);
        // Instantiate an Arena provided with the
        // name of a level description file, e.g.
        // targetFile1.txt
        Arena arena = new Arena(args[0]);
        // Our change in time between frames will be
        // set to 0.2 seconds.
        final double TIME_STEP = 0.2;
        // While the player has neither won nor lost,
        // keep running the game.
        while (!arena.gameOver()) {
            // Update the arena and all of its components,
            // such as moving each target based on its
            // velocity and processing the player's mouse
            // input if appropriate.
            arena.update(TIME_STEP);
            // Draw the updated arena and its components.
            arena.draw();
        }
        // When the while loop has finished, the game is
        // over. The arena will draw either a victory screen
        // or a defeat screen.
        arena.drawGameCompleteScreen();
    }
}
