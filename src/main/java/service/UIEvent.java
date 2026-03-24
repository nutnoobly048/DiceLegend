package service;

public class UIEvent {
    public static void HandleUIEvent(String[] args) {
        String mainAction = args[0];
        switch (mainAction) {
            case "PRINTIFWORK" -> System.out.println("SHOW CARD");
            case "WAITFOR" -> {}
            case "CARDPOPUP" -> {}
        }
    }
}
