package Gameplay;

import ServiceInterface.CellAttribute;
import objectClass.Board;

import java.util.ArrayList;
import java.util.EnumSet;



public class Cell {


    public Cell(int index) {
        this(index,0,0, -1);
    }

    public Cell(int index, int xPos, int yPos) {

        this(index, xPos, yPos, -1);

    }

    public Cell(int index, int xPos, int yPos, int destinationIndex) {
        this.index = index;
        this.xPos = xPos;
        this.yPos = yPos;
        this.destinationIndex = destinationIndex;


    }

    public int[] getPosition() {

        return new int[]{xPos, yPos};

    }

    public EnumSet<CellAttribute> getAttributes() {

        return this.attributes;

    }

    public int index;
    public int xPos;
    public int yPos;

    public EnumSet<CellAttribute> attributes = EnumSet.noneOf(CellAttribute.class);

    public int destinationIndex;



}