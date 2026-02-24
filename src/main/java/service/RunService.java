package service;

import ServiceInterface.ProcessByRunService;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class RunService {

    private static RunService instance;
    private static boolean isRunning = false;
    
    private final List<ProcessByRunService> registeredObject = new CopyOnWriteArrayList<>();
    private final List<Consumer<Double>> functionalListeners = new CopyOnWriteArrayList<>();

    private long lastTime = System.nanoTime();
    private double deltaTime;

    private RunService() {}

    public static RunService GetService() {
        if (instance == null) {
            instance = new RunService();
        }
        return instance;
    }


    public void addProcess(ProcessByRunService process) {
        process.OnCreate();
        registeredObject.add(process);
    }

    public void removeProcess(ProcessByRunService process) {
        registeredObject.remove(process);
    }

    public void addRunnable(Consumer<Double> method) {
        functionalListeners.add(method);
    }

    public void removeRunnable(Consumer<Double> method) {
        functionalListeners.remove(method);
    }

    public void start() {
        if (isRunning) return;
        isRunning = true;

        new Thread(() -> {
            while (isRunning) {
                calculateDeltaTime();


                for (ProcessByRunService process : registeredObject) {
                    process.OnUpdate(deltaTime);
                }

                for (Consumer<Double> method : functionalListeners) {
                    method.accept(deltaTime);
                }

                for (ProcessByRunService process : registeredObject) {
                    process.OnLateUpdate();
                }

                try {Thread.sleep(16);} catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "GameThread").start();
    }

    private void calculateDeltaTime() {
        long currentTime = System.nanoTime();
        deltaTime = (currentTime - lastTime) / 1_000_000.0;
        lastTime = currentTime;
    }

    public double getDeltaTime() { return deltaTime; }

    public void stop() { isRunning = false; }
}