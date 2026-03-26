package objectClass;

import Gameplay.Cell;
import ServiceInterface.CellAttribute;

import java.util.EnumSet;

public class Board {


//    public static int[][] coordinatesMysteriousJungle = { //old position
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
            {510, 980}, {610, 980}, {710, 980}, {810, 980}, {910, 980}, {1010, 980}, {1110, 980}, {1210, 980}, {1310, 980}, {1410, 980},
            {1410, 880}, {1310, 880}, {1210, 880}, {1110, 880}, {1010, 880}, {910, 880}, {810, 880}, {710, 880}, {610, 880}, {510, 880},
            {510, 780}, {610, 780}, {710, 780}, {810, 780}, {910, 780}, {1010, 780}, {1110, 780}, {1210, 780}, {1310, 780}, {1410, 780},
            {1410, 680}, {1310, 680}, {1210, 680}, {1110, 680}, {1010, 680}, {910, 680}, {810, 680}, {710, 680}, {610, 680}, {510, 680},
            {510, 580}, {610, 580}, {710, 580}, {810, 580}, {910, 580}, {1010, 580}, {1110, 580}, {1210, 580}, {1310, 580}, {1410, 580},
            {1410, 480}, {1310, 480}, {1210, 480}, {1110, 480}, {1010, 480}, {910, 480}, {810, 480}, {710, 480}, {610, 480}, {510, 480},
            {510, 380}, {610, 380}, {710, 380}, {810, 380}, {910, 380}, {1010, 380}, {1110, 380}, {1210, 380}, {1310, 380}, {1410, 380},
            {1410, 280}, {1310, 280}, {1210, 280}, {1110, 280}, {1010, 280}, {910, 280}, {810, 280}, {710, 280}, {610, 280}, {510, 280},
            {510, 180}, {610, 180}, {710, 180}, {810, 180}, {910, 180}, {1010, 180}, {1110, 180}, {1210, 180}, {1310, 180}, {1410, 180},
            {1410, 80}, {1310, 80}, {1210, 80}, {1110, 80}, {1010, 80}, {910, 80}, {810, 80}, {710, 80}, {610, 80}, {510, 80}
    };

    //Board number - 1 = Array Index Number
    //{3, 58} => เมื่อเหยียบ 4 จะพาไป 59
    public static int[][] destinationMysteriousJungle = {{1,49}, {2,49}, {3, 49}, {4,49}, {5,49}, {6,49}, {7, 49}};
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
