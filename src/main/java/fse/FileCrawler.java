package fse;

import org.apache.tika.Tika;
import fse.*;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;


public class FileCrawler {

    private String sizeFormat(long size) {

        //long var = 0;
        String appendage;
        int count = 0;
        while(size > 1023){
            size =size/1024;
            count++;
        }
        if(count == 0){
            appendage = size + " B";
        }else if(count == 1){
            appendage = size + " KB";
        }else if(count == 2){
            appendage = size + " MB";
        }else if(count == 3){
            appendage = size + " GB";
        }else{
            appendage = size + " TB";
        }
        return appendage;

    }


    public void crawlFiles(String path) throws IOException, NoSuchAlgorithmException, SQLException {
        Path start = Paths.get(path);
        MessageDigest mdigest = MessageDigest.getInstance("MD5");
        ChecksumGenerator checksumGenerator = new ChecksumGenerator();
        Tika tika = new Tika();
        FileRepository fileRepository = new FileRepository();


        try {
            Files.walk(start).filter(Files::isRegularFile)
                    .forEach(p -> {
                        try{
                            String dirpath = p.toString();
                            File file = new File(dirpath);
                            String type = tika.detect(file);
                            if(type.startsWith("text") || type.endsWith("json")) {
                                String content = Files.readString(p);

                                FileMetadata fileMetadata = new FileMetadata(p,dirpath,file,mdigest,content);
                                fileRepository.insertDatabase(fileMetadata);

                            }
                        }
                        catch(IOException ignored){} catch (SQLException e) {
                            System.out.println("FILE CRAWLER: SQL ERROR");
                        }
                    });
        }catch(IOException e){
            e.printStackTrace();
        }
        fileRepository.purgeNonExistentFiles();
    }


}
