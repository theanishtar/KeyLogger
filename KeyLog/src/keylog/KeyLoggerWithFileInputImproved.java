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

public class KeyLoggerWithFileInputImproved extends JFrame {
    private JTextArea textArea;
    private BufferedWriter writer;
    private JTextField filePathField;

    public KeyLoggerWithFileInputImproved() {
        // Tạo cửa sổ JFrame
        super("Key Logger");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel chứa TextField và Button
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        // TextField để nhập đường dẫn tệp
        filePathField = new JTextField();
        filePathField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startLogging();
            }
        });
        inputPanel.add(filePathField, BorderLayout.CENTER);

        // Button để bắt đầu ghi log
        JButton startButton = new JButton("Start Logging");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startLogging();
            }
        });
        inputPanel.add(startButton, BorderLayout.EAST);

        getContentPane().add(inputPanel, BorderLayout.NORTH);

        // Tạo JTextArea để hiển thị các phím được nhấn
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Thiết lập focusable để nhận sự kiện từ bàn phím
        setFocusable(true);
    }

    private void startLogging() {
        String filePath = filePathField.getText().trim();
        if (filePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a file path.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Tạo luồng ghi file
            writer = new BufferedWriter(new FileWriter(filePath));

            // Sử dụng KeyBindings để bắt sự kiện phím nhấn
            textArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "logEnter");
            textArea.getActionMap().put("logEnter", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    logKey('\n');
                }
            });

            textArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "logBackspace");
            textArea.getActionMap().put("logBackspace", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    logKey('\b');
                }
            });

            textArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "logDelete");
            textArea.getActionMap().put("logDelete", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    logKey('\u007F');
                }
            });

            // Tắt TextField và Button sau khi đã bắt đầu ghi log
            filePathField.setEditable(false);
            getContentPane().remove(filePathField);
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
                KeyLoggerWithFileInputImproved keyLogger = new KeyLoggerWithFileInputImproved();
                keyLogger.setVisible(true);
            }
        });
    }
}
