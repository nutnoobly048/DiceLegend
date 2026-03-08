package objectClass;

import ServiceInterface.Drawable;
import graphicsUtilities.ImagePreload;
import graphicsUtilities.SceneUtilities;

import java.awt.*;

public class AnimatedSprite extends GameObject implements Drawable {

    private Image spriteSheet;
    private int frameCount;       // จำนวนเฟรมทั้งหมดในรูป
    private int currentFrame = 0; // เฟรมปัจจุบันที่กำลังเล่นอยู่

    private int frameWidth;       // ความกว้างของ 1 เฟรม (คำนวณอัตโนมัติ)
    private int frameHeight;      // ความสูงของ 1 เฟรม (คำนวณอัตโนมัติ)

    private double timePerFrame;  // เวลาที่ต้องใช้ต่อ 1 เฟรม (วินาที)
    private double animationTimer = 0;

    public boolean playLooped = true;
    public boolean playRollback = false;

    private int playDirection = 1; // 1 = เดินหน้า, -1 = ถอยหลัง
    private boolean isPlaying = true;

    // Constructor 1
    public AnimatedSprite(String networkId, String imgFileName, int x, int y, int frameCount, double fps) {
        super(networkId);
        this.x = x;
        this.y = y;
        this.frameCount = frameCount;

        // ดึงรูปจาก ImagePreload
        this.spriteSheet = ImagePreload.get(imgFileName);
        if (this.spriteSheet == null) {
            this.spriteSheet = ImagePreload.get("blank.png");
        }

        // คำนวณขนาดของแต่ละ Frame
        this.frameWidth = this.spriteSheet.getWidth(null) / frameCount;
        this.frameHeight = this.spriteSheet.getHeight(null);

        setSpeed(fps);
    }

    // Constructor 2 มี True, False ข้างหลัง
    public AnimatedSprite(String networkId, String imgFileName, int x, int y, int frameCount, double fps, boolean playloop, boolean playrollback) {
        this(networkId, imgFileName, x, y, frameCount, fps); // เรียกใช้ Constructor 1
        this.playLooped = playloop;
        this.playRollback = playrollback;
    }
    // Method สำหรับเปลี่ยนความเร็ว
    public void setSpeed(double fps) {
        if (fps <= 0) {
            fps = 1;
        }


        this.timePerFrame = 1.0 / fps;
    }

    @Override
    public void OnUpdate(double deltaTime) {
        super.OnUpdate(deltaTime);

        // ถ้า Object ปิดการใช้งานอยู่, ไม่ได้อยู่ในฉากปัจจุบัน หรือกดหยุดเล่นไว้ ก็ไม่ต้องคำนวณ
        if (!isActive || currentScene != SceneUtilities.getCurrentGameScene() || !isPlaying) {
            return;
        }

        // แค่ 1 เฟรมไม่ต้อง Onupdate
        if (frameCount <= 1) {
            return;
        }

        animationTimer += deltaTime;

        // ถึงเวลาที่ต้องเปลี่ยนเฟรมแล้วหรือยัง
        if (animationTimer >= timePerFrame) {
            animationTimer -= timePerFrame;
            currentFrame += playDirection;

            if (playRollback) {
                // โหมดเล่นไป-กลับ
                if (currentFrame >= frameCount) {
                    currentFrame = frameCount - 2; // ถอยกลับมา 1 สเตป
                    playDirection = -1; // เปลี่ยนทิศเป็นถอยหลัง
                } else if (currentFrame < 0) {
                    if (playLooped) {
                        currentFrame = 1; // เด้งกลับไปเดินหน้า
                        playDirection = 1;
                    } else {
                        currentFrame = 0; // ถ้าไม่ Loop ก็ให้ค้างที่เฟรมแรกและหยุดเล่น
                        isPlaying = false;
                    }
                }
            } else {
                // โหมดเล่นปกติ (เดินหน้าอย่างเดียว)
                if (currentFrame >= frameCount) {
                    if (playLooped) {
                        currentFrame = 0; // วนกลับไปเฟรมศูนย์
                    } else {
                        currentFrame = frameCount - 1; // ค้างที่เฟรมสุดท้าย
                        isPlaying = false;
                    }
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (spriteSheet != null && isActive) {
            // sx, sy คือพิกัดบนรูปต้นฉบับ (Source) ที่เราจะตัดมา
            int sx1 = currentFrame * frameWidth;
            int sy1 = 0;
            int sx2 = sx1 + frameWidth;
            int sy2 = frameHeight;

            // dx, dy คือพิกัดบนหน้าจอ (Destination) ที่เราจะเอาไปแปะ
            int dx1 = this.x;
            int dy1 = this.y;
            int dx2 = dx1 + frameWidth;
            int dy2 = dy1 + frameHeight;

            // คำสั่งวาดภาพโดยตัดเฉพาะส่วนที่ต้องการ (ประสิทธิภาพสูงมาก)
            g2d.drawImage(spriteSheet, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
        }
    }

    // --- Methods ควบคุมการเล่น ---
    public void play() { isPlaying = true; }

    public void pause() { isPlaying = false; }

    public void reset() {
        currentFrame = 0;
        animationTimer = 0;
        playDirection = 1;
        isPlaying = true;
    }
}