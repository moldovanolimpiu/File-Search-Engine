package fse;

import org.apache.tika.Tika;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;


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


    public CrawlerReport crawlFiles(String path) throws IOException, NoSuchAlgorithmException, SQLException {
        Path start = Paths.get(path);
        MessageDigest mdigest = MessageDigest.getInstance("MD5");

        Tika tika = new Tika();
        FileRepository fileRepository = new FileRepository();
        AtomicInteger countTextFilesFound = new AtomicInteger();
        AtomicInteger countFileInsertions = new AtomicInteger();
        AtomicInteger countFileUpdates = new AtomicInteger();
        int countFileDeletions;


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
                                int val = fileRepository.insertDatabase(fileMetadata);
                                countTextFilesFound.addAndGet(1);
                                if(val == 1){
                                    countFileInsertions.addAndGet(1);
                                }else{
                                    if(val == 2){
                                        countFileUpdates.addAndGet(1);
                                    }
                                }

                            }
                        }
                        catch(IOException ignored){} catch (SQLException e) {
                            System.out.println("FILE CRAWLER: SQL ERROR");
                        }
                    });
        }catch(IOException e){
            e.printStackTrace();
        }
        countFileDeletions = fileRepository.purgeNonExistentFiles();

        return new CrawlerReport(countTextFilesFound.get(),countFileInsertions.get(),countFileDeletions, countFileUpdates.get());

    }


}
