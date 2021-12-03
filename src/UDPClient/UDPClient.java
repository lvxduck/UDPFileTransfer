package UDPClient;

import UDPServer.FileInfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.*;

public class UDPClient {
    private static final int PIECES_OF_FILE_SIZE = 1024 * 32;
    private DatagramSocket clientSocket;
    private final int serverPort = 6677;
    private final String serverHost = "localhost";
    static Gui frame;
    static String defaultDir = "D:\\Bach Khoa\\CSNM\\FIles Demo\\Client\\hello.txt";

    public static void main(String[] args) {
        final JFileChooser fileDialog = new JFileChooser();
        fileDialog.setCurrentDirectory(new File(defaultDir));
        frame = new Gui();
        frame.setVisible(true);
        frame.filePathTextField.setText(defaultDir);
        frame.showFileDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileDialog.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileDialog.getSelectedFile();
                    frame.filePathTextField.setText(file.getPath());
                    updateInfoUI(file);
//                    UDPClient udpClient = new UDPClient();
//                    udpClient.connectServer();
//                    udpClient.sendFile(file);
                } else {
                    frame.fileNameLabel.setText("Open command cancelled by user.");
                    frame.filePathLabel.setText("");
                    frame.fileSizeLabel.setText("");
                }
            }
        });
        frame.transferFileButton.addActionListener(e -> {
            if (!frame.filePathTextField.getText().isEmpty()) {
                UDPClient udpClient = new UDPClient();
                udpClient.connectServer();
                udpClient.sendFile(new File(frame.filePathTextField.getText()));
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Bạn chưa chọn file",
                        "Ops",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void connectServer() {
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void sendFile(File fileSend) {
        try {
            InputStream inputStream = new FileInputStream(fileSend);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            // Tinh toan so luong goi can gui va kich thuoc cua goi cuoi cung
            long fileLength = fileSend.length();
            int piecesOfFile = (int) (fileLength / PIECES_OF_FILE_SIZE);
            int lastByteLength = (int) (fileLength % PIECES_OF_FILE_SIZE);
            if (lastByteLength > 0) {
                piecesOfFile++;
            }

            // Tao fileInfo chua data can gui
            FileInfo fileInfo = new FileInfo(
                    fileSend.getName(),
                    fileSend.length(),
                    piecesOfFile,
                    lastByteLength
            );

            sendFileInfo(fileInfo);

            String response = getServerResponse();

            if (response.equals("YES")) {
                // Chia file thanh tung goi
                byte[][] fileBytes = new byte[piecesOfFile][PIECES_OF_FILE_SIZE];
                byte[] bytePart = new byte[PIECES_OF_FILE_SIZE];
                int count = 0;
                while (bufferedInputStream.read(bytePart, 0, PIECES_OF_FILE_SIZE) > 0) {
                    fileBytes[count++] = bytePart;
                    bytePart = new byte[PIECES_OF_FILE_SIZE];
                }
//                frame.progressBar.setValue(0);
//                frame.sendingInfo.setText("hello");
                updateProgressBarUI(0, 0);
                waitMillisecond(1000);
                // Gui du lieu cua file theo tung goi
                for (int i = 0; i < (count - 1); i++) {
                    DatagramPacket sendPacket = new DatagramPacket(
                            fileBytes[i], PIECES_OF_FILE_SIZE,
                            InetAddress.getByName(serverHost), serverPort
                    );
                    clientSocket.send(sendPacket);
                    updateProgressBarUI((i + 1) * PIECES_OF_FILE_SIZE, fileInfo.getFileSize());
//                    int sendingPercent = (int) ((i + 1) * (100.0 / fileInfo.getPiecesOfFile()));
//                    frame.progressBar.setValue(sendingPercent);
//                    frame.sendingInfo.setText(
//                            sendingPercent
//                                    + "%" + "  |  "
//                                    + (i + 1) * PIECES_OF_FILE_SIZE
//                                    + " / " + fileInfo.getFileSize() + "Kb"
//                    );
                    System.out.println("Sending file... " + i);
                    waitMillisecond(180);
                }
                // Gui nhung byte cuoi cung cua file
                DatagramPacket sendPacket = new DatagramPacket(
                        fileBytes[count - 1], PIECES_OF_FILE_SIZE,
                        InetAddress.getByName(serverHost), serverPort
                );
                clientSocket.send(sendPacket);
                waitMillisecond(40);
//                frame.progressBar.setValue(100);
//                frame.sendingInfo.setText(
//                        "100%" + "  |  " + fileInfo.getFileSize() + " / " + fileInfo.getFileSize() + "Kb"
//                );
                updateProgressBarUI((int) fileInfo.getFileSize(), fileInfo.getFileSize());
                bufferedInputStream.close();
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Server từ chối nhận file",
                        "Ops",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sent.");
    }

    private void sendFileInfo(FileInfo fileInfo) throws IOException {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(arrayOutputStream);
        oos.writeObject(fileInfo);
        DatagramPacket sendPacket = new DatagramPacket(
                arrayOutputStream.toByteArray(),
                arrayOutputStream.toByteArray().length,
                InetAddress.getByName(serverHost),
                serverPort
        );
        clientSocket.send(sendPacket);
        System.out.println("Sending file...");
    }

    private String getServerResponse() throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(
                new byte[1024],
                1024,
                InetAddress.getByName(serverHost),
                serverPort
        );
        clientSocket.receive(sendPacket);
        return new String(sendPacket.getData()).substring(0, sendPacket.getLength());
    }

    static void updateInfoUI(File file) {
        frame.fileNameLabel.setText("File name: " + file.getName());
        frame.fileNameLabel.paintImmediately(frame.fileNameLabel.getVisibleRect());
        frame.filePathLabel.setText("File path: " + file.getPath());
        frame.filePathLabel.paintImmediately(frame.filePathLabel.getVisibleRect());
        frame.fileSizeLabel.setText("File size: " + file.length());
        frame.fileSizeLabel.paintImmediately(frame.fileSizeLabel.getVisibleRect());
        frame.progressBar.setValue(40);
        frame.progressBar.paintImmediately(frame.progressBar.getVisibleRect());

    }

    void updateProgressBarUI(int sendingSize, long fileSize) {
        if (fileSize == 0) {
            frame.progressBar.setValue(0);
            frame.progressBar.paintImmediately(frame.progressBar.getVisibleRect());
            frame.sendingInfo.setText("__/__ bytes");
            frame.sendingInfo.paintImmediately(frame.sendingInfo.getVisibleRect());
        } else {
            frame.progressBar.setValue((int) ((sendingSize / fileSize)*100));
            frame.progressBar.paintImmediately(frame.progressBar.getVisibleRect());
            frame.sendingInfo.setText(
                    (int) ((sendingSize / fileSize)*100)
                            + "%" + "  |  "
                            + sendingSize
                            + " / " + fileSize + " Bytes"
            );
//            frame.sendingInfo.paintImmediately(frame.sendingInfo.getVisibleRect());
        }

    }

    public void waitMillisecond(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
