package ca.sheridancollege.zhaoste.databases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.zhaoste.beans.MenuItem;
import ca.sheridancollege.zhaoste.beans.Order;
import ca.sheridancollege.zhaoste.beans.User;

@Repository
public class DatabaseAccess {
	@Autowired
	protected NamedParameterJdbcTemplate jdbc;
	@Autowired
	private PasswordEncoder passwordEncoder;

	// Menu Table:
	public List<MenuItem> getMenuList(String name) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("name", name.toLowerCase());
		if (name.isBlank() || name.isEmpty()) {
			String query = "SELECT * FROM menu_items";
			return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<MenuItem>(MenuItem.class));
		} else {
			String query = "SELECT * FROM menu_items WHERE name iLIKE (:name)";
			return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<MenuItem>(MenuItem.class));
		}
	}

	public MenuItem getItemById(Long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "SELECT * FROM menu_items WHERE item_id = :id";
		namedParameters.addValue("id", id);

		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<MenuItem>(MenuItem.class)).get(0);
	}
	
	public void insertMenuItem(MenuItem mi) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		
		namedParameters.addValue("name", mi.getName());
		namedParameters.addValue("price", mi.getPrice());
		namedParameters.addValue("spiceLevel", mi.getSpiceLevel());
		
		String query = "INSERT INTO menu_items(name, price, spice_level) VALUES(:name, :price, :spiceLevel)";
		int rowsAffected = jdbc.update(query, namedParameters);
		if (rowsAffected > 0)
			System.out.println("inserted into the database");
	}

	// Order table:
	public List<Order> getOrderList(String name) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("name", name.toLowerCase());
		if (name.isBlank() || name.isEmpty()) {
			String query = "SELECT * FROM orders";
			return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Order>(Order.class));
		} else {
			String query = "SELECT * FROM orders WHERE name iLIKE (:name)";
			return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Order>(Order.class));
		}
	}

	public void insertOrder(Order order) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		namedParameters.addValue("customerName", order.getCustomerName());
		namedParameters.addValue("foodId", order.getFoodId());

		String query = "INSERT INTO orders(customer_name, food_id) VALUES(:customerName, :foodId)";

		int rowsAffected = jdbc.update(query, namedParameters);
		if (rowsAffected > 0)
			System.out.println("inserted into the database");
	}

	public void deleteOrder(Long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "DELETE FROM orders WHERE order_id = :id";
		namedParameters.addValue("id", id);

		if (jdbc.update(query, namedParameters) > 0) {
			System.out.println("Deleted id: " + id);
		}
	}

	public Order getOrderById(Long id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();

		String query = "SELECT * FROM Orders WHERE order_id = :id";
		namedParameters.addValue("id", id);

		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Order>(Order.class)).get(0);
	}

	// Security Stuff
	// Method to get User Roles for a specific User id
	public List<String> getRolesById(Long userId) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT sec_role.roleName " + "FROM user_role, sec_role "
				+ "WHERE user_role.roleId = sec_role.roleId " + "AND userId = :userId";
		namedParameters.addValue("userId", userId);
		return jdbc.queryForList(query, namedParameters, String.class);
	}

	// Method to find a user account by email
	public User findUserAccount(String email) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM sec_user where email = :email";
		namedParameters.addValue("email", email);
		try {
			return jdbc.queryForObject(query, namedParameters, new BeanPropertyRowMapper<>(User.class));
		} catch (EmptyResultDataAccessException erdae) {
			return null;
		}
	}
}
