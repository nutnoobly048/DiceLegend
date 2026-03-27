package scene;

import Gameplay.GameState;
import Gameplay.LobbyState;
import Gameplay.SceneList;
import animation.Tween;
import animation.TweenProperty;
import graphicsUtilities.FontLoader;
import graphicsUtilities.ImagePreload;
import graphicsUtilities.Scene;
import graphicsUtilities.SceneUtilities;
import misc.Player;
import objectClass.GameButton;
import objectClass.GameObject;
import service.CommandHandler;
import service.RunService;

import javax.swing.*;
import java.awt.*;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.ArrayList;

public class LobbyScene extends Scene {

    // private final ChangeNamePopUp changeNamePopUp = new ChangeNamePopUp();

    private GameObject transition_left = new GameObject("transit_left", "Transition.png", 0, 0);
    private GameObject transition_right = new GameObject("transit_right", "Transition.png", 0, 0);
    private GameObject transition_up = new GameObject("transit_up", "Transition.png", 0, 0);
    private GameObject transition_down = new GameObject("transit_down", "Transition.png", 0, 0);

    private GameButton roomNumber = new GameButton("", "lobbyNumber.png", "lobbyNumber.png");

    private GameButton backButton = new GameButton("", "Back.png", "back_onhover.png");
    private GameButton startButton = new GameButton("", "Start.png", "start_onhover2.png");
    private GameButton mapButton = new GameButton("", "Map.png", "map_onhover.png");
    private GameButton arrowThis = new GameButton("", "Arrow.png", "Arrow.png");
    // private GameButton changeCharactorButton = new GameButton("",
    // "change-char.png", "change-char.png");
    private GameButton kennethCard = new GameButton("", "kenneth.png", "kenneth.png");
    private GameButton vanceCard = new GameButton("", "vance.png", "vance.png");
    private GameButton riveraCard = new GameButton("", "rivera.png", "rivera.png");
    private GameButton sylviaCard = new GameButton("", "Sylvia.png", "Sylvia.png");

    private int x_arrow = 305;

    private double duration = 0.5;

    public LobbyScene() {
        setBackground(ImagePreload.get("Lobby_Background.png"));
    }

