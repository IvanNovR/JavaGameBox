package ru.ivannovr.utils;

import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private final String url;
    private final String user;
    private final String password;

    public DatabaseManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        initializeDatabase();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private void initializeDatabase() {
        String createUsersTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                username TEXT PRIMARY KEY,
                password TEXT NOT NULL
            )
        """;
        String createLeaderboardTableSQL = """
            CREATE TABLE IF NOT EXISTS leaderboard (
                username TEXT,
                game TEXT,
                score INTEGER DEFAULT 0,
                record_time TIMESTAMP,
                PRIMARY KEY (username, game),
                FOREIGN KEY (username) REFERENCES users(username)
            )
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTableSQL);
            stmt.execute(createLeaderboardTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean registerUser(String username, String password) {
        String query = """
            INSERT INTO users (username, password) VALUES (?, ?) ON CONFLICT (username) DO NOTHING
        """;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                initializeLeaderboard(username);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void initializeLeaderboard(String username) {
        String query = """
            INSERT INTO leaderboard (username, game, score, record_time) VALUES (?, ?, 0, NULL)
        """;
        String[] games = {"Snake", "Dinosaur", "Pacman"};
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (String game : games) {
                pstmt.setString(1, username);
                pstmt.setString(2, game);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticateUser(String username, String password) {
        String query = """
            SELECT password FROM users WHERE username = ?
        """;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password").equals(password);
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public @Nullable List<Map<String, Object>> getLeaderboard(String game) {
        String query = """
            SELECT * FROM leaderboard WHERE game = ? ORDER BY score DESC LIMIT 10
        """;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, game);
            ResultSet rs = pstmt.executeQuery();

            List<Map<String, Object>> result = new ArrayList<>();
            while (rs.next()) {
                result.add(parseLeaderBoard(rs));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public @Nullable Map<String, Object> getUserLeaderboard(String username, String game) {
        String query = """
            SELECT * FROM leaderboard WHERE username = ? AND game = ? LIMIT 1
        """;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, game);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) return parseLeaderBoard(rs);
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, Object> parseLeaderBoard(ResultSet rs) throws SQLException {
        Map<String, Object> map = new HashMap<>();
        map.put("username", rs.getString("username"));
        map.put("game", rs.getString("game"));
        map.put("score", rs.getInt("score"));
        map.put("record_time", rs.getTimestamp("record_time"));
        return map;
    }

    public void updateScore(String username, String game, int score) {
        String query = """
            INSERT INTO leaderboard (username, game, score, record_time)
            VALUES (?, ?, ?, CURRENT_TIMESTAMP)
            ON CONFLICT (username, game)
            DO UPDATE SET
                score = EXCLUDED.score,
                record_time = CURRENT_TIMESTAMP;
        """;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, game);
            pstmt.setInt(3, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
