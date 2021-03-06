package UDPServer;

import javax.swing.*;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;

public class UDPServer {
    private static final int PIECES_OF_FILE_SIZE = 1024 * 32;
    private DatagramSocket serverSocket;
    private final int port = 9998;
    private final String ip = "192.168.1.18";
    static Gui frame;

    public static void main(String[] args) {
        final JFileChooser fileDialog = new JFileChooser();
        final String defaultDir = "E:\\Documents\\bach khoa\\CSNM\\fileDemo\\server";
        fileDialog.setCurrentDirectory(new File(defaultDir));
        fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        frame = new Gui();
        frame.setVisible(true);
        frame.defaultDirTextField.setText(defaultDir);
        frame.pickDefaultDirButton.addActionListener(e -> {
            int returnVal = fileDialog.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileDialog.getSelectedFile();
                frame.defaultDirTextField.setText(file.getPath());
            }
        });

        UDPServer udpServer = new UDPServer();
        udpServer.openServer();
    }

    private void openServer() {
        try {
            serverSocket = new DatagramSocket(port, InetAddress.getByName(ip));
            System.out.println("Server is opened on port " + port);
            while (true) {
                receiveFile();
            }
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Ops",
                    JOptionPane.ERROR_MESSAGE
            ); }
    }

    public void receiveFile() {
        byte[] receiveData = new byte[PIECES_OF_FILE_SIZE];
        DatagramPacket receivePacket;

        try {
            // Cho nhan file
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            System.out.println("receive file");

            // Nhan goi data dau tien co chua thong tin c???a file s???p truy???n
            // Gom:
            // - fileSize: dung luong file
            // - piecesOfFile: so luong manh cua file sau khi duoc chia nho
            // - lastByteLength: do dai du lieu cua manh cuoi cung
            InetAddress inetAddress = receivePacket.getAddress();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(receivePacket.getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            FileInfo fileInfo = (FileInfo) objectInputStream.readObject();

            // Show thong bao
            // Yes: Doc va viet file
            // No: ko lam gi ca
            int dialogResult = showDialog(inetAddress.getHostAddress(), fileInfo.getFilename());
            if (dialogResult == JOptionPane.YES_OPTION) {
                senResponse("YES", receivePacket);

                // Cap nhat lai UI
                frame.updateFileInfoUI(fileInfo);

                // Tao fileReceive v?? buffer de ghi file
                System.out.println("Receiving file...");
                File fileReceive = new File(frame.defaultDirTextField.getText() +"\\"+ fileInfo.getFilename());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileReceive));

                frame.updateProgressBarUI(0,0);
                // Lap qua tat ca cac mieng: Nhan -> ghi data
                for (int i = 0; i < (fileInfo.getPiecesOfFile() - 1); i++) {
                    receivePacket = new DatagramPacket(receiveData, receiveData.length, inetAddress, port);
                    serverSocket.receive(receivePacket);
                    bos.write(receiveData, 0, PIECES_OF_FILE_SIZE);
                    frame.updateProgressBarUI((i + 1) * PIECES_OF_FILE_SIZE, fileInfo.getFileSize());
                    System.out.println("Receiving file..." + i );
                }

                // Ghi doan du lieu con lai
                receivePacket = new DatagramPacket(receiveData, receiveData.length, inetAddress, port);
                serverSocket.receive(receivePacket);
                bos.write(receiveData, 0, fileInfo.getLastByteLength());
                bos.flush();
                bos.close();
                frame.updateProgressBarUI((int) fileInfo.getFileSize(), fileInfo.getFileSize());
                senResponse("COMPLETE", receivePacket);
                JOptionPane.showMessageDialog(
                        null,
                        "Nh???n file th??nh c??ng",
                        "Ops",
                        JOptionPane.ERROR_MESSAGE
                );
                System.out.println("Done!");
            } else {
                senResponse("NO", receivePacket);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Ops",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    void senResponse(String response, DatagramPacket datagramPacket) throws IOException {
        DatagramPacket responsePacket = new DatagramPacket(response.getBytes(),
                response.length(),
                datagramPacket.getAddress(),
                datagramPacket.getPort());
        serverSocket.send(responsePacket);
    }

    int showDialog(String ipSend, String fileName) {
        return JOptionPane.showConfirmDialog(
                null,
                ipSend + " mu???n g???i cho b???n file: " + fileName + "\nB???n c?? mu???n nh???n ko?",
                "Warning",
                JOptionPane.YES_NO_OPTION
        );
    }
}
