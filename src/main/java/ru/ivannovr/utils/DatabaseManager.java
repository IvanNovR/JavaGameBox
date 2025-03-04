package ru.ivannovr.utils;

import org.jetbrains.annotations.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
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
        logger.debug("Connecting to database: {}", url);
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
            logger.info("Database initialized successfully");
        } catch (SQLException e) {
            logger.error("Failed to initialize database", e);
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
                logger.info("User registered: {}", username);
                return true;
            }
            logger.warn("User registration failed (username may already exist): {}", username);
            return false;
        } catch (SQLException e) {
            logger.error("Error registering user: {}", username, e);
            return false;
        }
    }

    private void initializeLeaderboard(String username) {
        String query = """
            INSERT INTO leaderboard (username, game, score, record_time) VALUES (?, ?, 0, NULL)
        """;
        String[] games = {"Snake", "FlappyBird", "Pacman"};
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (String game : games) {
                pstmt.setString(1, username);
                pstmt.setString(2, game);
                pstmt.executeUpdate();
            }
            logger.info("Leaderboard initialized for user: {}", username);
        } catch (SQLException e) {
            logger.error("Error initializing leaderboard for user: {}", username, e);
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
                boolean authenticated = rs.getString("password").equals(password);
                logger.info("Authentication attempt for {}: {}", username, authenticated ? "success" : "failed");
                return authenticated;
            }
            logger.warn("User not found: {}", username);
            return false;
        } catch (SQLException e) {
            logger.error("Error authenticating user: {}", username, e);
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
            logger.debug("Leaderboard retrieved for game: {}", game);
            return result;
        } catch (SQLException e) {
            logger.error("Error retrieving leaderboard for game: {}", game, e);
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

            if (rs.next()) {
                Map<String, Object> result = parseLeaderBoard(rs);
                logger.debug("User leaderboard retrieved for {} in game {}", username, game);
                return result;
            }
            logger.warn("No leaderboard entry found for {} in game {}", username, game);
            return null;
        } catch (SQLException e) {
            logger.error("Error retrieving user leaderboard for {} in game {}", username, game, e);
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
            logger.info("Score updated for {} in game {}: {}", username, game, score);
        } catch (SQLException e) {
            logger.error("Error updating score for {} in game {}: {}", username, game, score, e);
        }
    }
}