package aplicacionAPI;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BDManager {
	public static Connection abrirBD() {
		System.out.println("Conectando con base de datos");
		return null;
	}

	public static void cerrarBD(Connection con) {
		System.out.println("Pechando base de datos");
	}

	public static ArrayList<Lead> getLeads(Connection con) throws SQLException {
		ArrayList<Lead> resultado = new ArrayList<>();
		Statement st;

		st = con.createStatement();
		String query = "SELECT * FROM  Lead";
		ResultSet rs = st.executeQuery(query);

		while (rs.next()) {
			String firstName = rs.getString("firstName");
			String lastName = rs.getString("lastName");
			String phone = rs.getString("phone");
			String email = rs.getString("email");
			String company = rs.getString("firstcompany");

			Lead l = new Lead(firstName, lastName, phone, email, company);
			resultado.add(l);

		}
		return resultado;
	}
}
