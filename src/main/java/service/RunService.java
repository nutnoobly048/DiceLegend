package service;

import Gameplay.GameState;
import Gameplay.LobbyState;
import ServiceInterface.MQTTService;
import ServiceInterface.ProcessByRunService;
import graphicsUtilities.ImagePreload;
import graphicsUtilities.SceneUtilities;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
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


    //INITIALIZE NETWORK
    public static final MQTTService mqtt = new MQTTService();

    public static final ConcurrentLinkedQueue<String> intentQueue = new ConcurrentLinkedQueue<>();
    public static final ConcurrentLinkedQueue<String> resultQueue = new ConcurrentLinkedQueue<>();

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
        ImagePreload.preloadAllImage();
        AudioService.preloadAllClips();

        SceneUtilities.setCurrentGameFrame(mainGameFrame);

        if (isRunning) return;
        isRunning = true;
        //AI: Thread Debugger
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("=== Console Debugger Started ===");
            System.out.println("Type commands using your format (e.g., INTENT:Player1:MOVE:100:200)");
            System.out.println("Type 'exit' to stop the debugger.");

            while (true) {
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine();

                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Stopping console debugger...");
                        break;
                    }

                    if (LobbyState.current != null) {
                        System.out.println("Sent");
                        CommandHandler.sentIntent(input);
                    }
                }
            }

            scanner.close();
        }, "ConsoleDebugThread").start();


        new Thread(() -> {
            while (isRunning) {
                calculateDeltaTime();

                processResultQueue();
                processIntentQueue();
                for (ProcessByRunService process : registeredObject) {
                    process.OnUpdate(deltaTime);
                }

                for (Consumer<Double> method : functionalListeners) {
                    method.accept(deltaTime);
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

    private void processIntentQueue() {
        while (!intentQueue.isEmpty()) {
            String packet = intentQueue.poll();
            if (packet == null) continue;

            String[] parts = packet.split("\\s*:\\s*");

            if (parts.length >= 3 && parts[0].equals("INTENT")) {
                String senderID = parts[1];
                String mainAction = parts[2];

                String[] parameters = Arrays.copyOfRange(parts, 3, parts.length);
                CommandHandler.handleIntent(senderID, mainAction, parameters);
            } else {
                System.err.println("Invalid INTENT packet: " + packet);
            }
        }
    }

    private void processResultQueue() {
        while (!resultQueue.isEmpty()) {
            String packet = resultQueue.poll();
            if (packet == null) continue;

            String[] parts = packet.split("\\s*:\\s*");

            if (parts.length >= 3 && parts[0].equals("RESULT")) {
                String targetID = parts[1];
                String mainAction = parts[2];

                String[] parameters = Arrays.copyOfRange(parts, 3, parts.length);


                CommandHandler.handleResult(targetID, mainAction, parameters);
            } else {
                System.err.println("Invalid RESULT packet: " + packet);
            }
        }
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