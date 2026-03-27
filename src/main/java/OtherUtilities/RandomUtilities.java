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

    public static int randomPlusMinus(int start, int range){
        if (range < 10) throw new IllegalArgumentException("range must be >= 10");

        int realRange = R.nextInt(range - 10 + 1) + 10;

        int min = Math.max(1, start - realRange);
        int max = Math.min(98, start + realRange);

        return R.nextInt(max - min + 1) + min;
    }

    public static int randomIntDigits(int digits) {
        // Random r = new Random();
        return Math.abs(R.nextInt((int) Math.pow(10, digits - 1), (int) Math.pow(10, digits)));
    }

    
    
}
