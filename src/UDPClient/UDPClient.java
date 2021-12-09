package UDPClient;

import UDPServer.FileInfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class UDPClient {
    private static final int PIECES_OF_FILE_SIZE = 1024 * 32;
    private DatagramSocket clientSocket;
    private final int serverPort = 6677;
    private final String serverHost = "localhost";
    static Gui frame;
    static String defaultDir = "E:\\Documents\\bach khoa\\CSNM\\fileDemo\\client\\kitten.png";

    public static void main(String[] args) {
        final JFileChooser fileDialog = new JFileChooser();
        fileDialog.setCurrentDirectory(new File(defaultDir));
        frame = new Gui();
        frame.setVisible(true);
        frame.filePathTextField.setText(defaultDir);
        frame.showFileDialogButton.addActionListener(e -> {
            int returnVal = fileDialog.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileDialog.getSelectedFile();
                frame.filePathTextField.setText(file.getPath());
                frame.updateInfoUI(file);
            } else {
                frame.fileNameLabel.setText("Open command cancelled by user.");
                frame.filePathLabel.setText("");
                frame.fileSizeLabel.setText("");
            }
        });
        frame.transferFileButton.addActionListener(e -> {
            if (!frame.filePathTextField.getText().isEmpty()) {
                File file = new File(frame.filePathTextField.getText());
                if(file.exists() && !file.isDirectory()) {
                    UDPClient udpClient = new UDPClient();
                    udpClient.connectServer();
                    udpClient.sendFile(file);
                }else{
                    JOptionPane.showMessageDialog(
                            null,
                            "File không tồn tại!",
                            "Ops",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

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
        new Thread() {
            public void run() {
                try {
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

                    // Lắng nghe phản hồi của server
                    String response = getServerResponse();
                    if (response.equals("YES")) {
                        byte[][] fileBytes = getFileBytes(fileSend, piecesOfFile);
                        frame.updateProgressBarUI(0, 0);
                        waitMillisecond(1000);
                        // Gui du lieu cua file theo tung goi
                        for (int i = 0; i < piecesOfFile; i++) {
                            DatagramPacket sendPacket = new DatagramPacket(
                                    fileBytes[i], PIECES_OF_FILE_SIZE,
                                    InetAddress.getByName(serverHost), serverPort
                            );
                            clientSocket.send(sendPacket);
                            frame.updateProgressBarUI((i + 1) * PIECES_OF_FILE_SIZE, fileInfo.getFileSize());
                            System.out.println("Sending file... " + i);
                            waitMillisecond(180);
                        }
                        String lastResponse = getServerResponse();
                        if(lastResponse.equals("COMPLETE")){
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Gửi file thành công",
                                    "Ops",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                        waitMillisecond(40);
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Server từ chối nhận file",
                                "Ops",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(
                            null,
                            e.getMessage(),
                            "Ops",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
                System.out.println("Sent.");
            }
        }.start();
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

    private byte[][] getFileBytes(File fileSend, int piecesOfFile) throws IOException {
        InputStream inputStream = new FileInputStream(fileSend);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        byte[][] fileBytes = new byte[piecesOfFile][PIECES_OF_FILE_SIZE];
        byte[] bytePart = new byte[PIECES_OF_FILE_SIZE];
        int count = 0;
        while (bufferedInputStream.read(bytePart, 0, PIECES_OF_FILE_SIZE) > 0) {
            fileBytes[count++] = bytePart;
            bytePart = new byte[PIECES_OF_FILE_SIZE];
        }
        bufferedInputStream.close();
        return fileBytes;
    }

    public void waitMillisecond(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(
                    null,
                    e.getMessage(),
                    "Ops",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
