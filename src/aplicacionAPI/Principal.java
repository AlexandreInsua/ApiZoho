package aplicacionAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.xml.sax.InputSource;

public class Principal {

	public static void main(String[] args) throws HttpException, IOException {

		String opcion = "0";

		do {
			System.err.println("\nMENÚ DE EXERCICIOS DA API DE ZOHO");

			System.out.println("Exercicio 1.- Mostrar todos os contactos de Zoho");
			System.out.println("Exercicio 2.- Inserir Leads en Zoho desde unha BBDD MySQL");

			System.out.println("Exercicio 5.- Engadir un orzamento a un contacto en Zoho usando XML");
			System.out.println("Exercicio 6.- Obter un listado das ligas en java e xml");
			System.out.println("0.- Saír");

			opcion = introducirDatos("Introduce unha opción: ");

			switch (opcion) {
			case "1":
				mostrarContactos();
				break;

			case "2":
				try {
					Connection con = BDManager.abrirBD();
					ArrayList<Lead> lista;
					lista = BDManager.getLeads(con);
					BDManager.cerrarBD(con);
					inserirLeads(lista);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;

			case "5":
				engadirOrzamento();
				break;

			case "6":
				break;
			case "0":
				System.exit(0);
				break;
			default:
				System.out.println("Opción non válida");

			}

		} while (!opcion.equals("20"));

	}

	private static void engadirOrzamento() {

		StringBuilder xml = new StringBuilder();
		// Vou simular dous produtos nun orzamento que coloco ao principio de
		// todo
		xml.append("<Quotes>");
		xml.append("<row no='1'>");
		xml.append("<FL val='Subject'>Orzamento para App Balaidos</FL>");
		xml.append("<FL val='Account Name'> App Informática Balaidos </FL>");
		xml.append("<FL val='Product Details'>");
		
		xml.append("<product no='1'>");
		xml.append("<FL val='Product Name'>Monitor TFT Asus 17\"</FL>");
		xml.append("<FL val='Quantity'>5</FL>");
		xml.append("<FL val='Unit Price'>80</FL>");
		xml.append("<FL val='List Price'>Black Friday</FL>");
		xml.append("</product>");
		
		xml.append("<product no='2'>");
		xml.append("<FL val='Product Name'>Ratón Wireless</FL>");
		xml.append("<FL val='Quantity'>10</FL>");
		xml.append("<FL val='Unit Price'>11.99</FL>");
		xml.append("<FL val='List Price'>Grandes Cantidades</FL>");
		xml.append("</product>");
		xml.append("</FL>");
		
		
		
		xml.append("</row>");

		xml.append("</Quotes>");
		// url para inserir datos
		String url = "https://crm.zoho.eu/crm/private/xml/Quotes/insertRecords";
		// token
		String token = "0debe5329fd1efcf6f1b01de4703a756";

		// Cliente http
		HttpClient httpClient = new HttpClient();
		// petición
		PostMethod post = new PostMethod(url);

		// parametros
		post.setParameter("authtoken", token);
		post.setParameter("scope", "crmapi");
		post.setParameter("xmlData", xml.toString());

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
			// String expresionNomePrimeira =
			// "response/result/Contacts/row[1]/FL[@val='First Name']/text()";
			// obten o número de contactos
			double numeroContactos = (double) xPath.evaluate(expresionContar, inputsource, XPathConstants.NUMBER);

			System.out.println("LISTA DE CONTACTOS \nNº de contactos: " + numeroContactos);
			for (int i = 1; i <= numeroContactos; i++) {

				// obter todos os nomes
				InputSource inputsource1 = new InputSource(new StringReader(cadeaXml));
				String expresionNome = "response/result/Contacts/row[" + i + "]/FL[@val='First Name']/text()";
				String nome = (String) xPath.evaluate(expresionNome, inputsource1, XPathConstants.STRING);

				// obter os apelidos

				InputSource inputsource2 = new InputSource(new StringReader(cadeaXml));
				String expresionApelido = "response/result/Contacts/row[" + i + "]/FL[@val='Last Name']/text()";
				String apelido = (String) xPath.evaluate(expresionApelido, inputsource2, XPathConstants.STRING);

				System.out.println(nome + " " + apelido);
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (XPathExpressionException xpathe) {
			xpathe.printStackTrace();
		}
	}

	public static void inserirLeads(ArrayList<Lead> lista) throws HttpException, IOException {
		StringBuilder xml = new StringBuilder();
		xml.append("<Leads>");
		int contador = 1;
		for (Lead l : lista) {
			xml.append("<row no='" + contador + "'>");
			xml.append("<FL val='First Name'>");
			xml.append(l.firstName);
			xml.append("</FL>");

			xml.append("<FL val='Last Name'>");
			xml.append(l.lastName);
			xml.append("</FL>");

			xml.append("</row>");
			// todo co resto repetir co resto
			contador++;
		}
		xml.append("</Leads>");

		// PARAMETROS DA CONEXION
		// url para inserir os datos
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

	public static String introducirDatos(final String cad) throws IOException {
		// Introducindo unha cadea de caracteres
		String cadea = null;
		// 1º. Crea un obxecto InputStreamReader
		InputStreamReader isr = new InputStreamReader(System.in);
		// 2º. Crea un obxecto BufferedReader
		BufferedReader br = new BufferedReader(isr);
		System.out.println(cad);
		return br.readLine();
	}

}
