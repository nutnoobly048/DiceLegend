package OtherUtilities;

import java.util.HashSet;

import objectClass.Board;

public class RandomPortalPosition {
    
    public static HashSet<Integer> banPosition = new HashSet<>();

    static {

        int[] banPositionList = Board.itemTileMysteriousJungle;
        for (int i = 0; i < banPositionList.length; i++ ){
            banPosition.add(banPositionList[i]);
        }
        banPosition.add(0);
        banPosition.add(99);

    }

    public static String resultRandomPortalPosition(){

        String resultString = "";
        int head;
        int tail;
        for ( int j = 0; j < 7; j++ ){

            do{
                head = RandomUtilities.randomInt(100);
            }while(banPosition.contains(head));

            do{
                tail = RandomUtilities.randomInt(100);
            }while(head == tail || banPosition.contains(tail));

            resultString += (String.valueOf(head) + "," + String.valueOf(tail) + ",");
            
        }
        resultString = resultString.replaceAll(",$", "");
        System.out.println(resultString);
        return resultString;

    }

    public static int[][] convertToArrayInt(String resultString){

        String[] parts = resultString.split(",");

        int pairCount = parts.length / 2;
        int[][] result = new int[pairCount][2];

        for (int i = 0; i < pairCount; i++) {
            result[i][0] = Integer.parseInt(parts[i * 2]);
            result[i][1] = Integer.parseInt(parts[i * 2 + 1]);
        }

        return result;

    }

}