    @Override
    public void onCreate() {

        // initialXofArraw();

        spawnObjectAt(transition_left);
        spawnObjectAt(transition_right);
        spawnObjectAt(transition_up);
        spawnObjectAt(transition_down);

        roomNumber.setBounds(773, 85, 422, 205);
        backButton.setBounds(0, 34, 431, 221);
        startButton.setBounds(775, 920, 376, 138);
        mapButton.setBounds(560, 914, 159, 145);
        kennethCard.setBounds(197, 345, 295, 395);
        vanceCard.setBounds(604, 345, 295, 395);
        riveraCard.setBounds(1026, 345, 295, 395);
        sylviaCard.setBounds(1440, 345, 295, 395);
        arrowThis.setBounds(x_arrow, 760, 73, 73);

        // this.add(changeNamePopUp);

        this.add(kennethCard);
        this.add(vanceCard);
        this.add(riveraCard);
        this.add(sylviaCard);

        kennethCard.setVisible(false);
        vanceCard.setVisible(false);
        riveraCard.setVisible(false);
        sylviaCard.setVisible(false);

        String topicRoomAmount = "DiceLegend/" + LobbyState.current.lobbyName + "/room_amount";
        roomNumber.setText(LobbyState.current.lobbyName);
        RunService.mqtt.subscribe(topicRoomAmount, (t, msg) -> {
            try {
                LobbyState snapshot = LobbyState.current;
                if (snapshot == null)
                    return;
                int amount = Integer.parseInt(msg);
                SwingUtilities.invokeLater(() -> {
                    LobbyState snapInUI = LobbyState.current;
                    if (snapInUI == null)
                        return;
                    updatePlayerCards(amount);
                    initialXofArraw(snapInUI);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        setupButtonLogic();
        roomNumber.setForeground(Color.BLACK);
        roomNumber.setFont(FontLoader.getFont(60).deriveFont(Font.BOLD));
        this.add(roomNumber);

        this.add(backButton);
        this.add(startButton);
        this.add(mapButton);
        this.add(arrowThis);

        if (!LobbyState.current.isHost) {
            startButton.setVisible(false);
        } else {
            startButton.setVisible(true);
        }

    }

    @Override
    public void onEnter() {
        new Tween(transition_left, TweenProperty.X, -960, -1920, duration).start();
        new Tween(transition_right, TweenProperty.X, 960, 1920, duration).start();
        new Tween(transition_up, TweenProperty.Y, -540, -1080, duration).start();
        new Tween(transition_down, TweenProperty.Y, 540, 1080, duration).start();
    }

    @Override
    public void onExit() {
        new Tween(transition_left, TweenProperty.X, -1920, -960, duration).start();
        new Tween(transition_right, TweenProperty.X, 1920, 960, duration).start();
        new Tween(transition_up, TweenProperty.Y, -1080, -540, duration).start();
        new Tween(transition_down, TweenProperty.Y, 1080, 540, duration).start();
    }

    private void initialXofArraw(LobbyState state) {

        if (LobbyState.current == null)
            return;

        ArrayList<String> allPlayersID = new ArrayList<>();

        for (Player value : state.allPlayers.values()) {
            allPlayersID.add(value.getNetworkID());
        }

        int index = allPlayersID.indexOf(Player.getLocalPlayerId());
        switch (index) {
            case 0:
                x_arrow = 303 + 2;
                break;
            case 1:
                x_arrow = 710 + 2;
                break;
            case 2:
                x_arrow = 1133 + 2;
                break;
            case 3:
                x_arrow = 1546 + 2;
                break;
        }
        arrowThis.setBounds(x_arrow, 760, 73, 73); // ← อัปเดต position จริง
        arrowThis.revalidate();
        arrowThis.repaint();
    }

    private void updatePlayerCards(int amount) {
        kennethCard.setVisible(amount >= 1);
        vanceCard.setVisible(amount >= 2);
        riveraCard.setVisible(amount >= 3);
        sylviaCard.setVisible(amount >= 4);
        this.repaint();
    }

    private void setupButtonLogic() {
        AtomicBoolean isBackClicked = new AtomicBoolean(false);
        AtomicBoolean isStartClicked = new AtomicBoolean(false);
        backButton.setOnButtonClicked(() -> {
            if (isBackClicked.get())
                return;
            isBackClicked.set(true);

            if (RunService.mqtt.isConnected() && LobbyState.current.isHost) {
                CommandHandler.broadcastResult("CHANGESCENETO", "main_menu");
                CommandHandler.broadcastResult("DISCONNECT");
                RunService.mqtt.clearRetained("DiceLegend/" + LobbyState.current.lobbyName + "/room_state");
            } else if (RunService.mqtt.isConnected()) {
                handleClientExit();
            }

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        isBackClicked.set(false);
                    });
                }
            }, 5000);
        });

        startButton.setOnButtonClicked(() -> {

            if (isStartClicked.get())
                return;
            isStartClicked.set(true);

            String baseTopic = "DiceLegend/" + LobbyState.current.lobbyName;
            RunService.mqtt.publishRetained(baseTopic + "/room_state", "GAME_STARTED");
            CommandHandler.sentIntent("INTENT:SELF:START_GAME");

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        isStartClicked.set(false);
                    });
                }
            }, 5000);

        });

        // changeNameButton.setOnButtonClicked(() -> {
        // changeNamePopUp.setVisible(true);
        // });

    }

    private void handleClientExit() {
        AtomicBoolean isMinus = new AtomicBoolean(false);
        SceneUtilities.changeSceneTo(SceneList.mainMenu);

        CommandHandler.broadcastResult("PLAYER_LEFT", Player.localPlayer.getNetworkID());
        String topicRoomAmount = "DiceLegend/" + LobbyState.current.lobbyName + "/room_amount";

        RunService.mqtt.subscribe(topicRoomAmount, (t, msg) -> {
            if (!RunService.mqtt.isConnected())
                return;
            try {
                if (!isMinus.getAndSet(true)) {
                    int addAmount = Math.max(0, Integer.parseInt(msg) - 1);
                    RunService.mqtt.publishRetained(topicRoomAmount, String.valueOf(addAmount));
                    RunService.mqtt.unsubscribe(topicRoomAmount);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                SwingUtilities.invokeLater(() -> {
                    if (RunService.mqtt.isConnected())
                        RunService.mqtt.disconnect();
                });
            }
        });
        GameState.currentGame = null;
        LobbyState.destroy();
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

// class ChangeNamePopUp extends JPanel {
// private Image background = ImagePreload.get("mainMenu-join-popup.png");
// private final GameButton changeButton = new GameButton("",
// "mainMenu-popup-join.png", "mainMenu-popup-join-hover.png");
// private final GameButton closeButton = new GameButton("",
// "mainMenu-popup-close.png", "mainMenu-popup-close.png");
// private final JTextField textField = new JTextField();

// public ChangeNamePopUp() {
// this.setBounds(960-background.getWidth(null)/2,
// 540-background.getHeight(null)/2, background.getWidth(null),
// background.getHeight(null));
// this.setBackground(null);
// this.setLayout(null);
// this.setVisible(false);

// this.add(changeButton);
// changeButton.setBounds(50, 300, 401, 71);
// changeButton.setOnButtonClicked(() -> {
// Player.setLocalPlayerName(textField.getText());
// this.setVisible(false);
// System.out.println(Player.getLocalPlayerName() + " : Player name displayed");
// });

// this.add(textField);
// textField.setBackground(null);
// textField.setForeground(Color.white);
// textField.setFont(new Font("Arial", Font.PLAIN, 50));
// textField.setHorizontalAlignment(JTextField.CENTER);
// textField.setBounds(50, 160, 401, 100);
// textField.setBorder(null);

// this.add(closeButton);
// closeButton.setBounds(background.getWidth(null) - 67, 0, 67, 67);
// closeButton.setOnButtonClicked(() -> {
// this.setVisible(false);
// });
// }

// @Override
// protected void paintComponent(Graphics g) {
// super.paintComponent(g);
// Graphics2D g2d = (Graphics2D) g;

// if (background != null) {
// g2d.drawImage(background, 0, 0, getWidth(), getHeight(), null);
// }
// }

// }