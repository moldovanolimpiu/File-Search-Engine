package fse.FileData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
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
    private int rank_depth;
    private String dataccessed;

    public FileMetadata(Path p, String dirpath, File file, MessageDigest mdigest, String content) throws IOException {
        this.path = dirpath;
        this.fileName = p.getFileName().toString();
        this.fileExtension = this.fileName.substring(this.fileName.lastIndexOf(".")+1);
        this.fileSize = Files.size(p);
        this.hash = checksum(mdigest, file);
        this.content = content;
        this.datemodified = String.valueOf(Files.getLastModifiedTime(p));
        this.datecreated = String.valueOf(Files.getAttribute(p, "creationTime"));
        String[] pathSplit = this.path.split("\\\\");
        this.rank_depth = pathSplit.length;
    }

    public FileMetadata(String path, String fileName, String fileExtension, long fileSize, String hash, String content, String datecreated, String datemodified, int rank_depth) throws IOException {
        this.path = path;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.fileSize = fileSize;
        this.hash = hash;
        this.content = content;
        this.datecreated = datecreated;
        this.datemodified = datemodified;
        this.rank_depth = rank_depth;
        this.dataccessed = obtainDataAccess(this.path);

    }

    private String obtainDataAccess(String filePath) throws IOException {
        Path pathGen = Paths.get(filePath);
        BasicFileAttributes attrs = Files.readAttributes(pathGen, BasicFileAttributes.class);
        return attrs.lastAccessTime().toString();
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


    public String getFileName() {
        return fileName;
    }


    public String getFileExtension() {
        return fileExtension;
    }


    public long getFileSize() {
        return fileSize;
    }


    public String getHash() {
        return hash;
    }


    public String getContent() {
        return content;
    }


    public String getDatecreated() {
        return datecreated;
    }


    public String getDatemodified() {
        return datemodified;
    }

    public int getRank() {return rank_depth;}

    public String getDateccessed() {return dataccessed;}


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
