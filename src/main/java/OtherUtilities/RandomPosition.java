package OtherUtilities;

import java.util.ArrayList;
import java.util.HashSet;

import objectClass.Board;

public class RandomPosition {

    public static HashSet<Integer> banPosition = new HashSet<>();

    public static String resultPortalPositionString = "";
    public static String resultItemPositionString = "";
    public static String resultEventPositionString = "";

    public static void resultAllPosition() {

        resultPortalPositionString = "";
        resultItemPositionString = "";
        resultEventPositionString = "";

        banPosition.clear();
        banPosition.add(0);
        banPosition.add(99);

        int head, tail;
                                //early Game //Mid_game //End_game
        int[][]   tileRanges   = {{1,30},{31,75},{80,98}};

        int[][]   snakeRanges  = {{5,15},{15,25},{25,35}};
        int[][]   ladderRanges = {{15,34},{10,25},{8,18}};

        int[]     snakeCounts  = { 1, 3, 2 };
        int[]     ladderCounts = { 1, 3, 2 };

        for (int p = 0; p < 3; p++) {
            int tileMin = tileRanges[p][0], tileMax = tileRanges[p][1];

            for (int s = 0; s < snakeCounts[p]; s++) {
                do { head = tileMin + RandomUtilities.R.nextInt(tileMax - tileMin + 1); }
                while (banPosition.contains(head));

                do { tail = randomSnakeTail(head, snakeRanges[p]); }
                while (tail >= head || banPosition.contains(tail));

                banPosition.add(head); banPosition.add(tail);
                resultPortalPositionString += head + "," + tail + ",";
            }

            for (int l = 0; l < ladderCounts[p]; l++) {
                do { head = tileMin + RandomUtilities.R.nextInt(tileMax - tileMin + 1); }
                while (banPosition.contains(head));

                do { tail = randomLadderTail(head, ladderRanges[p]); }
                while (tail <= head || banPosition.contains(tail));

                banPosition.add(head); banPosition.add(tail);
                resultPortalPositionString += head + "," + tail + ",";
            }
        }

        resultPortalPositionString = resultPortalPositionString.replaceAll(",$", "");


        int positionItems;

        for ( int j = 0; j < 12; j++ ){

            do{
                positionItems = RandomUtilities.randomInt(70) + 10;
            }while(banPosition.contains(positionItems));

            banPosition.add(positionItems);

            resultItemPositionString += (String.valueOf(positionItems) + ",");

        }

        resultItemPositionString = resultItemPositionString.replaceAll(",$", "");

        int positionEvents;

        for ( int j = 0; j < 12; j++ ){

            do{
                positionEvents = RandomUtilities.randomInt(70) + 10;
            }while(banPosition.contains(positionEvents));

            resultEventPositionString += (String.valueOf(positionEvents) + ",");

        }
        resultEventPositionString = resultEventPositionString.replaceAll(",$", "");

    }

    private static int randomLadderTail(int head, int[] range) {

        return RandomUtilities.randomPlusMinus(head, range[1]);
    }

    private static int randomSnakeTail(int head, int[] range) {
        return RandomUtilities.randomPlusMinus(head, range[1]);
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