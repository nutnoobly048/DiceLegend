package objectClass;

import Gameplay.Cell;
import ServiceInterface.CellAttribute;

import java.util.EnumSet;

public class Board {

    private final Cell[] cells = new Cell[100];

    public Board(int[][] positions) {
        for (int i = 0; i < cells.length; i++) {

            cells[i] = new Cell(i);

        }
    }

    public void addAttribute(int index, CellAttribute attribute) {

        cells[index].attributes.add(attribute);

    }

    public int[] getPositionFromIndex(int index) {

        return cells[index].getPosition();

    }

    public EnumSet<CellAttribute> getAttributeFromIndex(int index) {

        return cells[index].getAttributes();
    }

}
