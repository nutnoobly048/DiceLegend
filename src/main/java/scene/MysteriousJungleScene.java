package scene;

import Gameplay.GameState;
import animation.Tween;
import animation.TweenProperty;
import graphicsUtilities.Scene;
import misc.PawnCharacter;
import misc.Player;
import objectClass.GameObject;
import service.CommandHandler;

import java.awt.*;
import java.util.List;

public class MysteriousJungleScene extends Scene {

    private final int SCREEN_W = 1920;
    private final int SCREEN_H = 1080;

    private final double TRANSITION_DURATION = 0.4;

    private GameObject transition_left  = new GameObject("transit_left",  "Transition.png", 0, 0);
    private GameObject transition_right = new GameObject("transit_right", "Transition.png", 0, 0);
    private GameObject transition_up    = new GameObject("transit_up",    "Transition.png", 0, 0);
    private GameObject transition_down  = new GameObject("transit_down",  "Transition.png", 0, 0);

    private GameObject boardTexture = new GameObject("board", "prototypeBoard.png", SCREEN_W / 2, SCREEN_H / 2);

    private static final int PAWN_SPRITE_W  = 64;
    private static final int PAWN_SPRITE_H  = 96;
    private static final int PAWN_OFFSET_X  = -(PAWN_SPRITE_W  / 2);
    private static final int PAWN_OFFSET_Y  = -PAWN_SPRITE_H;


    private static final int START_TILE_X = 500;
    private static final int START_TILE_Y = 1000;

    private static final int[][] SLOT_OFFSETS = {
            { -20, -20 },
            {  20, -20 },
            { -20,  10 },
            {  20,  10 },
    };

    public MysteriousJungleScene() {
        setBackground(Color.BLACK);
        setupObjects();
        setOnSceneEnter(() -> {
            playEnterTransition();
            spawnAllPawns();
            CommandHandler.sentIntent("INTENT:SELF:CONTINUE");
        });

    }

    private void setupObjects() {
        boardTexture.getSprite().offsetX = -500;
        boardTexture.getSprite().offsetY = -500;
        boardTexture.z = -1;
        spawnObjectAt(boardTexture);

        spawnObjectAt(transition_left);
        spawnObjectAt(transition_right);
        spawnObjectAt(transition_up);
        spawnObjectAt(transition_down);
    }

    private void spawnAllPawns() {
        List<Player> players = List.copyOf(GameState.currentGame.allPlayers.values());

        for (int slotIndex = 0; slotIndex < players.size(); slotIndex++) {
            Player player  = players.get(slotIndex);
            int[]  slot    = SLOT_OFFSETS[slotIndex];

            int spawnX = START_TILE_X + slot[0];
            int spawnY = START_TILE_Y + slot[1];

            PawnCharacter pawn = new PawnCharacter(player.getNetworkID(), player.getSpriteName(), spawnX, spawnY);
            pawn.getSprite().offsetX = PAWN_OFFSET_X;
            pawn.getSprite().offsetY = PAWN_OFFSET_Y;
            pawn.z = slotIndex;

            spawnObjectAt(pawn, spawnX, spawnY);

            GameState.currentGame.spawnedCharacter.put(player.getNetworkID(), pawn);
        }
    }

    private void playEnterTransition() {
        new Tween(transition_left,  TweenProperty.X, -1420, -SCREEN_W, TRANSITION_DURATION).start();
        new Tween(transition_right, TweenProperty.X,  500,   SCREEN_W, TRANSITION_DURATION).start();
        new Tween(transition_up,    TweenProperty.Y, -80,   -SCREEN_H, TRANSITION_DURATION).start();
        new Tween(transition_down,  TweenProperty.Y,  1000,  SCREEN_H, TRANSITION_DURATION).start();
    }

    @Override
    public void requestExit(Runnable onExitComplete) {
        playExitTransition(onExitComplete);
    }

    private void playExitTransition(Runnable onDone) {
        new Tween(transition_left,  TweenProperty.X, -SCREEN_W, 0, TRANSITION_DURATION).start();
        new Tween(transition_right, TweenProperty.X,  SCREEN_W, 0, TRANSITION_DURATION).start();
        new Tween(transition_up,    TweenProperty.Y, -SCREEN_H, 0, TRANSITION_DURATION).start();
        new Tween(transition_down,  TweenProperty.Y,  SCREEN_H, 0, TRANSITION_DURATION).OnComplete(onDone).start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        transition_left.getSprite().draw(g2d);
        transition_right.getSprite().draw(g2d);
        transition_up.getSprite().draw(g2d);
        transition_down.getSprite().draw(g2d);
    }
}