package misc;

import objectClass.VisualObject;

import java.util.HashMap;

public class PawnCharacter extends VisualObject {

    private int currentCharacterIndex = 0;

    public static HashMap<String, PawnCharacter> allCharacter = new HashMap<>();

    public PawnCharacter(String characterID, String imgFileName, int x, int y){
        super(characterID, imgFileName, x, y);

        allCharacter.put(this.networkId, this);
    }

    public PawnCharacter(String characterID, String imgFileName){
        super(characterID, imgFileName, 0, 0);

        allCharacter.put(this.networkId, this);
    }

    public static void moveAllCharacterToIndex() {


    }
    public static void moveThisCharacterToIndex() {
    }

    public static HashMap<String, PawnCharacter> getAllCharacter() {
        return allCharacter;
    }
}
