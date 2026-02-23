package service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

//เป็นนาฬิกาส่วนกลางที่จะ Update เวลาและ Flow ในเกม
public class RunService {

    private static RunService runService;
    private static boolean isRunning = false;

    private final static List<Consumer<Double>> listenersMethod = new ArrayList<>();

    //private to prevent object creation
    private RunService() {}

    //Get RunService
    public static RunService GetService() {
        if (runService == null) {
            runService = new RunService();
        }
        return runService;
    }

    public static void subscribeListener(Consumer<Double> env) {
        listenersMethod.add(env);
    }


    public static void startRunService() {
        if (isRunning) {
            System.out.println("RunService was already run!");
            return;
        }

        isRunning = true;

    }

    private static void acceptDeltaTime(double deltaTime) {
        for (Consumer<Double> method : listenersMethod) {
            method.accept(deltaTime);
        }
    }



}
