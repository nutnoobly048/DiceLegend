package service;
//Class ที่ช่วยให้ Developer / Designer assign ปุ่มบน keyboard ได้เพื่อใช้งาน Event แทน ActionListener
//E = UserInput.assignKey("E")
// if (E) {
//  sout("key pressed")
// }

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class UserInput implements KeyListener{

    private static HashMap<Integer, Boolean> keyMap = new HashMap<Integer, Boolean>();
    private static int keyCode;

//    public static void main(String[] args) {
//        JFrame frame = new JFrame("KeyListener Example");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(300, 200);
//
//        JPanel p = new JPanel();
//        UserInput listener = new UserInput();
//        p.addKeyListener(listener);
//
//        p.setFocusable(true);
//        p.requestFocusInWindow();
//
//        frame.add(p);
//        frame.setVisible(true);
//
//
//    }


    public UserInput(){
        System.out.println("User Input Initialized");
    }

    // Listen To KeyTyped
    @Override
    public void keyTyped(KeyEvent e) {

        char c = e.getKeyChar();
        //System.out.println("KeyLOG - Key typed: " + c);

    }

    public static boolean isKeyHold(int keyCode) {
        if (keyMap.containsKey(keyCode)) {
            return keyMap.get(keyCode);
        }
        return false;
    }


    // Listen to KeyPress As a key code
    @Override
    public void keyPressed(KeyEvent e) {
        keyCode = e.getKeyCode();
        //System.out.println("KeyLOG - Key pressed: " + KeyEvent.getKeyText(keyCode) + "  | Code : " + keyCode);
        if (!keyMap.containsKey(keyCode)) {
            keyMap.put(keyCode, true);
        }
        else {
            keyMap.replace(keyCode, true);
        }
    }

    // Listen to Key Release As a key code
    @Override
    public void keyReleased(KeyEvent e) {

        keyCode= e.getKeyCode();
        //System.out.println("KeyLOG - Key release: " + KeyEvent.getKeyText(keyCode));
        if (keyMap.containsKey(keyCode)) {
            keyMap.replace(keyCode, false);
        }
    }

    //Example for key press checking
    public static boolean isPressE() {
        return keyCode == KeyEvent.VK_E;
    }
}
