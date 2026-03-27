package service;

import Gameplay.GameState;
import graphicsUtilities.SceneUtilities;
import objectClass.AnimatedSprite;
import objectClass.GameModal;
import scene.CyroGard;

import javax.swing.*;

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
            case "ITEMPOPUP" -> {
                CyroGard.popupItem.setImageBackgound(args[1]);
                CyroGard.popupItem.setVisible(true);
                Timer delayTimer = new Timer(4000, e -> {
                    if (GameState.currentGame != null) {
                        CyroGard.popupItem.setVisible(false);
                    }
                });
                delayTimer.setRepeats(false);
                delayTimer.start();

            }
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
