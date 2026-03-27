package OtherUtilities;

import java.util.ArrayList;
import java.util.HashSet;

import objectClass.Board;

public class RandomPosition {

    public static HashSet<Integer> banPosition = new HashSet<>();

    public static String resultPortalPositionString = "";
    public static String resultItemPositionString = "";
    public static String resultEventPositionString = "";

    static {
        banPosition.add(0);
        banPosition.add(99);
    }

    public static void resultAllPosition(){

        resultPortalPositionString = "";
        resultItemPositionString = "";
        resultEventPositionString = "";

        int head;
        int tail;

        for ( int j = 0; j < 10; j++ ){

            do{
                head = RandomUtilities.randomInt(100);
            }while(banPosition.contains(head));

            // old: tail could be anywhere ±20, no direction guarantee
            // do{
            //     tail = RandomUtilities.randomPlusMinus(head, 20);
            // }while(head == tail || banPosition.contains(tail));
            boolean isLadder = RandomUtilities.R.nextBoolean();
            do {
                tail = isLadder ? randomLadderTail(head) : randomSnakeTail(head);
            } while (head == tail || banPosition.contains(tail));

            banPosition.add(head); banPosition.add(tail);

            resultPortalPositionString += (String.valueOf(head) + "," + String.valueOf(tail) + ",");

        }
        resultPortalPositionString = resultPortalPositionString.replaceAll(",$", "");

        int positionItems;

        for ( int j = 0; j < 10; j++ ){

            do{
                // old: full board 0-99, items could land in early or late game
                // positionItems = RandomUtilities.randomInt(100);
                positionItems = RandomUtilities.randomInt(70) + 10;
            }while(banPosition.contains(positionItems));

            banPosition.add(positionItems);

            resultItemPositionString += (String.valueOf(positionItems) + ",");

        }

        resultItemPositionString = resultItemPositionString.replaceAll(",$", "");

        int positionEvents;

        for ( int j = 0; j < 10; j++ ){

            do{
                // old: full board 0-99, events could land in early or late game
                // positionEvents = RandomUtilities.randomInt(100);
                positionEvents = RandomUtilities.randomInt(70) + 10;
            }while(banPosition.contains(positionEvents));

            resultEventPositionString += (String.valueOf(positionEvents) + ",");

        }
        resultEventPositionString = resultEventPositionString.replaceAll(",$", "");

    }

    private static int randomLadderTail(int head) {
        int jump = RandomUtilities.R.nextInt(21) + 10;
        return Math.min(98, head + jump);
    }

    private static int randomSnakeTail(int head) {
        int drop = RandomUtilities.R.nextInt(26) + 10;
        return Math.max(1, head - drop);
    }

    public static int[][] convertToArray2DInt(String resultString){

        String[] parts = resultString.split(",");

        int pairCount = parts.length / 2;
        int[][] result = new int[pairCount][2];

        for (int i = 0; i < pairCount; i++) {
            result[i][0] = Integer.parseInt(parts[i * 2]);
            result[i][1] = Integer.parseInt(parts[i * 2 + 1]);
        }

        return result;

    }

    public static int[] convertToArray1DInt(String resultString){

        String[] parts = resultString.split(",");

        int[] result = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }

        return result;

    }

}