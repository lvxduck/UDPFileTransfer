package UDPServer;

import java.io.Serializable;

public class FileInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String filename;
    private final long fileSize;
    private final int piecesOfFile;
    private final int lastByteLength;

    public FileInfo(String filename, long fileSize, int piecesOfFile, int lastByteLength) {
        this.filename = filename;
        this.fileSize = fileSize;
        this.piecesOfFile = piecesOfFile;
        this.lastByteLength = lastByteLength;
    }

    public String getFilename() {
        return filename;
    }

    public long getFileSize() {
        return fileSize;
    }

    public int getPiecesOfFile() {
        return piecesOfFile;
    }

    public int getLastByteLength() {
        return lastByteLength;
    }
}
