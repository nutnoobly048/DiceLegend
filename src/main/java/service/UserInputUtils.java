package service;
//เป็น wrapper class ที่เพิ่ม operation ให้กับการกดปุ่ม (Prototype)
//E = UserInput.assignKey("E")
//EHold = UserInputUtils.keyHold(E, 1) 1วิ
public class UserInputUtils {

    public UserInputUtils() {
    }

    public void listenESC(int keyCode) {
        if (keyCode == 27) {
            System.out.println("ESC Is pressed");
        }
    }

    public void listtenE(int keyCode) {
        if (keyCode == 0) {
            System.out.println("E is press");
        }
    }

}
