package service;

import Gameplay.GameState;
import graphicsUtilities.ImagePreload;
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
//                    System.out.println("Current scene is CyroGard, calling updateTargetSelectBtnForTurn");
                    cyroGard.updateTargetSelectBtnForTurn(thisTurnPlayerID);
                } else {
//                    System.out.println("Current scene is not CyroGard: " + SceneUtilities.getCurrentGameScene());
                }
            }
            case "ITEMPOPUP" -> {
                CyroGard.popupSequence.setSprite(args[1] + ".png");
                CyroGard.popupSequence.getSprite().offsetX = -ImagePreload.get(args[1] + ".png").getWidth(null) / 2;
                CyroGard.popupSequence.getSprite().offsetY = -ImagePreload.get(args[1] + ".png").getHeight(null) / 2;
                CyroGard.popupSequence.setVisible(true);

                Timer delayTimer = new Timer(6000, e -> {
                    if (GameState.currentGame != null) {
                        CyroGard.popupSequence.setSprite("blank.png");
                    }
                });
                delayTimer.setRepeats(false);
                delayTimer.start();
                //avoid green underbar in on EVENTPOPUP

            }

            case "EVENTPOPUP" -> {
                CyroGard.popupSequence.setSprite(args[1] + ".png");
                CyroGard.popupSequence.getSprite().offsetX = -ImagePreload.get(args[1] + ".png").getWidth(null) / 2;
                CyroGard.popupSequence.getSprite().offsetY = -ImagePreload.get(args[1]+ ".png").getHeight(null) / 2;
                CyroGard.popupSequence.setVisible(true);

                Timer delayTimer = new Timer(6000, e -> {
                    if (GameState.currentGame != null) {
                        CyroGard.popupSequence.setSprite("blank.png");
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
            case "EVENTICON" -> {
                System.out.println("EVENT ICON !!!!!!" + args[1]);
                CyroGard.cardIcon.setSprite(args[1] + ".png");
                CyroGard.cardIcon.getSprite().offsetX = -ImagePreload.get(args[1] + ".png").getWidth(null) / 2;
                CyroGard.cardIcon.getSprite().offsetY = -ImagePreload.get(args[1]+ ".png").getHeight(null) / 2;
                //CyroGard.cardIcon.getSprite().scaleImageByPercentage(0.2);
            }
        }
    }
}
