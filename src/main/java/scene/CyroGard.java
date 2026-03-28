package scene;

import Gameplay.GameState;
import Gameplay.LobbyState;
import animation.Tween;
import animation.TweenProperty;
import graphicsUtilities.FontLoader;
import graphicsUtilities.ImagePreload;
import graphicsUtilities.Scene;
import misc.PawnCharacter;
import misc.Player;
import objectClass.*;
import service.CommandHandler;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;

public final class CyroGard extends Scene {
    private final int SCREEN_W = 1920;
    private final int SCREEN_H = 1080;
    private final double TRANSITION_DURATION = 0.4;

    private GameObject transition_left  = new GameObject("transit_left",  "Transition.png", 0, 0);
    private GameObject transition_right = new GameObject("transit_right", "Transition.png", 0, 0);
    private GameObject transition_up    = new GameObject("transit_up",    "Transition.png", 0, 0);
    private GameObject transition_down  = new GameObject("transit_down",  "Transition.png", 0, 0);

    private GameObject boardFrame   = new GameObject("boardFrame", "CyroBoardFrame.png", SCREEN_W/2, SCREEN_H/2);
    private GameObject boardTexture = new GameObject("board", "CyroGardBoard.png", SCREEN_W / 2, SCREEN_H / 2);
    private GameObject diceBackground = new GameObject("diceBackground", "CyroDiceFrame.png", 1495, 27);
    private GameObject itemFrame    = new GameObject("itemFrame", "CyroItemFrame.png", 32, 27);
    private GameObject chatFrame = new GameObject("chatFrame", "CyroItemDes.png", 51, 514);
    public static JTextArea chatTextArea = new JTextArea();
    private JTextField chatTextField = new JTextField();
    private GameButton chatSendButton = new GameButton("Send", "button.png", "buttonOnHover.png");
    private JScrollPane chatScrollPane = new JScrollPane(chatTextArea);

    private GameObject dice = new GameObject("dice", "dice5.png", 1550, 40);
    public static GameObject cardIcon = new GameObject("cardEvent", "blank.png", 230, 270);
    public static GameObject popupSequence = new GameObject("popupSequence", "blank.png", 1920 / 2, 1080 / 2);

    GameButton targetSelectBtn;

    public static ArrayList<Player> playerList = new ArrayList<>();
    private ArrayList<GameObject> portraits = new ArrayList<>();

    private static final int PAWN_SPRITE_W  = 64;
    private static final int PAWN_SPRITE_H  = 96;
    private static final int PAWN_OFFSET_X  = -(PAWN_SPRITE_W  / 2);
    private static final int PAWN_OFFSET_Y  = -PAWN_SPRITE_H;
    private static final int START_TILE_X   = 500;
    private static final int START_TILE_Y   = 990;

    public CyroGard() {
        setBackground(ImagePreload.get("CyroMainBackground.png"));

        playerList.clear();
        playerList.addAll(GameState.currentGame.allPlayers.values());

        int count = 1;

        for (Player player: playerList) {
            System.out.println(player.getNetworkID() + " " + count);
            switch (count) {
                case 1 -> {
                    GameObject p = new GameObject(String.valueOf(count * 30), player.getLocalSpritePortraitName(), 1540, 730);
                    p.z = 3;
                    spawnObjectAt(p);
                    portraits.add(p);
                }
                case 2 -> {
                    GameObject p = new GameObject(String.valueOf(count * 30), player.getLocalSpritePortraitName(), 1700, 730);
                    p.z = 3;
                    spawnObjectAt(p);
                    portraits.add(p);
                }
                case 3 -> {
                    GameObject p = new GameObject(String.valueOf(count * 30), player.getLocalSpritePortraitName(), 1540, 890);
                    p.z = 3;
                    spawnObjectAt(p);
                    portraits.add(p);
                }
                case 4 -> {
                    GameObject p = new GameObject(String.valueOf(count * 30), player.getLocalSpritePortraitName(), 1700, 890);
                    p.z = 3;
                    spawnObjectAt(p);
                    portraits.add(p);
                }
            }
            count++;
        }

    }

    @Override
    public void onCreate() {
        setupBoardAndUI();
        this.setFocusable(true);
//        this.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//                    CommandHandler.sentIntent("INTENT:SELF:ROLLEVENT");
//                }
//            }
//        });
    }

    @Override
    public void onEnter() {
        setupPortals();
        playEnterTransition();
        spawnAllPawns();
        
        if (GameState.currentGame != null && !GameState.currentGame.currentPlayerTurnId.isEmpty()) {
            updateTargetSelectBtnForTurn(GameState.currentGame.currentPlayerTurnId);
        }
        
        CommandHandler.sentIntent("INTENT:SELF:CONTINUE");
    }

