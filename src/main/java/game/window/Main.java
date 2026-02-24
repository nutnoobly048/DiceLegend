// Dummy file to ensure src directory is tracked
package game.window;

import service.RunService;
import misc.TestInputInRUn;
import service.UserInput;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        RunService runService = RunService.GetService();
        runService.start();
//        JFrame frame = new JFrame();

        TestInputInRUn test = new TestInputInRUn();
//        JPanel p = new JPanel();
//        UserInput listener = new UserInput();
//        p.addKeyListener(listener);
//
//        p.setFocusable(true);
//        p.requestFocusInWindow();
//
//        frame.add(p);
//        frame.setVisible(true);
    }
}
