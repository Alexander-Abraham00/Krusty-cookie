package krusty;

import spark.Request;
import spark.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import static krusty.Jsonizer.toJson;

public class Database {
	/**
	 * Modify it to fit your environment and then use this string when connecting to
	 * your database!
	 */
	private static final String jdbcString = "jdbc:mysql://puccini.cs.lth.se/hbg03";

	// For use with MySQL or PostgreSQL
	private static final String jdbcUsername = "hbg03";
	private static final String jdbcPassword = "wrs701oe";
	private Connection conn;

	public void connect() {
		try {
			conn = DriverManager.getConnection(jdbcString, jdbcUsername, jdbcPassword);
		} catch (SQLException exception) {
			System.err.println(exception);
			exception.printStackTrace();
		}

	}

	// TODO: Implement and change output in all methods below!

	public String getCustomers(Request req, Response res) {
		String sql = "Select name, address from Customer";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {

			ResultSet rs = ps.executeQuery();
			return toJson(rs, "customers");


		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
			return Jsonizer.anythingToJson("error", "status");
		}

	}

	public String getRawMaterials(Request req, Response res) {
		String sql = "Select name, currentAmountInStock as amount, unit from RawMaterials";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			return toJson(rs, "raw-materials");
		} catch (SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			return Jsonizer.anythingToJson("error", "status");
		}
	}

	public String getCookies(Request req, Response res) {
		String sql = "Select name from Cookie";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			return Jsonizer.toJson(rs, "cookies");

		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
			return Jsonizer.anythingToJson("error", "status");
		}

	}

	public String getRecipes(Request req, Response res) {
		String sql = "Select cookie, raw_material, amount, R.unit from Recipe R, RawMaterials S where S.name = R.raw_material";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			return Jsonizer.toJson(rs, "recipes");
		} catch (SQLException e) {
			System.err.println(e);
			e.printStackTrace();
			return Jsonizer.anythingToJson("error", "status");
		}
	}

	public String getPallets(Request req, Response res) {

		String sql = "SELECT Pallet.cookie as cookie , Pallet.productionDate as production_date, IF(Pallet.blocked, 'yes', 'no') AS blocked  FROM Pallet";

		ArrayList<String> values = new ArrayList<String>();

		if (req.queryParams("from") != null || req.queryParams("to") != null
				|| req.queryParams("cookie") != null || req.queryParams("blocked") != null) {
			sql += " Where";

		}

		if (req.queryParams("from") != null) {
			sql += " productionDate >= ?";
			values.add(req.queryParams("from"));
		}

		if (req.queryParams("to") != null) {
			if (!values.isEmpty()) {
				sql += " and";
			}
			sql += " productionDate <= ?";
			values.add(req.queryParams("to"));
		}

		if (req.queryParams("cookie") != null) {
			if (!values.isEmpty()) {
				sql += " and";
			}
			sql += " cookie = ?";
			values.add(req.queryParams("cookie"));
		}

		if (req.queryParams("blocked") != null) {
			if (!values.isEmpty()) {
				sql += " and";
			}
			if (req.queryParams("blocked").equals("yes")) {
				sql += " blocked = 1";
			} else {
				sql += " blocked = 0";
			}
		}

		sql += " Order BY productionDate";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int i = 0; i < values.size(); i++) {
				ps.setString(i + 1, values.get(i));
			}
			System.out.println(ps.toString());

			ResultSet rs = ps.executeQuery();
			return Jsonizer.toJson(rs, "pallets");

		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
			return Jsonizer.anythingToJson("error", "status");
		}
	}

	public String reset(Request req, Response res) {
		// Hämta textfil med statements för att kunna göra reset
		StringBuilder sb = new StringBuilder();
		try {
			File myObj = new File("krusty\\krusty-skeleton\\src\\main\\java\\krusty\\reset.txt");
			Scanner scan = new Scanner(myObj);
			String txt = null;
			while (scan.hasNextLine()) {
				txt = scan.nextLine();
				sb.append(txt);

			}
			scan.close();

			ArrayList<String> stmts = new ArrayList<>();
			for (String s : sb.toString().split(";")) {
				stmts.add(s + ";");
			}
			
			for (String a : stmts) {
				try (PreparedStatement ps = conn.prepareStatement(a)) {
					conn.setAutoCommit(false);
					ps.executeUpdate();
					conn.commit();
					conn.setAutoCommit(true);
				} catch (Exception e) {
					System.err.println(e);
					e.printStackTrace();
					if (conn != null) {
						try {
						  System.err.print("Transaction is being rolled back");
						  conn.rollback();
						} catch (SQLException excep) {
							excep.getSQLState();
							excep.printStackTrace();
						}
				}
			}
		}
			return Jsonizer.anythingToJson("ok", "status");

		} catch (FileNotFoundException e) {
			System.out.println("Fil finns inte");
			e.printStackTrace();
			return Jsonizer.anythingToJson("error", "status");
		}

	}

	public String createPallet(Request req, Response res) {
		if (req.queryParams("cookie").contains("Almond delight") || req.queryParams("cookie").contains("Amneris")
				|| req.queryParams("cookie").contains("Berliner") || req.queryParams("cookie").contains("Nut cookie")
				|| req.queryParams("cookie").contains("Nut ring") || req.queryParams("cookie").contains("Tango")) {
			String cookie = req.queryParams("cookie");

			String sql = "insert into Pallet (palletId, blocked, productionDate, deliveryDate,  maxAmount, cookie) VALUES (?,?,?,?,?,?)";

			// Skapa ny pallet i databasen
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				conn.setAutoCommit(false);
				LocalDateTime now = LocalDateTime.now();// tiden nu
				LocalDateTime delivered = now.plusDays(3);// levereras om tre dagar
				ps.setInt(1, 0);
				ps.setInt(2, 0);
				ps.setString(3, now.toString());
				ps.setString(4, delivered.toString());
				ps.setInt(5, 5400);
				ps.setString(6, cookie);

				ps.executeUpdate();
				updateStorage(cookie);

				String lastPallet = "Select max(palletId) as lastPallet from Pallet";

				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(lastPallet);
				String palletId = null;
				while (rs.next()) {
					palletId = rs.getString("lastPallet");
				}
				conn.commit();
				conn.setAutoCommit(true);
				System.out.println(palletId + " - palletId");
				return "{\"status\": \"ok\",\n\"id\": " + palletId + "}";

			} catch (Exception e) {
			
				System.err.println(e);
				e.printStackTrace();
				if (conn != null) {
					try {
					  System.err.print("Transaction is being rolled back");
					  conn.rollback();
					} catch (SQLException excep) {
						excep.getSQLState();
						excep.printStackTrace();
					}
				return Jsonizer.anythingToJson("error", "status");
			}

			}
	}
	return Jsonizer.anythingToJson("unknown cookie", "status");
	}

	// hjälpmetod för att uppdatera storage
	public void updateStorage(String cookieName) {


		// Uppdatera raw materials
		HashMap<String, Integer> cookieMaterials = new HashMap<String, Integer>();
		String sqlString = "select RawMaterials.currentAmountInStock, RawMaterials.name from RawMaterials, Recipe"
				+ " where Recipe.raw_material = RawMaterials.name" +
				" AND Recipe.cookie = ?;";

		// Plocka ut nuvarande inventory för varje materialnamn (nyckeln är namnet)
		try (PreparedStatement prepState = conn.prepareStatement(sqlString)) {
			prepState.setString(1, cookieName);
			ResultSet materials = prepState.executeQuery();

			// Fyll mappen med alla material och dess startvärden
			while (materials.next()) {
				cookieMaterials.put(materials.getString("name"), materials.getInt("currentAmountInStock"));
			}

			//anropa metoden reduceWithRecipe för att returnera map med alla material som behövs beroende på recept på kakan
			HashMap<String, Integer> recipeAmount =reduceWithRecipe(cookieName);

			// sedan uppdatera databasen med det
			String updateString = "update RawMaterials set currentAmountInStock = ? where name = ? ;";

			for (Entry<String, Integer> update : cookieMaterials.entrySet()) {// går igenom cookieMaterials mappen
				String name = update.getKey();
				int amount = update.getValue();

				for (Entry<String, Integer> recipe : recipeAmount.entrySet()){// går igenom recipeAmount mappen
					String recName = recipe.getKey();
					int recAmount = recipe.getValue();

					if (name.equals(recName)){// kollar om namnet från RawMaterials finns på Recipes
						int reduce = amount - (recAmount*54); // beräkna den nya mängden
						try (PreparedStatement ps = conn.prepareStatement(updateString)) {//updaterar
							ps.setInt(1, reduce);
							ps.setString(2, name);
							ps.executeUpdate();

						} catch (Exception e) {
							System.err.println(e);
							e.printStackTrace();
						}
					}
				}
			}

		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();

		}

	}

	// Hämta antalet av alla material som behövs beroende på recept på kakan
	public HashMap reduceWithRecipe(String cookie) {
		HashMap<String, Integer> recipeMaterials = new HashMap<String, Integer>();
		String cookieAmount = "select amount, raw_material from Recipe where cookie = ?;";

		try (PreparedStatement stmt = conn.prepareStatement(cookieAmount)) {
			stmt.setString(1, cookie);
			ResultSet resSet = stmt.executeQuery();

			while (resSet.next()) {
				recipeMaterials.put(resSet.getString("raw_material"), resSet.getInt("amount"));
			}

		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
		return recipeMaterials;

	}


}



