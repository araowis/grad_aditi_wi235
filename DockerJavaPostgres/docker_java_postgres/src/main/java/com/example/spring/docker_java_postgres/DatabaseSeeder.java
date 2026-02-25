package com.example.spring.docker_java_postgres;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Random;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {

        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String db = System.getenv("DB_NAME");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASSWORD");

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + db;
        String insertSQL = "INSERT INTO users(name, email) VALUES (?, ?)";

        while (true) {
            try (Connection conn = DriverManager.getConnection(url, user, pass); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                String randomName = generateRandomString(8);
                String randomEmail = randomName.toLowerCase() + "@henlo.com";

                pstmt.setString(1, randomName);
                pstmt.setString(2, randomEmail);
                int rows = pstmt.executeUpdate();
                System.out.println("Inserted " + rows + " row(s): " + randomName);

            } 
            catch (Exception e) {
                e.printStackTrace();
            }

            Thread.sleep(5000);
        }
    }

    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}