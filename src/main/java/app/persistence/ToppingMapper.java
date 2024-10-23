package app.persistence;

import app.entities.Topping;
import app.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ToppingMapper {

    public static List<Topping> getAllToppings(ConnectionPool connectionPool) throws DatabaseException {
        List<Topping> toppingList = new ArrayList<>();

        String sql = "SELECT * FROM topping";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int toppingId = rs.getInt("topping_id");
                    String toppingFlavor = rs.getString("topping_flavor");
                    double price = rs.getDouble("price");

                    toppingList.add(new Topping(toppingId, toppingFlavor, price));
                }
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Error retrieving topping list");
        }
        return toppingList;
    }

    public static Topping getToppingById(int toppingId, ConnectionPool connectionPool) throws DatabaseException {
        Topping topping = null;
        String sql = "SELECT * FROM topping WHERE topping_id = ?";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, toppingId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String toppingFlavor = rs.getString("topping_flavor");
                    double price = rs.getDouble("price");

                    topping = new Topping(toppingId, toppingFlavor, price);
                }
            }
        } catch (SQLException exception) {
            throw new DatabaseException("Failed to retrieve topping with ID: " + toppingId);
        }
        return topping;
    }
}

