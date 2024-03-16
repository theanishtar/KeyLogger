/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package keylog;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SwingKeyLogger implements KeyListener {
    private BufferedWriter writer;

    public SwingKeyLogger() {
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
            System.out.println(e.getKeyCode());
            // Ghi ký tự vào tệp
            writer.write(e.getKeyChar());
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Không làm gì khi phím được thả ra
    }

    public static void main(String[] args) {
        SwingKeyLogger keyLogger = new SwingKeyLogger();
        JFrame frame = new JFrame("Swing Key Logger");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(keyLogger); // Thêm key listener vào frame
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}
