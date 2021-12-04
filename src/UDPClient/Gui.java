package UDPClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class Gui extends JFrame {
    private static final long serialVersionUID = 1L;

    JTextField filePathTextField;
    JButton showFileDialogButton;
    JButton transferFileButton;
    JProgressBar progressBar;
    JLabel sendingInfo;
    JLabel fileNameLabel;
    JLabel filePathLabel;
    JLabel fileSizeLabel;

    public Gui() {
        this.setTitle("Client - Truyền file bằng giao thức UDP");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 900, 600);
        this.setSize(560, 342);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(32, 10, 32, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel headerLabel;
        headerLabel = new JLabel("PHÍA CLIENT", JLabel.CENTER);
        headerLabel.setBorder(new EmptyBorder(0, 0, 32, 0));
        headerLabel.setFont(new Font("Serif", Font.BOLD, 22));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(headerLabel);

        Panel panelFileSelect = new Panel();
        filePathTextField = new JTextField(32);
        filePathTextField.setPreferredSize(new Dimension(100, 26));
        panelFileSelect.add(filePathTextField);
        showFileDialogButton = new JButton("Chọn File");
        showFileDialogButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFileSelect.add(showFileDialogButton);
        transferFileButton = new JButton("Truyền File");
        transferFileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFileSelect.add(transferFileButton);
        panel.add(panelFileSelect);

        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(100, 22));
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        panel.add(progressBar);

        sendingInfo = new JLabel("__/__ bytes", JLabel.CENTER);
        sendingInfo.setBorder(new EmptyBorder(8, 0, 0, 0));
        sendingInfo.setFont(new Font("Serif", Font.PLAIN, 16));
        sendingInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(sendingInfo);

        fileNameLabel = new JLabel("File name: ", JLabel.CENTER);
        fileNameLabel.setBorder(new EmptyBorder(16, 0, 0, 0));
        fileNameLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        fileNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(fileNameLabel);

        filePathLabel = new JLabel("File path: ", JLabel.CENTER);
        filePathLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        filePathLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(filePathLabel);

        fileSizeLabel = new JLabel("File size: ", JLabel.CENTER);
        fileSizeLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        fileSizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(fileSizeLabel);

        add(panel);
    }

    void updateInfoUI(File file) {
        fileNameLabel.setText("File name: " + file.getName());
        filePathLabel.setText("File path: " + file.getPath());
        fileSizeLabel.setText("File size: " + file.length());
        progressBar.setValue(0);
        sendingInfo.setText("__/__ bytes");
    }

    void updateProgressBarUI(int sendingSize, long fileSize) {
        if (fileSize == 0) {
            progressBar.setValue(0);
            sendingInfo.setText("__/__ bytes");
        } else {
            int percent = (int) ((sendingSize / (double) fileSize) * 100);
            if (percent > 100) percent = 100;
            progressBar.setValue(percent);
            sendingInfo.setText(
                    percent
                            + "%" + "  |  "
                            + sendingSize
                            + " / " + fileSize + " Bytes"
            );
        }
    }
}