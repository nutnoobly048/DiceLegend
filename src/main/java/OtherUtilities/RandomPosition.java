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

        resultAllPosition();
        banPosition.add(0);
        banPosition.add(99);

    }

    public static void resultAllPosition(){

        // ArrayList<Integer> portalArrayList = new ArrayList<>();
        // ArrayList<Integer> itemArrayList = new ArrayList<>();

        int head;
        int tail;

        for ( int j = 0; j < 7; j++ ){

            do{
                head = RandomUtilities.randomInt(100);
            }while(banPosition.contains(head));

            do{
                tail = RandomUtilities.randomInt(100);
            }while(head == tail || banPosition.contains(tail));

            banPosition.add(head); banPosition.add(tail);

            resultPortalPositionString += (String.valueOf(head) + "," + String.valueOf(tail) + ",");
            
        }
        resultPortalPositionString = resultPortalPositionString.replaceAll(",$", "");

        int positionItems;

        for ( int j = 0; j < 10; j++ ){

            do{
                positionItems = RandomUtilities.randomInt(100);
            }while(banPosition.contains(positionItems));

            banPosition.add(positionItems);

            resultItemPositionString += (String.valueOf(positionItems) + ",");
            
        }

        resultItemPositionString = resultItemPositionString.replaceAll(",$", "");

        int positionEvents;

        for ( int j = 0; j < 10; j++ ){

            do{
                positionEvents = RandomUtilities.randomInt(100);
            }while(banPosition.contains(positionEvents));

            resultEventPositionString += (String.valueOf(positionEvents) + ",");

        }
        resultEventPositionString = resultEventPositionString.replaceAll(",$", "");

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
