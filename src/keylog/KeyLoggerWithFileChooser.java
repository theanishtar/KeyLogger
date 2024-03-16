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

public class KeyLoggerWithFileChooser extends JFrame {
    private JTextArea textArea;
    private BufferedWriter writer;

    public KeyLoggerWithFileChooser() {
        // Tạo cửa sổ JFrame
        super("Key Logger");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel chứa Button
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        // Button để bắt đầu ghi log và chọn thư mục lưu file
        JButton startButton = new JButton("Start Logging");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showSaveDialog(KeyLoggerWithFileChooser.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFolder = fileChooser.getSelectedFile();
                    startLogging(selectedFolder);
                }
            }
        });
        inputPanel.add(startButton, BorderLayout.CENTER);

        getContentPane().add(inputPanel, BorderLayout.NORTH);

        // Tạo JTextArea để hiển thị các phím được nhấn
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void startLogging(File folder) {
        try {
            // Tạo tên file dựa trên thời gian hiện tại
            String fileName = "log_" + System.currentTimeMillis() + ".txt";
            File logFile = new File(folder, fileName);

            // Tạo luồng ghi file
            writer = new BufferedWriter(new FileWriter(logFile));

            // Bắt sự kiện phím nhấn
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent e) {
                    if (e.getID() == KeyEvent.KEY_PRESSED) {
                        logKey(e.getKeyChar());
                    }
                    return false;
                }
            });

            // Tắt Button sau khi đã bắt đầu ghi log
            getContentPane().remove(0); // Xóa Button
            getContentPane().revalidate();
            getContentPane().repaint();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error opening file for writing.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logKey(char keyChar) {
        try {
            writer.write(keyChar);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        textArea.append(String.valueOf(keyChar));
    }

    public static void main(String[] args) {
        // Tạo và hiển thị cửa sổ
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                KeyLoggerWithFileChooser keyLogger = new KeyLoggerWithFileChooser();
                keyLogger.setVisible(true);
            }
        });
    }
}
