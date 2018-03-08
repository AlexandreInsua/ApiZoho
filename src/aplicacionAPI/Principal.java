package aplicacionAPI;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.xml.sax.InputSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Principal {

	public static void main(String[] args) throws HttpException, IOException {

		int opcion = 20;

		do {
			System.err.println("\nMEN� DE EXERCICIOS DA API DE ZOHO");

			System.out.println("Exercicio 1.- Mostrar todos os 	CONTACTOS de Zoho");
			System.out.println("Exercicio 2.- Inserir LEADS en Zoho desde unha BBDD MySQL");
			System.out.println("Exercicio 3.- Listar LEADS usando Python con datos JSON");
			System.out.println("Exercicio 4.- Listar CASOS usando Python con datos XML");
			System.out.println("Exercicio 5.- Engadir un orzamento a un CONTACTO en Zoho usando XML");
			System.out.println("Exercicio 6.- Obter un listado das LIGAS en java e xml");
			System.out.println("Exercicio 7. <pulsar 7> - Obter un listado das ligas en python e json");
			System.out.println("Exemplo 8.- Listar CONTAS");
			System.out.println("Exemplo 9.- Parsear caso en JSON");
			System.out.println("Exemplo 10.- Gardar informaci�n en Excel");
			System.out.println("Exemplo 11.- Inserir CONTA en zoho");
			System.out.println("0.- Sa�r");

			opcion = Integer.parseInt(introducirDatos("Introduce unha opci�n: "));

			switch (opcion) {
			case 1:
				mostrarContactos();
				break;

			case 2:
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

			case 3:
				JOptionPane.showMessageDialog(null, "Ver ficheiro .py ");
				break;
			case 4:
				JOptionPane.showMessageDialog(null, "Ver ficheiro .py ");
				break;
			case 5:
				engadirOrzamento();
				break;

			case 6:
				mostrarLigasFutbol();
				break;
			case 7:
				JOptionPane.showMessageDialog(null, "Ver ficheiro .py ");
				break;
			case 8:
				listarContas();
				break;
			case 9:
				parsearJson();
				break;
			case 10:
				gardarExcell();
				break;
			case 11:
				inserirCodigo();
				break;
			case 0:
				System.exit(0);
				break;
			default:
				System.out.println("Opci�n non v�lida");

			}

		} while (opcion != 0);

	}

	private static void inserirCodigo() {
		// preparamos a informaci�n a inserir
		String accountName = "";
		String webSite = "";
		String phone = "";

		String xml;
		xml = "<Accounts>";
		xml += "<row no='1'>";

		xml += "<FL val='Account Name'>" + accountName + "</FL>";
		xml += "<FL val='Website'>" + webSite + "</FL>";
		xml += "<FL val='Phone'>" + phone + "</FL>";

		xml += "</row>";
		xml += "</Accounts>";

		// lanzamos a petici�n
		String url = "https://crm.zoho.eu/crm/private/xml/Accounts/insertRecords";
		String token = "0971a0330c11e6f870cb6067152c9a66";

		Account cuenta = new Account("Cebem2", "986332211", "www.cebem.net");

		HttpClient httpClient = new HttpClient();
		PostMethod post = new PostMethod(url);
		post.setParameter("authtoken", token);
		post.setParameter("scope", "crmapi");
		post.setParameter("xmlData", xml);

		int resultado = 0;
		String postRespuesta = null;
		try {
			resultado = httpClient.executeMethod(post);
			postRespuesta = post.getResponseBodyAsString();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(resultado);

		System.out.println(postRespuesta);

	}

	private static void gardarExcell() {
		// Preparamos un libro nuevo de excel
		HSSFWorkbook libro = new HSSFWorkbook();
		// a�adimos una nueva hoja de excel
		HSSFSheet hoja = libro.createSheet();
		// a�adimos a esa hoja una nueva fila
		HSSFRow fila = hoja.createRow(0);
		// Creamos una primera celda de esa hoja
		HSSFCell celda0 = fila.createCell(0);
		// Creamos una segunda celda de esa hoja
		HSSFCell celda1 = fila.createCell(1);
		// insertamos en nuestra primera celda un valor
		celda0.setCellValue("Esto es una prueba");
		// insertamos en nuestra segunda celda otro valor
		celda1.setCellValue("cebem");

		// guardamos nuestro fichero en disco
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("miFicheroExcel.xls");
			libro.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				libro.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static void parsearJson() {
		String postResp = "  <<esto es un texto en formato json>>  ";
		// Instanciamos o parseador
		JsonParser parser = new JsonParser();
		// para cada elemento vaise parseando o fillo
		JsonObject raiz = parser.parse(postResp).getAsJsonObject();
		JsonObject response = raiz.get("response").getAsJsonObject();
		JsonObject result = response.get("result").getAsJsonObject();
		JsonObject cases = result.get("Cases").getAsJsonObject();
		// ollo a colecci�n vai nun jsonArray!!!!
		JsonArray rows = cases.get("row").getAsJsonArray();
		// para cada elemento da collecci�n cun buble for each
		for (JsonElement row : rows) {
			JsonArray fls = row.getAsJsonObject().get("FL").getAsJsonArray();
			for (JsonElement fl : fls) {
				if (fl.getAsJsonObject().get("val").getAsString().equals("Product Name")) {
					String content = fl.getAsJsonObject().get("content").getAsString();
					System.out.println("Nombre del producto" + content);
				}
				if (fl.getAsJsonObject().get("val").getAsString().equals("Subject")) {
					String content = fl.getAsJsonObject().get("content").getAsString();
					System.out.println("Texto del problema" + content);
				}
			}
		}

	}

	private static void listarContas() {

		// RECUPERAR A INFORMACI�N ---------------------------------------------
		// Url � que vamos lanzar a petici�n
		String url = "https://crm.zoho.eu/crm/private/xml/Accounts/getRecords";
		// Token persoal
		String token = "0971a0230c11e6kt70cb6067152c8a66";
		// Cliente que permite lanzar petici�ns web
		HttpClient httpClient = new HttpClient();
		// preparamos unha petici�n de tipo POST con 2 par�metros
		PostMethod post = new PostMethod(url);
		post.setParameter("authtoken", token);
		post.setParameter("scope", "crmapi");
		// lanzamos a petici�n

		// obtenemos a petici�n en formato texto (xml)
		String postResp = null;
		try {
			httpClient.executeMethod(post);
			postResp = post.getResponseBodyAsString();
		} catch (IOException e) {

			e.printStackTrace();
		}
		System.out.println(postResp);

		// PARSEAR A INFORMACI�N EN XML
		// -----------------------------------------
		// Contar o n�mero de empresas
		try {
			// crea unha instancia do parteador
			XPath xPath = XPathFactory.newInstance().newXPath();
			InputSource inputSourcerContador = new InputSource(new StringReader(postResp));
			// crea a expresi�n
			String expresion = "count(response/result/Accounts/row)";
			// recupera o n�mero de contas
			Double numeroCuentas = (Double) xPath.evaluate(expresion, inputSourcerContador, XPathConstants.NUMBER);

			System.out.println("Numero cuentas=" + numeroCuentas);

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		// Obter datos (nome e tel�fono) da 1� empresa
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();

			InputSource inputSourcer1 = new InputSource(new StringReader(postResp));

			String expresion = "response/result/Accounts/row[0]/FL[@val='Account Name']/text()";
			String nombre = (String) xPath.evaluate(expresion, inputSourcer1, XPathConstants.STRING);

			InputSource inputSourcer2 = new InputSource(new StringReader(postResp));

			expresion = "response/result/Accounts/row[0]/FL[@val='Phone']/text()";
			String telefono = (String) xPath.evaluate(expresion, inputSourcer2, XPathConstants.STRING);

			System.out.println(nombre);
			System.out.println(telefono);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		// Obter datos de todas as empresas

		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			InputSource inputSourcerContador = new InputSource(new StringReader(postResp));
			String expresion = "count(response/result/Accounts/row)";

			Double numeroCuentas = (Double) xPath.evaluate(expresion, inputSourcerContador, XPathConstants.NUMBER);

			System.out.println("Numero cuentas=" + numeroCuentas);

			for (int i = 1; i <= numeroCuentas; i++) {
				InputSource inputSourcer1 = new InputSource(new StringReader(postResp));

				expresion = "response/result/Accounts/row[" + i + "]/FL[@val='Account Name']/text()";
				String nombre = (String) xPath.evaluate(expresion, inputSourcer1, XPathConstants.STRING);

				InputSource inputSourcer2 = new InputSource(new StringReader(postResp));

				expresion = "response/result/Accounts/row[" + i + "]/FL[@val='Phone']/text()";
				String telefono = (String) xPath.evaluate(expresion, inputSourcer2, XPathConstants.STRING);

				System.out.println(nombre);
				System.out.println(telefono);

			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

	}

	private static void mostrarLigasFutbol() {

		String key = "77cc1f9a011c7583dc33958e2aaa1443";

		String url = "http://www.apiclient.resultados-futbol.com/scripts/api/api.php?key=" + key
				+ "&format=xml&req=categories&filter=all";
		HttpClient cliente = new HttpClient();
		GetMethod get = new GetMethod(url);

		try {
			cliente.executeMethod(get);

			String xml = get.getResponseBodyAsString();

			xml = xml.replaceAll("\n", "");

			System.out.println(xml);

			get.releaseConnection();
			XPath parser = XPathFactory.newInstance().newXPath();
			InputSource input = new InputSource(new StringReader(xml));

			String reconto = "count(categories/category)";
			double numeroLigas = (double) parser.evaluate(reconto, input, XPathConstants.NUMBER);

			System.out.println("\nLISTA DE LIGAS DE F�TBOL\nN� de ligas: " + (int) numeroLigas);

			System.out.println(
					"Id \t IdLiga Orde \t Ano \t Alias \t Nome \t Pa�s \t Continente \t Xornada Actual \t Xornadas Totais");
			for (int i = 0; i < numeroLigas; i++) {
				InputSource is1 = new InputSource(new StringReader(xml));
				String expresionId = "categories/category[" + i + "]/id/text()";
				String id = (String) parser.evaluate(expresionId, is1, XPathConstants.STRING);

				InputSource is2 = new InputSource(new StringReader(xml));
				String expresionLeagueId = "categories/category[" + i + "]/league_id/text()";
				String leagueId = (String) parser.evaluate(expresionLeagueId, is2, XPathConstants.STRING);

				InputSource is3 = new InputSource(new StringReader(xml));
				String expresionOrder = "categories/category[" + i + "]/order/text()";
				String order = (String) parser.evaluate(expresionOrder, is3, XPathConstants.STRING);

				InputSource is4 = new InputSource(new StringReader(xml));
				String expresionYear = "categories/category[" + i + "]/year/text()";
				String year = (String) parser.evaluate(expresionYear, is4, XPathConstants.STRING);

				InputSource is5 = new InputSource(new StringReader(xml));
				String expresionAlias = "categories/category[" + i + "]/alias/text()";
				String alias = (String) parser.evaluate(expresionAlias, is5, XPathConstants.STRING);

				InputSource is6 = new InputSource(new StringReader(xml));
				String expresionName = "categories/category[" + i + "]/name/text()";
				String name = (String) parser.evaluate(expresionName, is6, XPathConstants.STRING);

				InputSource is7 = new InputSource(new StringReader(xml));
				String expresionCountry = "categories/category[" + i + "]/country/text()";
				String country = (String) parser.evaluate(expresionCountry, is7, XPathConstants.STRING);
				country = country.toUpperCase();

				InputSource is8 = new InputSource(new StringReader(xml));
				String expresionContinent = "categories/category[" + i + "]/continent/text()";
				String continent = (String) parser.evaluate(expresionContinent, is8, XPathConstants.STRING);
				continent = continent.toUpperCase();

				InputSource is9 = new InputSource(new StringReader(xml));
				String expresionCurrentRound = "categories/category[" + i + "]/current_round/text()";
				String currentRound = (String) parser.evaluate(expresionCurrentRound, is9, XPathConstants.STRING);

				InputSource is10 = new InputSource(new StringReader(xml));
				String expresionTotalRounds = "categories/category[" + i + "]/total_rounds/text()";
				String totalRounds = (String) parser.evaluate(expresionTotalRounds, is10, XPathConstants.STRING);

				System.out.println(id + "\t" + leagueId + "\t" + order + "\t" + year + "\t" + alias + "\t\t " + name
						+ "\t" + country + "\t" + continent + "\t" + currentRound + "\t" + totalRounds);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	private static void engadirOrzamento() {

		StringBuilder xml = new StringBuilder();
		// Vou simular dous produtos nun orzamento que coloco ao principio de
		// todo
		xml.append("<Quotes>");
		xml.append("<row no='1'>");
		xml.append("<FL val='Subject'>Orzamento para App Balaidos</FL>");
		xml.append("<FL val='Account Name'> App Inform�tica Balaidos </FL>");
		xml.append("<FL val='Product Details'>");

		xml.append("<product no='1'>");
		xml.append("<FL val='Product Name'>Monitor TFT Asus 17\"</FL>");
		xml.append("<FL val='Quantity'>5</FL>");
		xml.append("<FL val='Unit Price'>80</FL>");
		xml.append("<FL val='List Price'>Black Friday</FL>");
		xml.append("</product>");

		xml.append("<product no='2'>");
		xml.append("<FL val='Product Name'>Rat�n Wireless</FL>");
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
		// petici�n
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

			// clase que representa a petici�n
			PostMethod post = new PostMethod(url);

			// par�metros da petici�n
			post.setParameter("authtoken", token);
			post.setParameter("scope", "crmapi");

			// RECUPERAMOS A INFORMACI�N post
			// Executamos a petici�n con post
			httpclient.executeMethod(post);
			// Recuperamos o string
			String cadeaXml = post.getResponseBodyAsString();
			System.out.println(cadeaXml);

			// PASEAR A INFO CON XPATH
			// Crear o obxecto parseador
			XPath xPath = XPathFactory.newInstance().newXPath();
			InputSource inputsource = new InputSource(new StringReader(cadeaXml));

			// CONSULTAR
			// Expresi�n para contar as filas
			String expresionContar = "count(response/result/Contacts/row)";
			// Expresi�n para buscar a primeira fila e recuperar o nome
			// String expresionNomePrimeira =
			// "response/result/Contacts/row[1]/FL[@val='First Name']/text()";
			// obten o n�mero de contactos
			double numeroContactos = (double) xPath.evaluate(expresionContar, inputsource, XPathConstants.NUMBER);

			System.out.println("LISTA DE CONTACTOS \nN� de contactos: " + numeroContactos);
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
			xml.append(l.getFirstName());
			xml.append("</FL>");

			xml.append("<FL val='Last Name'>");
			xml.append(l.getLastName());
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

		// clase que representa a petici�n
		PostMethod post = new PostMethod(url);

		// par�metros da petici�n
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

			// identificaci�n
			String token = "0debe5329fd1efcf6f1b01de4703a756";

			HttpClient httpclient = new HttpClient();

			// clase que representa a petici�n
			post = new PostMethod(url);

			// par�metros da petici�n
			post.setParameter("authtoken", token);
			post.setParameter("scope", "crmapi");
			// parametro para inserir datos
			// post.setParameter("xmlData", conta.toXml());

			// Executamos a pecici�n
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
		// 1�. Crea un obxecto InputStreamReader
		InputStreamReader isr = new InputStreamReader(System.in);
		// 2�. Crea un obxecto BufferedReader
		BufferedReader br = new BufferedReader(isr);
		System.out.println(cad);
		return br.readLine();
	}

}
