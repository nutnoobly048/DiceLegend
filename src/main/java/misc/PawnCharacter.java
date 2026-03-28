package misc;

import java.util.LinkedList;
import java.util.Queue;
import Gameplay.GameState;
import animation.Timeline;
import animation.Tween;
import animation.TweenProperty;
import objectClass.AnimatedSprite;
import objectClass.GameObject;
import service.AudioService;
import service.CommandHandler;

public class PawnCharacter extends GameObject {

    private int currentTileIndex = 0;
    public int slotIndex = 0;

    public static final int[][] SLOT_OFFSETS = {
            { -20, -20 },
            {  20, -20 },
            { -20,  20 },
            {  20,  20 },
    };

    public PawnCharacter(String playerID, String imgFileName, int x, int y) {
        super(playerID, imgFileName, x, y);
    }

    public PawnCharacter(String playerID, String imgFileName) {
        this(playerID, imgFileName, 0, 0);
    }
    public PawnCharacter(String networkId, AnimatedSprite sprite, int x, int y) {
        super(networkId, sprite, x, y);
    }
    private Queue<Integer> moveQueue = new LinkedList<>();
    private boolean isAnimating = false;


    public void moveToTileIndex(int targetIndex) {
        moveQueue.add(targetIndex);
        if (!isAnimating) {
            processNextMove();
        }
    }

    public int getCurrentTileIndex() {
        return currentTileIndex;
    }

    public void setCurrentTileIndex(int index) {
        this.currentTileIndex = index;
    }

    private int getSlotOffsetIndex(int tileIndex) {
        int count = 0;
        if (GameState.currentGame != null && GameState.currentGame.spawnedCharacter != null) {
            for (PawnCharacter pawn : GameState.currentGame.spawnedCharacter.values()) {
                if (pawn != this && pawn.getCurrentTileIndex() == tileIndex) {
                    count++;
                }
            }
        }
        return Math.min(count, SLOT_OFFSETS.length - 1);
    }

    private void processNextMove() {
        if (moveQueue.isEmpty()) {
            isAnimating = false;
            // คิวว่าง -> continue
            CommandHandler.sentIntent("INTENT:SELF:CONTINUE");
            return;
        }

        isAnimating = true;
        int targetIndex = moveQueue.poll();
        AudioService.getInstance().playSFX("Typewriter.wav");
        int currentIndex = this.getCurrentTileIndex();

        System.out.println("Processing move: " + currentIndex + " -> " + targetIndex);

        if (targetIndex > currentIndex && targetIndex - currentIndex <= 6) {
            // เดินปกติ ไม่เจอบันได
            animateStepByStep(targetIndex);

        } else {
            // เจองู / บันได -> กระโดด
            animateDirectJump(targetIndex);
            CommandHandler.broadcastResult("PLAYSFX, portal-in.wav");
        }
    }
    
    private void animateStepByStep(int targetIndex) {
        final double moveDuration = 0.15;
        final double pauseDuration = 0.15;
        double delay = 0;

        Timeline moveTL = new Timeline();
        int fromX = this.x;
        int fromY = this.y;
        Tween lastTween = null;

        for (int i = this.getCurrentTileIndex() + 1; i <= targetIndex; i++) {
            int[] offset = SLOT_OFFSETS[this.slotIndex];

            int toX = GameState.currentGame.gameBoard.getPositionFromIndex(i)[0];
            int toY = GameState.currentGame.gameBoard.getPositionFromIndex(i)[1];
//            System.out.println("[DEBUG] step " + i + ": (" + fromX + "," + fromY + ") -> (" + toX + "," + toY
//                     + ") delay=" + delay);
            Tween moveX = new Tween(this, TweenProperty.X, fromX, toX, moveDuration);
            Tween moveY = new Tween(this, TweenProperty.Y, fromY, toY, moveDuration);
            moveTL.add(moveX, delay);
            moveTL.add(moveY, delay);

            lastTween = moveY;
            fromX = toX;
            fromY = toY;
            delay += moveDuration + pauseDuration;
        }
        
        if (lastTween != null) {
            lastTween.OnComplete(() -> processNextMove());
        } else {
            processNextMove();
        }

        this.setCurrentTileIndex(targetIndex);
        moveTL.play();
    }

    private void animateDirectJump(int targetIndex) {
        final double jumpDuration = 1.0;

        Timeline jumpTL = new Timeline();
        int[] offset = SLOT_OFFSETS[this.slotIndex];

        int toX = GameState.currentGame.gameBoard.getPositionFromIndex(targetIndex)[0];
        int toY = GameState.currentGame.gameBoard.getPositionFromIndex(targetIndex)[1];

        Tween jumpX = new Tween(this, TweenProperty.X, this.x, toX, jumpDuration);
        Tween jumpY = new Tween(this, TweenProperty.Y, this.y, toY, jumpDuration);

        jumpTL.add(jumpX, 0);
        jumpTL.add(jumpY, 0);

        jumpY.OnComplete(() -> processNextMove());

        this.setCurrentTileIndex(targetIndex);
        jumpTL.play();
    }

    private int getMySlotIndex() {
        int index = 0;
        for (String id : GameState.currentGame.allPlayers.keySet()) {
            if (id.equals(this.networkId)) return index;
            index++;
        }
        return 0;
    }

}
