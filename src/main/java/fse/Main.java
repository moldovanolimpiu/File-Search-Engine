package fse;

import fse.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, SQLException {


        System.out.println("URL: " + System.getenv("FSE_DB_URL"));
        System.out.println("USER: " + System.getenv("FSE_DB_USER"));


    }
}