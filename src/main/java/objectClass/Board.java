package objectClass;

import Gameplay.Cell;
import ServiceInterface.CellAttribute;

public class Board {

    private final Cell[] cells = new Cell[100];

    public Board(int[][] positions) {
        for (int i = 0; i < 100; i++) {

            cells[i] = new Cell(i);

        }
    }

    public void addAttribute(int index, CellAttribute attribute) {

        cells[index].attributes.add(attribute);

    }

}
