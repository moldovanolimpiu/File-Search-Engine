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

    public void insertDatabase(FileMetadata metadata) throws SQLException {
        String sqlSearch = "SELECT * FROM files where path = ?";

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
    }

    public void purgeNonExistentFiles() throws SQLException {
        Statement st = con.createStatement();

        st.executeUpdate("DELETE FROM files WHERE exists_flag = false");
        st.executeUpdate("UPDATE files SET exists_flag = false");
    }

    public List<String> searchFilename(String query) throws SQLException {
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
        List<String> filenameStrings = new ArrayList<>();
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
                boolean exists_flag = rs.getBoolean("exists_flag");
                filenameStrings.add(filename);
                //System.out.println("path: " + path);
                System.out.println("filename: " + filename);
                //System.out.println("extension: " + extension);
                //System.out.println("size: " + size);
                //System.out.println("hash: " + hash);
                //System.out.println("content: " + content);
                //System.out.println("date_created: " + date_created);
                //System.out.println("date_modified: " + date_modified);
                //System.out.println("exists_flag: " + exists_flag);

            }
        }catch (SQLException e) {
            System.out.println("SEARCH FILENAME: Search statement failed");
        }

        return filenameStrings;

    }

    public void searchContent(String query) throws SQLException {
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
                boolean exists_flag = rs.getBoolean("exists_flag");
                System.out.println("path: " + path);
                System.out.println("filename: " + filename);
                System.out.println("extension: " + extension);
                System.out.println("size: " + size);
                System.out.println("hash: " + hash);
                //System.out.println("content: " + content);
                System.out.println("date_created: " + date_created);
                System.out.println("date_modified: " + date_modified);
                System.out.println("exists_flag: " + exists_flag);

            }
        }catch (SQLException e) {
            System.out.println("SEARCH CONTENT: Search statement failed");
        }

    }

    public void addToDbTest() throws SQLException {
        String sql = "INSERT INTO files(path, filename, extension, size, hash, content, date_created, date_modified, exists_flag) VALUES" +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1,"filepathlol/randomfilename");
        ps.setString(2,"randomfilename");
        ps.setString(3,"lol");
        ps.setLong(4,29425);
        ps.setString(5,"checksum");
        ps.setString(6,"content");
        ps.setString(7,"20/12/2000");
        ps.setString(8,"21/12/2020");
        ps.setBoolean(9,true);
        ps.executeUpdate();


    }

    public void selectTest() throws SQLException {
        try {
            String sql = "SELECT * FROM files where filename = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,"randomfilenam");
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
                    boolean exists_flag = rs.getBoolean("exists_flag");
                    System.out.println("path: " + path);
                    System.out.println("filename: " + filename);
                    System.out.println("extension: " + extension);
                    System.out.println("size: " + size);
                    System.out.println("hash: " + hash);
                    System.out.println("content: " + content);
                    System.out.println("date_created: " + date_created);
                    System.out.println("date_modified: " + date_modified);
                    System.out.println("exists_flag: " + exists_flag);

                }
            }catch (SQLException es) {
                System.out.println("Item not found");
            }
        }catch (SQLException e) {
            System.out.println("Query failed");
        }
    }




}
