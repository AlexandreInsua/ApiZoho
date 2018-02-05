package aplicacionZoho;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.poi.util.SystemOutLogger;
import org.xml.sax.InputSource;

public class Principal {

	public static void main(String[] args) throws HttpException, IOException {
		// exercicio 1
		// mostrarContactos();

		// exercicio 2

		try {
			Connection con = BDManager.abrirBD();
			ArrayList<Lead> lista;
			lista = BDManager.getLeads(con);
			BDManager.cerrarBD(con);
			ZohoManager.insertLeads(lista);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void mostrarContactos() {

		try {
			// PARAMETROS DA CONEXION
			// url para recuperar datos
			String url = "https://crm.zoho.eu/crm/private/xml/Contacts/getRecords";
			// token
			String token = "0debe5329fd1efcf6f1b01de4703a756";

			// CLIENTE HTTP
			HttpClient httpclient = new HttpClient();

			// clase que representa a petición
			PostMethod post = new PostMethod(url);

			// parámetros da petición
			post.setParameter("authtoken", token);
			post.setParameter("scope", "crmapi");

			// RECUPERAMOS A INFORMACIÓN post
			// Executamos a petición con post
			httpclient.executeMethod(post);
			// Recuperamos o string
			String cadeaXml = post.getResponseBodyAsString();
			System.out.println(cadeaXml);

			// PASEAR A INFO CON XPATH
			// Crear o obxecto parseador
			XPath xPath = XPathFactory.newInstance().newXPath();
			InputSource inputsource = new InputSource(new StringReader(cadeaXml));

			// CONSULTAR
			// Expresión para contar as filas
			String expresionContar = "count(response/result/Contacts/row)";
			// Expresión para buscar a primeira fila e recuperar o nome
			String expresionNomePrimeira = "response/result/Contacts/row[1]/FL[@val='First Name']/text()";
			// obten o número de contas
			double numeroContas = (double) xPath.evaluate(expresionContar, inputsource, XPathConstants.NUMBER);

			for (int i = 1; i <= numeroContas; i++) {
				// obter todos nos nomes
				String expresionFilas = "response/result/Contacts/row[" + i + "]/FL[@val='First Name']/text()";
				InputSource inputsourceFilas = new InputSource(new StringReader(cadeaXml));
				String nome = (String) xPath.evaluate(expresionContar, inputsource, XPathConstants.STRING);
				System.out.println(nome);
			}

		} catch (IOException | XPathExpressionException e) {

			e.printStackTrace();
		}
	}

	public void metodoContas() {
		PostMethod post = null;

		try {
			// dato identificativoss
			// url para recuperar datos
			String url = "https://crm.zoho.eu/crm/private/xml/Accounts/getRecords";

			// url para inserir datos
			String url2 = "https://crm.zoho.eu/crm/private/xml/Accounts/insertRecords";

			Account conta = new Account("Cebem2", "986332211", "www.cebem.net");

			// identificación
			String token = "0debe5329fd1efcf6f1b01de4703a756";

			HttpClient httpclient = new HttpClient();

			// clase que representa a petición
			post = new PostMethod(url);

			// parámetros da petición
			post.setParameter("authtoken", token);
			post.setParameter("scope", "crmapi");
			// parametro para inserir datos
			// post.setParameter("xmlData", conta.toXml());

			// Executamos a pecición
			// devovolve un enteiro coa filas mostradas ou modificadas
			int numeroRexistrosAlerados = httpclient.executeMethod(post);

			System.out.println(numeroRexistrosAlerados);

			// recupera o xml en formato de string na
			String postRes = post.getResponseBodyAsString();

			System.out.println(postRes);

			ArrayList<Account> listacontas = Account.xmlToListAccount(postRes);

			for (Account a : listacontas) {
				System.out.println(a);
			}

			Account.listToExcell(listacontas, "contas.xls");

		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
	}

}

class BDManager {
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

class ZohoManager {
	public static void insertLeads(ArrayList<Lead> lista) throws HttpException, IOException {
		StringBuilder xml = new StringBuilder();
		xml.append("<Leads>");
		int contador = 1;
		for (Lead l : lista) {
			xml.append("<row no='" + contador + "'>");
			xml.append("<FL var='First Name'>");
			xml.append(l.firstName);
			xml.append("</FL>");
			
			xml.append("<FL var='Last Name'>");
			xml.append(l.lastName);
			xml.append("</FL>");
			
			
			xml.append("<FL var='Company'>");
			xml.append(l.company);
			xml.append("</FL>");
			
			
			xml.append("</row>");
			// todo co resto repetir co resto
			contador++;
		}
		xml.append("</Leads>");

		// PARAMETROS DA CONEXION
		// url para recuperar datos
		String url = "https://crm.zoho.eu/crm/private/xml/Leads/insertRecords";
		// token
		String token = "0debe5329fd1efcf6f1b01de4703a756";

		// CLIENTE HTTP
		HttpClient httpClient = new HttpClient();

		// clase que representa a petición
		PostMethod post = new PostMethod(url);

		// parámetros da petición
		post.setParameter("authtoken", token);
		post.setParameter("scope", "crmapi");
		post.setParameter("xmlData", xml.toString());
		
		try {
			int resultado = httpClient.executeMethod(post);
			System.out.println(resultado);
			String postResposta = post.getResponseBodyAsString(); 
			
		} finally {
			
		}
	}
}