package UDPClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Gui extends JFrame {
    private static final long serialVersionUID = 1L;

    JButton showFileDialogButton;
    JProgressBar progressBar;
    JLabel sendingInfo;
    JLabel fileNameLabel;
    JLabel filePathLabel;
    JLabel fileSizeLabel;

    public Gui() {
        this.setTitle("TRUYỀN FILE UDP_CLIENT");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 969, 660);
        this.setSize(600, 420);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(32, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel headerLabel;
        headerLabel = new JLabel("TRUYỀN FILE UDP_CLIENT", JLabel.CENTER);
        headerLabel.setBorder(new EmptyBorder(0, 0, 32, 0));
        headerLabel.setFont(new Font("Serif", Font.BOLD, 22));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(headerLabel);

        showFileDialogButton = new JButton("Chọn File");
        showFileDialogButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(showFileDialogButton);

        progressBar = new JProgressBar();
        progressBar.setBorder(new EmptyBorder(32, 0, 0, 0));
        progressBar.setValue(100);
        panel.add(progressBar);

        sendingInfo = new JLabel("", JLabel.CENTER);
        sendingInfo.setBorder(new EmptyBorder(8, 0, 0, 0));
        sendingInfo.setFont(new Font("Serif", Font.PLAIN, 16));
        sendingInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(sendingInfo);

        fileNameLabel = new JLabel("File name: ", JLabel.CENTER);
        fileNameLabel.setBorder(new EmptyBorder(32, 0, 0, 0));
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
}