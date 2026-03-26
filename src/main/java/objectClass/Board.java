package objectClass;

import Gameplay.Cell;
import ServiceInterface.CellAttribute;

import java.util.EnumSet;

public class Board {


//    public static int[][] coordinatesMysteriousJungle = { ##old position
//            {554, 990}, {654, 990}, {754, 990}, {854, 990}, {954, 990}, {1054, 990}, {1154, 990}, {1254, 990}, {1354, 990}, {1454, 990},
//            {1454, 890}, {1354, 890}, {1254, 890}, {1154, 890}, {1054, 890}, {954, 890}, {854, 890}, {754, 890}, {654, 890}, {554, 890},
//            {554, 790}, {654, 790}, {754, 790}, {854, 790}, {954, 790}, {1054, 790}, {1154, 790}, {1254, 790}, {1354, 790}, {1454, 790},
//            {1454, 690}, {1354, 690}, {1254, 690}, {1154, 690}, {1054, 690}, {954, 690}, {854, 690}, {754, 690}, {654, 690}, {554, 690},
//            {554, 590}, {654, 590}, {754, 590}, {854, 590}, {954, 590}, {1054, 590}, {1154, 590}, {1254, 590}, {1354, 590}, {1454, 590},
//            {1454, 490}, {1354, 490}, {1254, 490}, {1154, 490}, {1054, 490}, {954, 490}, {854, 490}, {754, 490}, {654, 490}, {554, 490},
//            {554, 390}, {654, 390}, {754, 390}, {854, 390}, {954, 390}, {1054, 390}, {1154, 390}, {1254, 390}, {1354, 390}, {1454, 390},
//            {1454, 290}, {1354, 290}, {1254, 290}, {1154, 290}, {1054, 290}, {954, 290}, {854, 290}, {754, 290}, {654, 290}, {554, 290},
//            {554, 190}, {654, 190}, {754, 190}, {854, 190}, {954, 190}, {1054, 190}, {1154, 190}, {1254, 190}, {1354, 190}, {1454, 190},
//            {1454, 90},  {1354, 90},  {1254, 90},  {1154, 90},  {1054, 90},  {954, 90},  {854, 90},  {754, 90},  {654, 90},  {554, 90}
//    };

    public static int[][] coordinatesMysteriousJungle = { //new position
            {530, 980}, {630, 980}, {730, 980}, {830, 980}, {930, 980}, {1030, 980}, {1130, 980}, {1230, 980}, {1330, 980}, {1430, 980},
            {1430, 880}, {1330, 880}, {1230, 880}, {1130, 880}, {1030, 880}, {930, 880}, {830, 880}, {730, 880}, {630, 880}, {530, 880},
            {530, 780}, {630, 780}, {730, 780}, {830, 780}, {930, 780}, {1030, 780}, {1130, 780}, {1230, 780}, {1330, 780}, {1430, 780},
            {1430, 680}, {1330, 680}, {1230, 680}, {1130, 680}, {1030, 680}, {930, 680}, {830, 680}, {730, 680}, {630, 680}, {530, 680},
            {530, 580}, {630, 580}, {730, 580}, {830, 580}, {930, 580}, {1030, 580}, {1130, 580}, {1230, 580}, {1330, 580}, {1430, 580},
            {1430, 480}, {1330, 480}, {1230, 480}, {1130, 480}, {1030, 480}, {930, 480}, {830, 480}, {730, 480}, {630, 480}, {530, 480},
            {530, 380}, {630, 380}, {730, 380}, {830, 380}, {930, 380}, {1030, 380}, {1130, 380}, {1230, 380}, {1330, 380}, {1430, 380},
            {1430, 280}, {1330, 280}, {1230, 280}, {1130, 280}, {1030, 280}, {930, 280}, {830, 280}, {730, 280}, {630, 280}, {530, 280},
            {530, 180}, {630, 180}, {730, 180}, {830, 180}, {930, 180}, {1030, 180}, {1130, 180}, {1230, 180}, {1330, 180}, {1430, 180},
            {1430, 80}, {1330, 80}, {1230, 80}, {1130, 80}, {1030, 80}, {930, 80}, {830, 80}, {730, 80}, {630, 80}, {530, 80}
    };

    //Board number - 1 = Array Index Number
    //{3, 58} => เมื่อเหยียบ 4 จะพาไป 59
    public static int[][] destinationMysteriousJungle = {{1,55}, {2,56}, {3, 58}, {4,34}, {5,12}, {6,87}, {7, 80}};
    public static int[] itemTileMysteriousJungle = {}; //mock data
    public static int[] eventTileMysteriousJungle = {}; //mock data




    private Cell[] cells;

    public Board(int[][] coordinateSource, int[][] destinations, int[] itemTiles, int[] eventTiles) {
        int totalCells = coordinateSource.length;
        this.cells = new Cell[totalCells];

        for (int i = 0; i < totalCells; i++) {
            int x = coordinateSource[i][0];
            int y = coordinateSource[i][1];

            cells[i] = new Cell(i, x, y);

            if (i == 0) {
                cells[i].attributes = CellAttribute.START_TILE;
            } else if (i == totalCells - 1) {
                cells[i].attributes = CellAttribute.WIN_TILE;
            }
            else if (contains(itemTiles, i)) {
                cells[i].attributes = CellAttribute.ITEM_TILE;
            } else if (contains(eventTiles, i)) {
                cells[i].attributes = CellAttribute.EVENT_TILE;
            }


        }

        for (int[] portal : destinations) {
            int startIndex = portal[0];
            int targetIndex = portal[1];

            if (startIndex >= 0 && startIndex < cells.length) {
                cells[startIndex].destinationIndex = targetIndex;
                if (startIndex < targetIndex) {
                    cells[startIndex].attributes = CellAttribute.LADDER_TILE;
                } else if (startIndex > targetIndex) {
                    cells[startIndex].attributes = CellAttribute.SNAKE_TILE;
                }
            }
        }
    }

    public void addAttribute(int index, CellAttribute attribute) {
        cells[index].attributes = attribute;
    }

    private static boolean contains(int[] arr, int value) {
        for (int v : arr) {
            if (v == value) return true;
        }
        return false;
    }

    public int[] getPositionFromIndex(int index) {
        return cells[index].getPosition();
    }

    public CellAttribute getAttributeFromIndex(int index) {
        return cells[index].attributes;
    }

    public int getBoardSize() {
        return cells.length;
    }

    public int getDestinationFromIndex(int index) {
        if (cells[index].destinationIndex == -1) return index;
        return cells[index].destinationIndex;
    }



    //Mock method
    public int getNearestIndexWithAttribute(CellAttribute att, int startIndex) {
        return 0;
    }
}
