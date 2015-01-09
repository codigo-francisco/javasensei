package javasensei.util;

/**
 *
 * @author Rock
 */
public class EnumHelper {
    public static String toString(String name){        
        return name.substring(0,1).concat(name.substring(1).toLowerCase());
    }
}
