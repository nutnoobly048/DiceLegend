package OtherUtilities;

import java.util.Random;

public class RandomUtilities {
    public static int randomInt() {
        Random r = new Random();
        return Math.abs(r.nextInt());
    }

    public static int randomIntDigits(int digits) {
        Random r = new Random();
        return Math.abs(r.nextInt((int) Math.pow(10, digits - 1), (int) Math.pow(10, digits)));
    }
    
}
