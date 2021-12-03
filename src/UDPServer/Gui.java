package UDPServer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Gui extends JFrame {
    JButton pickDefaultDirButton;
    JProgressBar progressBar;
    JLabel defaultDir;
    JLabel receiveInfo;
    JLabel fileName;
    JLabel info2;
    JLabel fileSize;

    public Gui(){
        this.setTitle("NHẬN FILE UDP_SERVER");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(100, 100, 969, 660);
        this.setSize(600, 420);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(32, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel headerLabel;
        headerLabel = new JLabel("NHẬN FILE UDP_SERVER", JLabel.CENTER);
        headerLabel.setBorder(new EmptyBorder(0, 0, 32, 0));
        headerLabel.setFont(new Font("Serif", Font.BOLD, 22));
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(headerLabel);

        pickDefaultDirButton = new JButton("Chọn File");
        pickDefaultDirButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(pickDefaultDirButton);

        defaultDir = new JLabel("Default Directory: ", JLabel.CENTER);
        defaultDir.setBorder(new EmptyBorder(32, 0, 12, 0));
        defaultDir.setFont(new Font("Serif", Font.BOLD, 18));
        defaultDir.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(defaultDir);

        progressBar = new JProgressBar();
        progressBar.setValue(100);
        panel.add(progressBar);

        receiveInfo = new JLabel("", JLabel.CENTER);
        receiveInfo.setBorder(new EmptyBorder(8, 0, 0, 0));
        receiveInfo.setFont(new Font("Serif", Font.PLAIN, 16));
        receiveInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(receiveInfo);


        fileName = new JLabel("File name: ", JLabel.CENTER);
        fileName.setBorder(new EmptyBorder(32, 0, 0, 0));
        fileName.setFont(new Font("Serif", Font.PLAIN, 16));
        fileName.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(fileName);

//        info2 = new JLabel("File path: ", JLabel.CENTER);
//        info2.setFont(new Font("Serif", Font.PLAIN, 16));
//        info2.setAlignmentX(Component.CENTER_ALIGNMENT);
//        panel.add(info2);

        fileSize = new JLabel("File size: ", JLabel.CENTER);
        fileSize.setFont(new Font("Serif", Font.PLAIN, 16));
        fileSize.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(fileSize);

        add(panel);
    }
}
