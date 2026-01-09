package org.example.database;

//JDBC: Java Database Connectivity, ő fordít a java és az sql alkalmazások között, tolmács

import java.sql.Connection; //kkapcsolat az ab-al, sql parancsok a java és az ab között
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbManager {
//h2 automatikusan létrehozza az ab-t
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

    public static void saveWin(String playerName) {
    //próbálunk frissíteni, ha már létezik az adott név
        String updateSql = """
        UPDATE player_wins
        SET numberOfWins = numberOfWins + 1
        WHERE name = ?
        """;
        //ha még nem létezik, bele tesszük és 1-et írunk a győzelemhez
        String insertSql = """
        INSERT INTO player_wins (name, numberOfWins)
        VALUES (?, 1)
        """;

        try (Connection conn = getConnection()) {

            try (PreparedStatement update = conn.prepareStatement(updateSql)) {
                update.setString(1, playerName);
                int affectedRows = update.executeUpdate();

        //affectedRows: eltárolja, hányszor volt tábla frissítés

        //ha nem volt, insert történik, újként bekerül a név és a pont
                if (affectedRows == 0) {
                    try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                        insert.setString(1, playerName);
                        insert.executeUpdate();
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Győzelem mentése sikertelen", e);
        }
    }


    public static void printHighScore() {
        String sql = """
        SELECT name, numberOfWins
        FROM player_wins
        ORDER BY numberOfWins DESC
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- High Score ---");

            while (rs.next()) {
                String name = rs.getString("name");
                int wins = rs.getInt("numberOfWins");
                System.out.println(name + "\t-\t" + wins + " győzelem");
            }
            System.out.println("------------------\n");

        } catch (Exception e) {
            throw new RuntimeException("High score lekérdezés hiba", e);
        }
    }


    public static void initDatabase() {
        // ha még nem létezik a player_highscore, létrehozz, ha igen, megnyitja
        // létrejönnek az oszlopok a megadott tulajdonságokkal
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