    @Override
    public void onExit() {
        playExitTransition();
    }

    private void setupBoardAndUI() {
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

        chatFrame.z = -2;
        spawnObjectAt(chatFrame);
        setUpChat();

        spawnObjectAt(dice);

        cardIcon.z = 99;
        spawnObjectAt(cardIcon);

        popupSequence.setVisible(false);
        popupSequence.z = 100;
        spawnObjectAt(popupSequence);

        spawnObjectAt(transition_left);
        spawnObjectAt(transition_right);
        spawnObjectAt(transition_up);
        spawnObjectAt(transition_down);

        AtomicBoolean isRolledClicked = new AtomicBoolean(false);

        GameButton rollBtn = new GameButton("", "CyroRoll.png", "CyroRollHover.png");
        rollBtn.setBounds(1555, 443, 289, 108);
        rollBtn.setOnButtonClicked(() -> {
            if (isRolledClicked.get()) return;
            isRolledClicked.set(true); 
            CommandHandler.sentIntent("INTENT:SELF:ROLLEVENT");

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        isRolledClicked.set(false);
                    });
                }
            }, 5000);

        });
        this.add(rollBtn);

        targetSelectBtn = new GameButton("", "TargetSelectBtn.png", "TargetSelectBtn.png");

        targetSelectBtn.setBounds(1524, 715, 342, 342);
        targetSelectBtn.addMouseMotionListener(new MouseMotionAdapter() {
            int btnW = targetSelectBtn.getWidth();
            int btnH = targetSelectBtn.getHeight();
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (x < btnW / 2 && y < btnH / 2) { // top left
                    ImageIcon hoverImg = new ImageIcon(ImagePreload.get("select-hover-1.png"));
                    targetSelectBtn.setRolloverIcon(hoverImg);
                    targetSelectBtn.setPressedIcon(hoverImg);
                } else if (x > btnW / 2 && y < btnH / 2) {  // top right
                    ImageIcon hoverImg = new ImageIcon(ImagePreload.get("select-hover-2.png"));
                    targetSelectBtn.setRolloverIcon(hoverImg);
                    targetSelectBtn.setPressedIcon(hoverImg);
                } else if (x < btnW / 2 && y > btnH / 2) { // bottom left
                    ImageIcon hoverImg = new ImageIcon(ImagePreload.get("select-hover-3.png"));
                    targetSelectBtn.setRolloverIcon(hoverImg);
                    targetSelectBtn.setPressedIcon(hoverImg);
                } else if (x > btnW / 2 && y > btnH / 2){ // bottom right
                    ImageIcon hoverImg = new ImageIcon(ImagePreload.get("select-hover-4.png"));
                    targetSelectBtn.setRolloverIcon(hoverImg);
                    targetSelectBtn.setPressedIcon(hoverImg);
                } else {  // set to default
                    ImageIcon defaultImg = new ImageIcon(ImagePreload.get("TargetSelectBtn.png"));
                    targetSelectBtn.setRolloverIcon(defaultImg);
                    targetSelectBtn.setPressedIcon(defaultImg);
                }
            }

        });

        targetSelectBtn.addMouseListener(new MouseAdapter() {

            private int getQaureand(MouseEvent e, GameButton b) {
                if (e.getX() < b.getWidth() / 2) {
                    return (e.getY() < b.getHeight() / 2) ? 1: 2;
                } else {
                    return (e.getY() < b.getHeight() / 2) ? 3: 4;
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {

                GameButton b = (GameButton) e.getSource();

                if (GameState.currentGame != null) {
                    if (getQaureand(e, b) == 1) {
                        if (GameState.currentGame.isSinglePlayer || !Player.localPlayer.getNetworkID().equals(playerList.get(0).getNetworkID())) {
                            CommandHandler.sentIntent("INTENT:SELF:SETTARGET:" + playerList.get(0).getNetworkID());
                        }
                    } else if (getQaureand(e, b) == 3) {
                        if (GameState.currentGame.isSinglePlayer || !Player.localPlayer.getNetworkID().equals(playerList.get(1).getNetworkID())) {
                            CommandHandler.sentIntent("INTENT:SELF:SETTARGET:" + playerList.get(1).getNetworkID());
                        }
                    } else if (getQaureand(e, b) == 2) {
                        if (GameState.currentGame.isSinglePlayer || !Player.localPlayer.getNetworkID().equals(playerList.get(2).getNetworkID())) {
                            CommandHandler.sentIntent("INTENT:SELF:SETTARGET:" + playerList.get(2).getNetworkID());
                        }
                    } else if (getQaureand(e, b) == 4) {
                        if (GameState.currentGame.isSinglePlayer || !Player.localPlayer.getNetworkID().equals(playerList.get(3).getNetworkID())) {
                            CommandHandler.sentIntent("INTENT:SELF:SETTARGET:" + playerList.get(3).getNetworkID());
                        }
                    }
                }

            }
        });

        this.add(targetSelectBtn);

    }

    private void setUpChat() {
        add(chatScrollPane);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        chatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatScrollPane.setBounds(70, 535, 333, 450);
        chatTextArea.setFont(FontLoader.getFont(30));
        chatTextArea.setLineWrap(true);
        chatTextArea.setWrapStyleWord(true);

        add(chatTextField);
        chatTextField.setBounds(70, 985, 250, 50);
        chatTextField.setBackground(Color.red);
        chatTextField.setForeground(Color.white);
        chatTextField.setFont(FontLoader.getFont(30));

        add(chatSendButton);
        chatSendButton.setBounds(320, 985, 83, 50);
        chatSendButton.setForeground(Color.black);
        chatSendButton.setOnButtonClicked(() -> {
            String message = chatTextField.getText();
            if (!message.isEmpty()) {
                CommandHandler.sentIntent("INTENT:" + Player.getLocalPlayerId() + ":CHAT:" + message);
            }
        });
    }

    private void setupPortals() {
        int[][] destinations = GameState.currentGame.gameBoard.getDestinations();

        for (int i = 0; i < destinations.length; i++) {
            int portalInIndex = destinations[i][0];
            int[] posIn = GameState.currentGame.gameBoard.getPositionFromIndex(portalInIndex);

            AnimatedSprite inSprite = new AnimatedSprite("PortalIn.png", posIn[0], posIn[1], 6, 2);
            inSprite.offsetX = -40; inSprite.offsetY = -50;
            spawnObjectAt(new GameObject("pIn" + i, inSprite, posIn[0], posIn[1]));

            int portalOutIndex = destinations[i][1];
            int[] posOut = GameState.currentGame.gameBoard.getPositionFromIndex(portalOutIndex);

            AnimatedSprite outSprite = new AnimatedSprite("PortalOut.png", posOut[0], posOut[1], 6, 2);
            outSprite.offsetX = -40; outSprite.offsetY = -50;
            spawnObjectAt(new GameObject("pOut" + i, outSprite, posOut[0], posOut[1]));
        }

    }

    private void spawnAllPawns() {
        List<Player> players = LobbyState.current.allPlayers.values()
                .stream()
                .sorted(Comparator.comparing(Player::getNetworkID))
                .toList();

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            int[] slot = PawnCharacter.SLOT_OFFSETS[i];

            PawnCharacter pawn = new PawnCharacter(p.getNetworkID(),
                    new AnimatedSprite(p.getSpriteName(), 0, 0, 2, 2), START_TILE_X, START_TILE_Y);

            pawn.getSprite().offsetX = PAWN_OFFSET_X + slot[0];
            pawn.getSprite().offsetY = PAWN_OFFSET_Y + slot[1];
            pawn.slotIndex = i;
            pawn.z = i;

            spawnObjectAt(pawn, START_TILE_X, START_TILE_Y);
            GameState.currentGame.spawnedCharacter.put(p.getNetworkID(), pawn);
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

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        
        for (GameObject portrait : portraits) {
            if (portrait.hasSprite()) {
                portrait.getSprite().draw(g2d);
            }
        }
        popupSequence.getSprite().draw(g2d);

        transition_left.getSprite().draw(g2d);
        transition_right.getSprite().draw(g2d);
        transition_up.getSprite().draw(g2d);
        transition_down.getSprite().draw(g2d);

    }
    
    public void updateTargetSelectBtnForTurn(String playerId) {
        if (targetSelectBtn == null) {
            System.out.println("updateTargetSelectBtnForTurn: targetSelectBtn is null");
            return;
        }
        
        int playerIndex = -1;
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getNetworkID().equals(playerId)) {
                playerIndex = i;
                break;
            }
        }
        
        System.out.println("updateTargetSelectBtnForTurn: playerId=" + playerId + ", playerIndex=" + playerIndex + ", playerList.size=" + playerList.size());
        
        if (playerIndex == -1) return;
        
        String imageName = "select-turn-" + (playerIndex + 1) + ".png";
        System.out.println("Loading image: " + imageName);
        ImageIcon turnIcon = new ImageIcon(ImagePreload.get(imageName));
        targetSelectBtn.setIcon(turnIcon);
        targetSelectBtn.setPressedIcon(turnIcon);
    }

}