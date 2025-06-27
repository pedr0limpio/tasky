package br.com.pedr0limpio.services;
import br.com.pedr0limpio.enums.Priority;
import br.com.pedr0limpio.enums.Tag;
import br.com.pedr0limpio.models.Task;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@ApplicationScoped
public class MySQLDAO extends TaskBaseDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLDAO.class);

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
            LOGGER.error(ex.getMessage());
        }
    }

    @Override
    public int writeTask(Task task) {
        int taskId = -1;
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            conn.setAutoCommit(false);
            taskId = insertTask(conn, task);
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
            LOGGER.error(e.getMessage());
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                conn.rollback();
            } catch (SQLException rollbackException) {
                LOGGER.error(rollbackException.getMessage());
            }
        }
        return taskId;
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
public Task getById(int id) { //Implement getById to fetch in DB for a task.
    String sql = "SELECT task_id, description, priority, created_at, conclusion_at FROM TASKS WHERE task_id = ?";
    try (Connection conn = DriverManager.getConnection(url, username, password);
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Task task = new Task(rs.getInt("task_id"));
                task.setDescription(rs.getString("description"));
                task.setPriority(Enum.valueOf(Priority.class, rs.getString("priority")));
                task.setCreation(rs.getTimestamp("created_at"));
                Timestamp conclusion = rs.getTimestamp("conclusion_at");
                if (conclusion != null) {
                    task.setConclusion(conclusion);
                }
                // Fetch tags for this task
                String tagSql = "SELECT t.name FROM TAGS t " +
                                "JOIN TASK_TAGS tt ON t.tag_id = tt.tag_id " +
                                "WHERE tt.task_id = ?";
                try (PreparedStatement tagStmt = conn.prepareStatement(tagSql)) {
                    tagStmt.setInt(1, id);
                    try (ResultSet tagRs = tagStmt.executeQuery()) {
                        List<Tag> tags = new ArrayList<>();
                        while (tagRs.next()) {
                            String tagName = tagRs.getString("name");
                            try {
                                tags.add(Tag.valueOf(tagName));
                            } catch (IllegalArgumentException e) {
                                LOGGER.warn("Unknown tag in DB: " + tagName);
                            }
                        }
                        task.setTagList(tags);
                    }
                }
                return task;
            }
        }
    } catch (SQLException e) {
        LOGGER.error(e.getMessage());
    }
    return null;
}

    @Override
    public void update(int idFrom, Task taskFor) { //TODO[#11]: Implement update(int idFrom, Task taskFor) to update a task in DB.

    }

    @Override
    public boolean removeById(int id) {
        String deleteTaskTagsSql = "DELETE FROM TASK_TAGS WHERE task_id = ?";
        String deleteTaskSql = "DELETE FROM TASKS WHERE task_id = ?";
        int affectedRows = 0;
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt1 = conn.prepareStatement(deleteTaskTagsSql)) {
                stmt1.setInt(1, id);
                stmt1.executeUpdate();
            }
            try (PreparedStatement stmt2 = conn.prepareStatement(deleteTaskSql)) {
                stmt2.setInt(1, id);
                affectedRows = stmt2.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                conn.rollback();
            } catch (SQLException rollbackException) {
                LOGGER.error(rollbackException.getMessage());
            }
        }
        return affectedRows > 0;
    }
}
