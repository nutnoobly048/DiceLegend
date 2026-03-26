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

    public static int[][] resultRandomPortalPosition(){
        
        int[][] result = new int[3][2];
        int head;
        int tail;
        for ( int j = 0; j < 3; j++ ){

            do{
                head = RandomUtilities.randomInt(100);
            }while(banPosition.contains(head));

            do{
                tail = RandomUtilities.randomInt(100);
            }while(head == tail || banPosition.contains(tail));

            result[j][0] = head;
            result[j][0] = tail;

        }
        
        return result;

    }

}
