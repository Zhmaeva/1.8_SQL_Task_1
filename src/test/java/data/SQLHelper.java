package data;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    public static Connection connection() throws SQLException {
        return DriverManager.getConnection(System.getProperty("db.url"), "app", "pass");
    }

    public static String getVerificationCode() {
        try (Connection connect = connection()) {
            String queryVerificationCode = "SELECT code  FROM auth_codes ORDER by created DESC LIMIT 1";
            return QUERY_RUNNER.query(connect, queryVerificationCode, new ScalarHandler<>());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clearDatabase() {
        try (Connection connect = connection()) {
            deleteVerificationCodeInTable();
            QUERY_RUNNER.execute(connect, "DELETE FROM card_transactions");
            QUERY_RUNNER.execute(connect, "DELETE FROM cards");
            QUERY_RUNNER.execute(connect, "DELETE FROM users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteVerificationCodeInTable() {
        try (Connection connect = connection()) {
            QUERY_RUNNER.execute(connect, "DELETE FROM auth_codes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
