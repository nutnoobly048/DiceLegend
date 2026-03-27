package service;

import graphicsUtilities.SceneUtilities;
import objectClass.AnimatedSprite;
import scene.CyroGard;

public class UIEvent {
    public static void HandleUIEvent(String[] args) {
        String mainAction = args[0];
        switch (mainAction) {
            case "PRINTIFWORK" -> System.out.println("SHOW CARD");
            case "WAITFOR" -> {
                String thisTurnPlayerID = args[1];
                System.out.println("UIEvent WAITFOR received for player: " + thisTurnPlayerID);
                if (SceneUtilities.getCurrentGameScene() instanceof CyroGard cyroGard) {
                    System.out.println("Current scene is CyroGard, calling updateTargetSelectBtnForTurn");
                    cyroGard.updateTargetSelectBtnForTurn(thisTurnPlayerID);
                } else {
                    System.out.println("Current scene is not CyroGard: " + SceneUtilities.getCurrentGameScene());
                }
            }
            case "CARDPOPUP" -> {}
            case "ROLLRESULT" -> {
                if (SceneUtilities.getCurrentGameScene().currentSceneObject.containsKey("dice")) {
                    SceneUtilities.getCurrentGameScene().currentSceneObject.get("dice").setSprite("dice" + args[1] + ".png");
                }
            }
            case "ANIMATEDROLL" -> {
                AnimatedSprite diceAnimated = new AnimatedSprite("diceAnimated.png", 1495, 27, 36, 29);
                if (SceneUtilities.getCurrentGameScene().currentSceneObject.containsKey("dice")) {
                    SceneUtilities.getCurrentGameScene().currentSceneObject.get("dice").setSprite(diceAnimated);
                }
            }
        }
    }
}
