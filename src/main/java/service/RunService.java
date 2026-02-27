package service;

import ServiceInterface.ProcessByRunService;
import graphicsUtilities.ImagePreload;
import graphicsUtilities.SceneUtilities;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
//เครื่องยนต์หลัก (Game Loop)
//ทำหน้าที่เป็นหัวใจที่คอยสั่งให้ทุกอย่างในเกมขยับ
//DeltaTime: คำนวณเวลาที่ต่างกันในแต่ละเฟรม เพื่อให้เกมรันเร็วเท่ากันทุกเครื่อง
//addRunnable:ใช้สำหรับเพิ่ม Custom Method เข้าไปทำงานใน Loop ได้เลยโดยไม่ต้องสร้าง Class ใหม่
//addProcess: ใช้สำหรับเพิ่ม object ใดๆ ที่ implement จาก ProcessByRunService เพื่อให้มันรัน ~60 Frame Per Second
public class RunService {

    private static MainGame mainGameFrame;
    private static UserInput userInput;

    private static RunService instance;
    private static boolean isRunning = false;
    
    private final List<ProcessByRunService> registeredObject = new CopyOnWriteArrayList<>();
    private final List<Consumer<Double>> functionalListeners = new CopyOnWriteArrayList<>();

    private long lastTime = System.nanoTime();
    private double deltaTime;
    private double rawDeltaTime;

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


        mainGameFrame.addKeyListener(new UserInput());



        ImagePreload.preloadAllImage();

        SceneUtilities.setCurrentGameFrame(mainGameFrame);

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



                //สั่งให้ EDT Thread repaint ในทุกๆ Frame
                SwingUtilities.invokeLater(() -> {
                    mainGameFrame.repaint();
                });

                UserInput.updateAndSync();


                try {Thread.sleep(16);} catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "GameThread").start();
    }

    private void calculateDeltaTime() {
        long currentTime = System.nanoTime();
        rawDeltaTime = (currentTime - lastTime) / 1_000_000.0;
        deltaTime = rawDeltaTime / 1_000;
        lastTime = currentTime;
    }

    public double getDeltaTime() { return deltaTime; }

    public void stop() { isRunning = false; }

    public void setMainGameFrame(MainGame frame) {
        mainGameFrame = frame;
    }

}