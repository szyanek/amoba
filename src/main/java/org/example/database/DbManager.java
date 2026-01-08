package org.example.database;

//JDBC: Java Database Connectivity, ő fordít a java és az sql alkalmazások között, tolmács

import java.sql.Connection; //kkapcsolat az ab-al, sql parancsok a java és az ab között
import java.sql.DriverManager; //
import java.sql.Statement;

public class DbManager {

    private static final String URL = "jdbc:h2:file:./data/player_highscore";
    private static final String USER = "sa"; //sa a h2 adabbázis alapértelmezett usere, minden joggal rendelkezik
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {//kapcsolat felépítése, elérési út, felh.név, jelszó hármassal
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException("DB kapcsolat nem jött létre", e);
        }
    }

    public static void initDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS player_wins (
                name VARCHAR(100) PRIMARY KEY,
                numberOfWins INT NOT NULL
            )
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (Exception e) {
            throw new RuntimeException("DB inicializálás hiba", e);
        }
    }
}
