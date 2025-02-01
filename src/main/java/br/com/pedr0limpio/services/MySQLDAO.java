package br.com.pedr0limpio.services;

import br.com.pedr0limpio.enums.Tag;
import br.com.pedr0limpio.models.Task;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

@ApplicationScoped
public class MySQLDAO extends TaskBaseDAO {
    private String url;
    private String username;
    private String password;

    public MySQLDAO() {
        loadDatabaseConfig();
    }

    private void loadDatabaseConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("Oops, application.properties not found!");
                return;
            }
            prop.load(input);
            url = prop.getProperty("quarkus.datasource.jdbc.url");
            username = prop.getProperty("quarkus.datasource.username");
            password = prop.getProperty("quarkus.datasource.password");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void writeTask(Task task) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            conn.setAutoCommit(false);

            int taskId = insertTask(conn, task);
            if (taskId == -1) {
                throw new SQLException("Failed to write task.");
            }

            for (Tag tag : task.getTagList()) {
                int tagId = insertOrGetTagId(conn, String.valueOf(tag));
                if (tagId == -1) {
                    throw new SQLException("Failed to insert and/or get tag ID for tag: " + tag);
                }

                insertTaskTag(conn, taskId, tagId);
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                conn.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
        }
    }

    private int insertTask(Connection conn, Task task) throws SQLException {
        String sql = "INSERT INTO TASKS (description, priority, created_at, conclusion_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, task.getDescription());
            stmt.setString(2, task.getPriority().name());
            stmt.setTimestamp(3, new Timestamp(task.getCreation().getTime()));
            stmt.setTimestamp(4, task.getConclusion() != null ? new Timestamp(task.getConclusion().getTime()) : null);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    System.out.println("Task inserted with ID: " + rs.getInt(1));
                    return rs.getInt(1);
                }
            }
        }
        System.err.println("Failed to insert task.");
        return -1;
    }

    private int insertOrGetTagId(Connection conn, String tag) throws SQLException {
        // Check if tag exists
        String selectSql = "SELECT tag_id FROM TAGS WHERE name = ?";
        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            selectStmt.setString(1, tag);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Tag found with ID: " + rs.getInt(1));
                    return rs.getInt(1);
                }
            }
        }

        // Insert tag if it does not exist
        String insertSql = "INSERT INTO TAGS (name) VALUES (?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setString(1, tag);
            insertStmt.executeUpdate();
            try (ResultSet rs = insertStmt.getGeneratedKeys()) {
                if (rs.next()) {
                    System.out.println("Tag inserted with ID: " + rs.getInt(1));
                    return rs.getInt(1);
                }
            }
        }
        System.err.println("Failed to insert or get tag ID for tag: " + tag);
        return -1;
    }

    private void insertTaskTag(Connection conn, int taskId, int tagId) throws SQLException {
        String sql = "INSERT INTO TASK_TAGS (task_id, tag_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            stmt.setInt(2, tagId);
            stmt.executeUpdate();
            System.out.println("Inserted task_tag with taskId: " + taskId + " and tagId: " + tagId);
        }
    }

    @Override
    public List<Task> getAllTasks() { //TODO[#9]: Implement getAllTasks() to fetch in DB for all tasks.
        return List.of();
    }

    @Override
    public Task getById(int id) { //TODO[#10]: Implement getById(int id) to fetch in DB for a task.
        return null;
    }

    @Override
    public void update(int idFrom, Task taskFor) { //TODO[#11]: Implement update(int idFrom, Task taskFor) to update a task in DB.

    }

    @Override
    public void removeById(int id) { //TODO[#12]: Implement removeById(int id) to delete a task by id in DB.

    }
}

