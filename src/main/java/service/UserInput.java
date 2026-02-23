package service;
//Class ที่ช่วยให้ Developer / Designer assign ปุ่มบน keyboard ได้เพื่อใช้งาน Event แทน ActionListener
//E = UserInput.assignKey("E")
// if (E) {
//  sout("key pressed")
// }

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UserInput implements KeyListener{

    //Main for testing
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
//    }
    //Main For testing


    public UserInput(){
    }

    // Listen To KeyTyped
    @Override
    public void keyTyped(KeyEvent e) {

        char c = e.getKeyChar();
        System.out.println("KeyLOG - Key typed: " + c);

    }

    // Listen to KeyPress As a key code
    @Override
    public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();
        System.out.println("KeyLOG - Key pressed: " + KeyEvent.getKeyText(keyCode) + "  | Code : " + keyCode);

    }

    // Listen to Key Release As a key code
    @Override
    public void keyReleased(KeyEvent e) {

        int keyCode= e.getKeyCode();
        System.out.println("KeyLOG - Key release: " + KeyEvent.getKeyText(keyCode));

    }

}
