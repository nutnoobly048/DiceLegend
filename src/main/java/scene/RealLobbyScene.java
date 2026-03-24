package scene;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.*;

import graphicsUtilities.ImagePreload;
import graphicsUtilities.Scene;
import objectClass.ClickableObject;
import objectClass.GameButton;
import objectClass.GameModal;

public class RealLobbyScene extends Scene {
    private JLabel lobbyText = new JLabel("Lobby");
    private ClickableObject mapChanger = new ClickableObject();
    private GameButton exitButton = new GameButton("Exit", "lobby-exit-button.png", "lobby-exit-button.png");
    private GameModal MapSeletorModal = new GameModal(1200, 600, "modal-mapSelector-bg.png");

    public RealLobbyScene() {
        this.setBackground(ImagePreload.get("lobby_bg.png"));
        setupLobbyText();
        setupMapChanger();
        setupExitButton();
        setupMapSeletorModal();

        this.add(MapSeletorModal);
        this.add(lobbyText);
        this.add(mapChanger);
        this.add(exitButton);
    }

    private void setupMapListInModal() {

    }

    private void setupMapSeletorModal() {
        int centerXPos = 960 - MapSeletorModal.getPreferredSize().width / 2;
        int centerYPos = 540 - MapSeletorModal.getPreferredSize().height / 2;
        MapSeletorModal.setVisible(false);
        MapSeletorModal.setLocation(centerXPos, centerYPos);
    }

    private void setupExitButton() {
        int xPos = 1920 - exitButton.getPreferredSize().width - 10;
        int yPos = 35;
        exitButton.setBounds(xPos, yPos, exitButton.getPreferredSize().width, exitButton.getPreferredSize().height);
    }

    private void setupLobbyText() {
        lobbyText.setFont(new Font("Serif", Font.BOLD, 48));
        lobbyText.setForeground(Color.WHITE);
        lobbyText.setBounds(960-lobbyText.getPreferredSize().width/2, 35, lobbyText.getPreferredSize().width, lobbyText.getPreferredSize().height);
    }

    private void setupMapChanger() {
        int currentMapLabelNormalFontSize = 30;
        int currentMapLabelHoverFontSize = 35;
        int currentMapNameLabelNormalFontSize = 40;
        int currentMapNameLabelHoverFontSize = 45;
        int mapChangerYPos = 120;
        mapChanger.setLayout(new BorderLayout());
        
        JLabel currentMapLabel = new JLabel("Current Map");
        JLabel currentMapNameLabel = new JLabel("The Mysterious Jungle");

        currentMapLabel.setFont(new Font("Serif", Font.PLAIN, currentMapLabelNormalFontSize));
        currentMapLabel.setForeground(Color.WHITE);
        currentMapLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentMapNameLabel.setFont(new Font("Serif", Font.PLAIN, currentMapNameLabelNormalFontSize));
        currentMapNameLabel.setForeground(Color.WHITE);
        currentMapNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        mapChanger.add(currentMapLabel, BorderLayout.NORTH);
        mapChanger.add(currentMapNameLabel, BorderLayout.SOUTH);
        mapChanger.setBounds(960-mapChanger.getPreferredSize().width/2, mapChangerYPos, mapChanger.getPreferredSize().width, mapChanger.getPreferredSize().height);
        mapChanger.setOpaque(false);

        mapChanger.setOnMouseEntered(() -> {
            currentMapLabel.setFont(new Font("Serif", Font.PLAIN, currentMapLabelHoverFontSize));
            currentMapNameLabel.setFont(new Font("Serif", Font.PLAIN, currentMapNameLabelHoverFontSize));
            mapChanger.setBounds(960-mapChanger.getPreferredSize().width/2, mapChangerYPos, mapChanger.getPreferredSize().width, mapChanger.getPreferredSize().height);
        });
    
        mapChanger.setOnMouseExited(() -> {
            currentMapLabel.setFont(new Font("Serif", Font.PLAIN, currentMapLabelNormalFontSize));
            currentMapNameLabel.setFont(new Font("Serif", Font.PLAIN, currentMapNameLabelNormalFontSize));
            mapChanger.setBounds(960-mapChanger.getPreferredSize().width/2, mapChangerYPos, mapChanger.getPreferredSize().width, mapChanger.getPreferredSize().height);
        });
    
        mapChanger.setOnButtonClicked(() -> {
            System.out.println("Modal map show.");
            MapSeletorModal.setVisible(true);;
        });
    }
}

class mapListInModal extends ClickableObject {
    private Image mapImage;
    private JLabel mapNameLabel;
    private JLabel mapDescriptonLabel;

    public mapListInModal() {
        this("button.png", "", "");
    }

    public mapListInModal(String mapImageName, String name, String description) {
        this.mapImage = ImagePreload.get(mapImageName);
        this.mapNameLabel = new JLabel(name);
        this.mapDescriptonLabel = new JLabel(description);

        
    }

    public void setName(String newName) {
        this.mapNameLabel.setText(newName);
    }

    public void setDescription(String newDescription) {
        this.mapDescriptonLabel.setText(newDescription);
    }
}