import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class KeyLog extends JFrame {
    private JTextArea textArea;
    private BufferedWriter writer;
    private JTextField filePathField;

    public KeyLog() {
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

            // Bắt sự kiện phím nhấn
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        logKey('\n'); // Ghi xuống dòng khi nhấn Enter
                    } else {
                        logKey(e.getKeyChar());
                    }
                    System.out.println(e.getKeyChar());
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
                KeyLog keyLogger = new KeyLog();
                keyLogger.setVisible(true);
            }
        });
    }
}
