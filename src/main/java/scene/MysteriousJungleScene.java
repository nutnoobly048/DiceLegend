package scene;

import Gameplay.GameState;
import Gameplay.LobbyState;
import animation.Tween;
import animation.TweenProperty;
import graphicsUtilities.Scene;
import misc.PawnCharacter;
import misc.Player;
import objectClass.AnimatedSprite;
import objectClass.GameButton;
import objectClass.GameObject;
import service.CommandHandler;

import java.awt.*;
import java.util.Comparator;
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
    private static final int START_TILE_X   = 500;
    private static final int START_TILE_Y   = 990;

    public MysteriousJungleScene() {
        setBackground(Color.BLACK);
    }

    @Override
    public void onCreate() {
        boardTexture.getSprite().offsetX = -500;
        boardTexture.getSprite().offsetY = -500;
        boardTexture.z = -1;
        spawnObjectAt(boardTexture);

        spawnObjectAt(transition_left);
        spawnObjectAt(transition_right);
        spawnObjectAt(transition_up);
        spawnObjectAt(transition_down);

        GameButton rollBtn = new GameButton("roll", "button.png", "buttonOnHover.png");
        rollBtn.setBounds(50, 50, 150, 60);
        rollBtn.setOnButtonClicked(() -> CommandHandler.sentIntent("INTENT:SELF:ROLLEVENT"));
        add(rollBtn);
    }

    @Override
    public void onEnter() {
        playEnterTransition();
        spawnAllPawns();
        CommandHandler.sentIntent("INTENT:SELF:CONTINUE");
    }

    @Override
    public void onExit() {
        playExitTransition();
    }

    private void spawnAllPawns() {
        List<Player> players = LobbyState.current.allPlayers.values()
                .stream()
                .sorted(Comparator.comparing(Player::getNetworkID))
                .toList();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            int[] slot = PawnCharacter.SLOT_OFFSETS[i];

            PawnCharacter pawn = new PawnCharacter(player.getNetworkID(),
                    new AnimatedSprite(player.getSpriteName(), 0, 0, 2, 2), START_TILE_X, START_TILE_Y);

            pawn.getSprite().offsetX = PAWN_OFFSET_X + slot[0];
            pawn.getSprite().offsetY = PAWN_OFFSET_Y + slot[1];
            pawn.slotIndex = i;
            pawn.z = i;

            spawnObjectAt(pawn, START_TILE_X, START_TILE_Y);
            GameState.currentGame.spawnedCharacter.put(player.getNetworkID(), pawn);
        }
    }

    private void playEnterTransition() {
        new Tween(transition_left,  TweenProperty.X, -1420, -SCREEN_W, TRANSITION_DURATION).start();
        new Tween(transition_right, TweenProperty.X,  500,   SCREEN_W, TRANSITION_DURATION).start();
        new Tween(transition_up,    TweenProperty.Y, -80,   -SCREEN_H, TRANSITION_DURATION).start();
        new Tween(transition_down,  TweenProperty.Y,  1000,  SCREEN_H, TRANSITION_DURATION).start();
    }

    private void playExitTransition() {
        new Tween(transition_left,  TweenProperty.X, -SCREEN_W, 0, TRANSITION_DURATION).start();
        new Tween(transition_right, TweenProperty.X,  SCREEN_W, 0, TRANSITION_DURATION).start();
        new Tween(transition_up,    TweenProperty.Y, -SCREEN_H, 0, TRANSITION_DURATION).start();
        new Tween(transition_down,  TweenProperty.Y,  SCREEN_H, 0, TRANSITION_DURATION).start();
    }
}