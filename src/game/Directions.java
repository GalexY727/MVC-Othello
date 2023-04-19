package game;
/**
 * Set up HashMap with the compass rose directions and
 * pair with a vector that will implement the change
 * in position
 *
 * @author Mr. Jaffe
 * @version 1.0
 */
public class Directions {
	
    /**
	 * The function takes a compass point as input and returns a Position object representing the
	 * corresponding vector.
	 * 
	 * @param compassPoint a String representing a compass point (N, NE, E, SE, S, SW, W, NW)
	 * @return The method is returning a Position object that represents a vector in a specific
	 * direction based on the input compass point. If the input is not a valid compass point, the
	 * method returns null.
	 */
	public static Position getVector(String compassPoint) {
        switch (compassPoint) {
            case "N":  return new Position(-1,0);
            case "NE": return new Position(-1,1);
            case "E":  return new Position(0,1);
            case "SE": return new Position(1,1);
            case "S":  return new Position(1,0);
            case "SW": return new Position(1,-1);
            case "W":  return new Position(0,-1);
            case "NW": return new Position(-1,-1);
        }
        return null;
    }

	
    /**
	 * The function returns an array of strings representing directions.
	 * 
	 * @return An array of strings representing the directions: "N", "NE", "E", "SE", "S", "SW", "W",
	 * and "NW".
	 */
	public static String[] getDirections() {
        return new String[]{"N","NE","E","SE","S","SW","W","NW"};
    }

}
