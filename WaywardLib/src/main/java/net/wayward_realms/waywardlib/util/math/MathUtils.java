package net.wayward_realms.waywardlib.util.math;

/**
 * Contains a variety of math related methods to make life easier.
 */
public class MathUtils {

    /**
     * Makes an approximate estimate on the square root
     *
     * @param indouble the double of which the square root will be approximated.
     * @return the square root.
     */
    public static double fastsqrt(double indouble) {
        return Double.longBitsToDouble(((Double.doubleToLongBits(indouble) - (1l << 52)) >> 1) + (1l << 61));
    }
}