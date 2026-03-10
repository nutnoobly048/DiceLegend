package misc;

import objectClass.GameObject;
import java.util.HashMap;

public class PawnCharacter extends GameObject {

    private int currentCharacterIndex = 0;

    public static HashMap<String, PawnCharacter> allCharacters = new HashMap<>();

    public PawnCharacter(String characterID, String imgFileName, int x, int y) {
        super(characterID, imgFileName, x, y);

        allCharacters.put(this.getNetworkId(), this);
    }

    public PawnCharacter(String characterID, String imgFileName) {
        this(characterID, imgFileName, 0, 0);
    }

    public static void moveAllCharactersToIndex() {
        for (PawnCharacter pc : allCharacters.values()) {
            pc.moveThisCharacterToIndex();
        }
    }

    public void moveThisCharacterToIndex() {

        System.out.println("Moving character " + this.getNetworkId() + " to index " + currentCharacterIndex);
    }


    public int getCurrentCharacterIndex() {
        return currentCharacterIndex;
    }

    public void setCurrentCharacterIndex(int index) {
        this.currentCharacterIndex = index;
    }

    public static HashMap<String, PawnCharacter> getAllCharacters() {
        return allCharacters;
    }
}