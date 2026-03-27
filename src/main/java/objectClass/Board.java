package objectClass;

import Gameplay.Cell;
import OtherUtilities.RandomPosition;
import ServiceInterface.CellAttribute;

public class Board {

    public static int[][] defaultPosition = {
            {500, 990}, {600, 990}, {700, 990}, {800, 990}, {900, 990}, {1000, 990}, {1100, 990}, {1200, 990}, {1300, 990}, {1400, 990},
            {1400, 890}, {1300, 890}, {1200, 890}, {1100, 890}, {1000, 890}, {900, 890}, {800, 890}, {700, 890}, {600, 890}, {500, 890},
            {500, 790}, {600, 790}, {700, 790}, {800, 790}, {900, 790}, {1000, 790}, {1100, 790}, {1200, 790}, {1300, 790}, {1400, 790},
            {1400, 690}, {1300, 690}, {1200, 690}, {1100, 690}, {1000, 690}, {900, 690}, {800, 690}, {700, 690}, {600, 690}, {500, 690},
            {500, 590}, {600, 590}, {700, 590}, {800, 590}, {900, 590}, {1000, 590}, {1100, 590}, {1200, 590}, {1300, 590}, {1400, 590},
            {1400, 490}, {1300, 490}, {1200, 490}, {1100, 490}, {1000, 490}, {900, 490}, {800, 490}, {700, 490}, {600, 490}, {500, 490},
            {500, 390}, {600, 390}, {700, 390}, {800, 390}, {900, 390}, {1000, 390}, {1100, 390}, {1200, 390}, {1300, 390}, {1400, 390},
            {1400, 290}, {1300, 290}, {1200, 290}, {1100, 290}, {1000, 290}, {900, 290}, {800, 290}, {700, 290}, {600, 290}, {500, 290},
            {500, 190}, {600, 190}, {700, 190}, {800, 190}, {900, 190}, {1000, 190}, {1100, 190}, {1200, 190}, {1300, 190}, {1400, 190},
            {1400, 90}, {1300, 90}, {1200, 90}, {1100, 90}, {1000, 90}, {900, 90}, {800, 90}, {700, 90}, {600, 90}, {500, 90}
    };

    //Board number - 1 = Array Index Number
    //{3, 58} => เมื่อเหยียบ 4 จะพาไป 59
    public static int[][] destinationMysteriousJungle = {{1,49}, {2,49}, {3, 49}, {4,49}, {5,49}, {6,49}, {7, 49}};
    public static int[] itemTileMysteriousJungle = {49,50,51,52,53,54,55,56,57,58,59,60}; //mock data
    public static int[] eventTileMysteriousJungle = {}; //mock data

    public static int[][] destinationCyroGard = {};
    public static int[] itemTileCyroGard = {};
    public static int[] eventTileCyroGard = {};




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
