package scene;

import Gameplay.GameState;
import Gameplay.LobbyState;
import animation.Tween;
import animation.TweenProperty;
import graphicsUtilities.ImagePreload;
import graphicsUtilities.Scene;
import misc.PawnCharacter;
import misc.Player;
import objectClass.AnimatedSprite;
import objectClass.Board;
import objectClass.GameButton;
import objectClass.GameObject;
import service.CommandHandler;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Comparator;
import java.util.List;

public class CyroGard extends Scene {
    private final int SCREEN_W = 1920;
    private final int SCREEN_H = 1080;

    private final double TRANSITION_DURATION = 0.4;

    private GameObject transition_left  = new GameObject("transit_left",  "Transition.png", 0, 0);
    private GameObject transition_right = new GameObject("transit_right", "Transition.png", 0, 0);
    private GameObject transition_up    = new GameObject("transit_up",    "Transition.png", 0, 0);
    private GameObject transition_down  = new GameObject("transit_down",  "Transition.png", 0, 0);

    private GameObject boardFrame=  new GameObject("boardFrame", "CyroBoardFrame.png", SCREEN_W/2, SCREEN_H/2);
    private GameObject boardTexture = new GameObject("board", "CyroGardBoard.png", SCREEN_W / 2, SCREEN_H / 2);
    private GameObject diceBackground = new GameObject("diceBackground", "CyroDiceFrame.png", 1495, 27);
    private GameObject itemFrame = new GameObject("itemFrame", "CyroItemFrame.png", 32, 27);

    private static final int PAWN_SPRITE_W  = 64;
    private static final int PAWN_SPRITE_H  = 96;
    private static final int PAWN_OFFSET_X  = -(PAWN_SPRITE_W  / 2);
    private static final int PAWN_OFFSET_Y  = -PAWN_SPRITE_H;

    private static final int START_TILE_X = 500 ; //-10 510-500
    private static final int START_TILE_Y = 990; //+10 980->990


    public CyroGard() {
        setBackground(ImagePreload.get("CyroMainBackground.png"));
        setupObjects();
        setOnSceneEnter(() -> {
            playEnterTransition();
            spawnAllPawns();
            CommandHandler.sentIntent("INTENT:SELF:CONTINUE");
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == 32) {
                    CommandHandler.sentIntent("INTENT:SELF:ROLLEVENT");
                }
            }
        });


    }

    private void setupObjects() {
        boardFrame.getSprite().offsetY = -513;
        boardFrame.getSprite().offsetX = -513;
        boardFrame.z = -2;
        spawnObjectAt(boardFrame);

        boardTexture.getSprite().offsetX = -500;
        boardTexture.getSprite().offsetY = -500;
        boardTexture.z = -1;
        spawnObjectAt(boardTexture);

        diceBackground.z = -2;
        spawnObjectAt(diceBackground);

        itemFrame.z = -2;
        spawnObjectAt(itemFrame);

        spawnObjectAt(transition_left);
        spawnObjectAt(transition_right);
        spawnObjectAt(transition_up);
        spawnObjectAt(transition_down);

        GameButton rollBtn = new GameButton("", "CyroRoll.png", "CyroRollHover.png");
        rollBtn.setBounds(70, 70, 289, 108);
        rollBtn.setOnButtonClicked(() -> {
            CommandHandler.sentIntent("INTENT:SELF:ROLLEVENT");
        });
        add(rollBtn);

        for (int i = 0; i < Board.destinationCyroGard.length; i++) {

            int portalInIndex;
            int[] portalInPosition;
            int portalInX;
            int portalInY;

            int portalOutIndex;
            int[] portalOutPosition;
            int portalOutX;
            int portalOutY;

            portalInIndex = Board.destinationCyroGard[i][0];
            portalInPosition = GameState.currentGame.gameBoard.getPositionFromIndex(portalInIndex);
            portalInX = portalInPosition[0];
            portalInY = portalInPosition[1];

            AnimatedSprite portalInSprite = new AnimatedSprite("PortalIn.png", portalInX, portalInY, 6, 2);
            portalInSprite.offsetX = -40;
            portalInSprite.offsetY = -50;


            portalOutIndex = Board.destinationCyroGard[i][1];
            portalOutPosition = GameState.currentGame.gameBoard.getPositionFromIndex(portalOutIndex);
            portalOutX = portalOutPosition[0];
            portalOutY = portalOutPosition[1];

            AnimatedSprite portalOutSprite = new AnimatedSprite("PortalOut.png", portalOutX, portalOutY, 6, 2);
            portalOutSprite.offsetX = -40;
            portalOutSprite.offsetY = -50;

            GameObject portalIn = new GameObject(Integer.toString(i), portalInSprite, portalInX, portalInY);

            GameObject portalOut = new GameObject(Integer.toString(i + 20), portalOutSprite, portalOutX, portalOutY);

            spawnObjectAt(portalIn);
            spawnObjectAt(portalOut);

        }
    }

    private void spawnAllPawns() {
        List<Player> players = LobbyState.current.allPlayers.values()
                .stream()
                .sorted(Comparator.comparing(Player::getNetworkID))
                .toList();

        for (int slotIndex = 0; slotIndex < players.size(); slotIndex++) {
            Player player = players.get(slotIndex);
            int[] slot = PawnCharacter.SLOT_OFFSETS[slotIndex];

            int spawnX = START_TILE_X;
            int spawnY = START_TILE_Y;

            PawnCharacter pawn = new PawnCharacter(player.getNetworkID(),
                    new AnimatedSprite(player.getSpriteName(), 0, 0, 2, 2), spawnX, spawnY);

            pawn.getSprite().offsetX = PAWN_OFFSET_X + slot[0];
            pawn.getSprite().offsetY = PAWN_OFFSET_Y + slot[1];
            pawn.slotIndex = slotIndex;
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
