package scene;

import java.awt.Color;

import javax.swing.JTextArea;

import graphicsUtilities.FontLoader;
import graphicsUtilities.Scene;

public class CreditScene extends Scene {
    JTextArea textArea = new JTextArea();

    @Override
    public void onCreate() {
        this.setBackground(Color.black);

        this.add(textArea);
        textArea.append("จิรสิน เหมาะตัว 68070015\n" + 
                        "ชยากร อารีย์ 68070026\n" +
                        "ติณณภัทร์ สัตยา 68070048\n" +
                        "นัธวัฒน์ ทุมสา 68070082\n" +
                        "นัศรุน เจ๊ะหมะ 68070084\n" +
                        "พนิต คุณะเกษม 68070114\n" +
                        "ภัทรพล สนธิศิริ 68070134\n" +
                        "ภาณุพงศ์ คนครอง 68070136\n" +
                        "ภีดาเนตรศิลาอาจ 68070139\n" +
                        "เมธา ภัทรพิชญกุล 68070150\n"
                        );
        System.out.println(textArea.getPreferredSize());
        textArea.setForeground(Color.BLACK);
        textArea.setBounds(960-textArea.getPreferredSize().width/2, 540-textArea.getPreferredSize().height/2, textArea.getPreferredSize().width, textArea.getPreferredSize().height);
        textArea.setFont(FontLoader.getFont(40));
    }

    @Override
    public void onEnter() {

        
    }

    @Override
    public void onExit() {
        

    }
}
