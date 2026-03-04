package Gameplay;

import ServiceInterface.CellAttribute;

import java.util.EnumSet;



public class Cell {
    public Cell(int index, int xPos, int yPos, int destinationIndex) {
        this.index = index;
        this.xPos = xPos;
        this.yPos = yPos;
        this.destinationIndex = destinationIndex;
    }

    public int index;
    public int xPos;
    public int yPos;
    public EnumSet<CellAttribute> attributes = EnumSet.noneOf(CellAttribute.class);

    public int destinationIndex;



}