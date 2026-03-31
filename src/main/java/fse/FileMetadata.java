package fse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class FileMetadata {

    private String path;
    private String fileName;
    private String fileExtension;
    private long fileSize;
    private String hash;
    private String content;
    private String datecreated;
    private String datemodified;

    public FileMetadata(Path p, String dirpath, File file, MessageDigest mdigest, String content) throws IOException {
        this.path = dirpath;
        this.fileName = p.getFileName().toString();
        this.fileExtension = this.fileName.substring(this.fileName.lastIndexOf(".")+1);
        this.fileSize = Files.size(p);
        this.hash = checksum(mdigest, file);
        this.content = content;
        this.datemodified = String.valueOf(Files.getLastModifiedTime(p));
        this.datecreated = String.valueOf(Files.getAttribute(p, "creationTime"));
    }



    public String checksum(MessageDigest digest, File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);

        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        while((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        fis.close();

        byte[] bytes = digest.digest();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getDatemodified() {
        return datemodified;
    }

    public void setDatemodified(String datemodified) {
        this.datemodified = datemodified;
    }

    @Override
    public String toString() {
        return "FileMetadata{" +
                "path='" + path + '\n' +
                ", fileName=" + fileName + '\n' +
                ", fileExtension=" + fileExtension + '\n' +
                ", fileSize=" + fileSize + '\n' +
                ", hash=" + hash + '\n' +
                //", content=" + content + '\'' +
                ", datecreated=" + datecreated + '\n' +
                ", datemodified=" + datemodified + '\n' +
                '}';
    }
}
