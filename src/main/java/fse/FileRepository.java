package fse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FileRepository {

    String url = System.getenv("FSE_DB_URL");
    String user = System.getenv("FSE_DB_USER");
    String password = System.getenv("FSE_DB_PASS");
    
    Connection con = DriverManager.getConnection(url,user,password);


    public FileRepository() throws SQLException {
    }

    public int insertDatabase(FileMetadata metadata) throws SQLException {
        String sqlSearch = "SELECT * FROM files where path = ?";
        int returnVal = 0;
        try (PreparedStatement ps = con.prepareStatement(sqlSearch)) {
            ps.setString(1, metadata.getPath());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                String sqlInsert = "INSERT INTO files(path, filename, extension, size, hash, content, date_created, date_modified, exists_flag) VALUES" +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try(PreparedStatement psInsert = con.prepareStatement(sqlInsert)) {
                    psInsert.setString(1, metadata.getPath());
                    psInsert.setString(2, metadata.getFileName());
                    psInsert.setString(3, metadata.getFileExtension());
                    psInsert.setLong(4, metadata.getFileSize());
                    psInsert.setString(5, metadata.getHash());
                    psInsert.setString(6, metadata.getContent());
                    psInsert.setString(7, metadata.getDatecreated());
                    psInsert.setString(8, metadata.getDatemodified());
                    psInsert.setBoolean(9, true);
                    psInsert.executeUpdate();
                    returnVal = 1;
                }catch (SQLException e) {
                    System.out.println("INSERT: Insert statement failed");

                }

            }else{
                if(!Objects.equals(metadata.getHash(), rs.getString("hash"))){
                    String sqlUpdate = "UPDATE files SET hash = ?, content = ?, date_modified = ? WHERE path = ?";
                    PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
                    psUpdate.setString(1, metadata.getHash());
                    psUpdate.setString(2, metadata.getContent());
                    psUpdate.setString(3, metadata.getDatemodified());
                    psUpdate.setString(4, metadata.getPath());
                    psUpdate.executeUpdate();
                    System.out.println("Updated file" + metadata.getFileName() + " at " +metadata.getPath());
                    returnVal = 2;
                }
                String sqlFlag = "UPDATE files SET exists_flag = ? WHERE path = ?";
                PreparedStatement psFlag = con.prepareStatement(sqlFlag);
                psFlag.setBoolean(1, true);
                psFlag.setString(2, metadata.getPath());
                psFlag.executeUpdate();
            }
        }catch (SQLException e) {
            System.out.println("INSERT: Initial search statement failed");
        }
        return returnVal;
    }

    public int purgeNonExistentFiles() throws SQLException {
        Statement st = con.createStatement();
        int deletedCount;
        deletedCount = st.executeUpdate("DELETE FROM files WHERE exists_flag = false");
        st.executeUpdate("UPDATE files SET exists_flag = false");
        return deletedCount;
    }

    public List<FileMetadata> searchFilename(String query) throws SQLException {
        String[] arr = query.split(" ");
        String sql = "SELECT * FROM files WHERE (filename ILIKE ?)";
        int i;
        for(i = 1; i < arr.length; i++){
            sql = sql + " OR (filename ILIKE ?)";
        }
        System.out.println(sql);

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" +arr[0] + "%");

        for(i = 1; i < arr.length; i++){
            ps.setString(i+1, "%" +arr[i] + "%");

        }
        List<FileMetadata> files = new ArrayList<>();
        try(ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String path = rs.getString("path");
                String filename = rs.getString("filename");
                String extension = rs.getString("extension");
                long size = rs.getLong("size");
                String hash = rs.getString("hash");
                String content = rs.getString("content");
                String date_created = rs.getString("date_created");
                String date_modified = rs.getString("date_modified");
                files.add(new FileMetadata(path,filename,extension,size,hash,content,date_created,date_modified));


            }
        }catch (SQLException e) {
            System.out.println("SEARCH FILENAME: Search statement failed");
        }

        return files;

    }

    public List<FileMetadata> searchContent(String query) throws SQLException {
        String[] arr = query.split(" ");
        String sql = "SELECT * FROM files WHERE (content ILIKE ?)";
        int i;
        for(i = 1; i < arr.length; i++){
            sql = sql + " AND (content ILIKE ?)";
        }
        System.out.println(sql);

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" +arr[0] + "%");

        for(i = 1; i < arr.length; i++){
            ps.setString(i+1, "%" +arr[i] + "%");

        }

        List<FileMetadata> files = new ArrayList<>();
        try(ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String path = rs.getString("path");
                String filename = rs.getString("filename");
                String extension = rs.getString("extension");
                long size = rs.getLong("size");
                String hash = rs.getString("hash");
                String content = rs.getString("content");
                String date_created = rs.getString("date_created");
                String date_modified = rs.getString("date_modified");
                files.add(new FileMetadata(path,filename,extension,size,hash,content,date_created,date_modified));


            }
        }catch (SQLException e) {
            System.out.println("SEARCH CONTENT: Search statement failed");
        }
        return files;

    }

    public List<FileMetadata> searchPath(String query) throws SQLException {

        String sql = "SELECT * FROM files WHERE (path ILIKE ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" +query + "%");
        List<FileMetadata> files = new ArrayList<>();
        try(ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String path = rs.getString("path");
                String filename = rs.getString("filename");
                String extension = rs.getString("extension");
                long size = rs.getLong("size");
                String hash = rs.getString("hash");
                String content = rs.getString("content");
                String date_created = rs.getString("date_created");
                String date_modified = rs.getString("date_modified");
                files.add(new FileMetadata(path,filename,extension,size,hash,content,date_created,date_modified));
            }
        }catch (SQLException e) {
            System.out.println("SEARCH PATH: Search statement failed");
        }
        return files;
    }

    public List<FileMetadata> searchPathContent(QueryData queryData) throws SQLException {
        String[] contentArr = queryData.getContent().split(" ");
        String pathQuery = queryData.getPath();
        String sql = "SELECT * FROM files WHERE (path ILIKE ?) AND (content ILIKE ?)";
        int i;
        for(i = 1; i < contentArr.length; i++){
            sql = sql + " AND (content ILIKE ?)";
        }
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" +pathQuery + "%");
        ps.setString(2, "%" +contentArr[0] + "%");
        for(i = 1; i < contentArr.length; i++){
            ps.setString(i+2, "%" +contentArr[i] + "%");
        }
        List<FileMetadata> files = new ArrayList<>();
        try(ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String path = rs.getString("path");
                String filename = rs.getString("filename");
                String extension = rs.getString("extension");
                long size = rs.getLong("size");
                String hash = rs.getString("hash");
                String content = rs.getString("content");
                String date_created = rs.getString("date_created");
                String date_modified = rs.getString("date_modified");
                files.add(new FileMetadata(path,filename,extension,size,hash,content,date_created,date_modified));
            }
        }catch (SQLException e) {
            System.out.println("SEARCH PATH/CONTENT: Search statement failed");
        }
        return files;


    }

    public QueryData queryProcessor(String query){
        String[] arr = query.split(" ");

        String path = null;
        StringBuilder sb = new StringBuilder();


        int i = 0;
        while(i < arr.length){
            if(arr[i].startsWith("path:")){
                path = arr[i].substring(5);
                i++;
            }else if(arr[i].startsWith("content:")){
                    if(!sb.isEmpty()){
                        sb.append(" ");
                    }
                    sb.append(arr[i].substring(8));
                    i++;
                    while(i<arr.length && (!arr[i].startsWith("path:")) && !arr[i].startsWith("content:")){
                        sb.append(" ");
                        sb.append(arr[i]);
                        i++;
                    }
            }else{
                i++;
            }

        }
        String content = sb.toString();


        QueryData queryData = new QueryData(path,content);


        return queryData;
    }
    public List<FileMetadata> searchCompound(String query) throws SQLException {
        QueryData querydata = queryProcessor(query);
        List<FileMetadata> files = new ArrayList<>();

        if(querydata.getContent() == null && querydata.getPath() == null){
            return files;
        }else if(querydata.getPath() == null){
            files = searchContent(querydata.getContent());
            return files;
        }else if(querydata.getContent().isEmpty()){
            files = searchPath(querydata.getPath());
            return files;
        }else{
            files = searchPathContent(querydata);
            return files;
        }

    }

}
