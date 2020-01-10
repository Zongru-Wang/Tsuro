package game.tile;

/**
 * The Abstract class that represent the rotation of Objects.
 * In this project, the Port and Tile are rotatable. So we have
 * Class Port extends Rotatable<Port>  and
 * Class Tile extends Rotatable<Tile>
 */
public abstract class Rotatable<E extends Rotatable<E>> {
    private static final String ANGLE_SHOULD_BE_90_MULTIPLES = "The degree should be the multiples of 90 degree";
    private static final String INVALID_ANGLE = "Invalid angle";

    /**
     * Handle the clock wise 90 degree rotation.
     * Return a new rotated object since we want to keep things immutable.
     *
     * @return A new Object that has been rotated from the old Object
     *         by 90 degree.
     */
    public E rotateClockwise() {
        return rotate(90);
    }

    /**
     * Represent the counter clockwise 90 degree rotation
     * @return A new Object that has been rotated from the old Object
     *         by counter clockwise 90 degree.
     */
    public E rotateCounterClockwise() {
        return rotate(270);
    }

    /**
     * Given an int represent the how many times of 90 degree clockwise rotation on an Object,
     * And return the new Object that rotate clockwise of the given times.
     * @param x        The times of clockwise rotation on object
     * @return         The new Object that rotate the given time 90 degree clockwise
     */
    protected abstract E rotateXTimes(int x);


    /**
     * Given a degree of rotation and return the new Object rotates the given degree
     * @param degrees   The degree of rotation of an object, the value must be divisible by 90.
     * @return          The new Object that rotated by the given degree
     * @throws IllegalArgumentException  Throw IllegalArgumentException if the given degree is not divisible by 90.
     */
    public E rotate(int degrees) throws IllegalArgumentException {
        int rotationCount;
        if (Math.abs(degrees) % 90 != 0)
            throw new IllegalArgumentException(ANGLE_SHOULD_BE_90_MULTIPLES);
        if (degrees >= 0) {
            rotationCount = degrees % 360 / 90 ;
        }  else {
            rotationCount = (4 - (-degrees % 360 / 90)) % 4 ;
        }
        return rotateXTimes(rotationCount);
    }

    /**
     * Get the angular difference between two Object in same type. Return the angular difference in int
     * represent the degree.
     * @param other Another object that compare to the current object
     * @return The angular difference between two object in int
     * @throws IllegalArgumentException if can't get the angular difference since two objects can't be same by
     *                                  rotation.
     */
    public int angularDifference(E other) {
        for (int degree = 0; degree < 360; degree += 90) {
            if (other.equals(this.rotate(degree))) {
                return degree;
            }
        }
        throw new IllegalArgumentException(INVALID_ANGLE);
    }
}
