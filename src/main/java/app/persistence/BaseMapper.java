package app.persistence;

import app.entities.Base;
import app.expections.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class BaseMapper {

    public static List<Base> getAllBases(ConnectionPool connectionPool) throws DatabaseException {
        List<Base> baseList = new ArrayList<>();

        String sql = "SELECT * FROM base";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int baseId = rs.getInt("base_id");
                    String baseFlavor = rs.getString("base_flavor");
                    double price = rs.getDouble("price");

                    baseList.add(new Base(baseId, baseFlavor, price));
                }
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Error retrieving base list");
        }
        return baseList;
    }

    public static Base getBaseById(int baseId, ConnectionPool connectionPool) throws DatabaseException {
        Base base = null;
        String sql = "SELECT * FROM base WHERE base_id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, baseId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String baseFlavor = rs.getString("base_flavor");
                    double price = rs.getDouble("price");

                    base = new Base(baseId, baseFlavor, price);
                }
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to retrieve base with ID: " + baseId);
        }
        return base;
    }
}
