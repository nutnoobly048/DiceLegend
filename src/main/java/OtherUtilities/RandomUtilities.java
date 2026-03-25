package OtherUtilities;

import java.util.Random;

public class RandomUtilities {
    public static final Random R = new Random();

    public static int randomInt() {
        // Random r = new Random();
        return Math.abs(R.nextInt());
    }

    public static int randomInt(int end) {
        // Random r = new Random();
        return R.nextInt(end);
    }

    public static int randomIntDigits(int digits) {
        // Random r = new Random();
        return Math.abs(R.nextInt((int) Math.pow(10, digits - 1), (int) Math.pow(10, digits)));
    }

    
    
}
