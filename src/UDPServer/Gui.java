package UDPServer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Gui extends JFrame {
    JButton pickDefaultDirButton;
    JProgressBar progressBar;
    JTextField defaultDirTextField;
    JLabel receiveInfo;
    JLabel fileName;
    JLabel fileSize;

    public Gui(){
        this.setTitle("Server - Truyền file bằng giao thức UDP");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 969, 660);
        this.setSize(550, 320);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(32, 10, 32, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel headerLabel;
        headerLabel = new JLabel("PHÍA SERVER", JLabel.CENTER);
        headerLabel.setBorder(new EmptyBorder(0, 0, 32, 0));
        headerLabel.setFont(new Font("Serif", Font.BOLD, 22));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(headerLabel);

        Panel panelDefaultDir = new Panel();
        defaultDirTextField = new JTextField(38);
        defaultDirTextField.setPreferredSize(new Dimension(100, 26));
        panelDefaultDir.add(defaultDirTextField);
        pickDefaultDirButton = new JButton("Chọn thư mục");
        pickDefaultDirButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDefaultDir.add(pickDefaultDirButton);
        panel.add(panelDefaultDir);

        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(100, 22));
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        panel.add(progressBar);

        receiveInfo = new JLabel("__/__ bytes", JLabel.CENTER);
        receiveInfo.setBorder(new EmptyBorder(8, 0, 0, 0));
        receiveInfo.setFont(new Font("Serif", Font.PLAIN, 16));
        receiveInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(receiveInfo);

        fileName = new JLabel("File name: ", JLabel.CENTER);
        fileName.setBorder(new EmptyBorder(16, 0, 0, 0));
        fileName.setFont(new Font("Serif", Font.PLAIN, 16));
        fileName.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(fileName);

        fileSize = new JLabel("File size: ", JLabel.CENTER);
        fileSize.setFont(new Font("Serif", Font.PLAIN, 16));
        fileSize.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(fileSize);

        add(panel);
    }

    void updateFileInfoUI(FileInfo fileInfo) {
        fileName.setText("File name: " + fileInfo.getFilename());
        fileSize.setText("File size: " + fileInfo.getFileSize());
        progressBar.setVisible(true);
    }

    void updateProgressBarUI(int sendingSize, long fileSize) {
        if (fileSize == 0) {
            progressBar.setValue(0);
            receiveInfo.setText("__/__ bytes");
        } else {
            int percent = (int) ((sendingSize / (double) fileSize) * 100);
            if (percent > 100) percent = 100;
            progressBar.setValue(percent);
            receiveInfo.setText(
                    percent
                            + "%" + "  |  "
                            + sendingSize
                            + " / " + fileSize + " Bytes"
            );
        }
    }
}
