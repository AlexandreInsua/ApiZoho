package aplicacionAPI;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.xml.sax.InputSource;

public class Account {
	private static String accountName;
	private static String phone;
	private static String webSite;

	public Account(String accountName, String phone, String web) {
		super();
		this.accountName = accountName;
		this.phone = phone;
		this.webSite = web;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public static ArrayList<Account> xmlToListAccount(String xml) {
		// o resultado vai ser un arraylist de contes
		ArrayList<Account> accountsList = new ArrayList<Account>();

		try {// instancia do parseador
			XPath xpath = XPathFactory.newInstance().newXPath();

			// pasa de string a xml
			InputSource inputsource = new InputSource(new StringReader(xml));

			// conta o número de contas

			String expresion = "count(response/result/Accounts/row)";
			Double numeroContas = (Double) xpath.evaluate(expresion, inputsource, XPathConstants.NUMBER);
			System.out.println("Numero de contas: " + numeroContas);

			// recupera o contido as etiquetas FL cuxo atributo sexa "Account Name" de cada
			// conta
			for (int i = 1; i <= numeroContas; i++) {
				inputsource = new InputSource(new StringReader(xml));

				expresion = "response/result/Accounts/row[" + i + "]/FL[@val='Account Name']/text()";
				String nomeConta = (String) xpath.evaluate(expresion, inputsource, XPathConstants.STRING);

				inputsource = new InputSource(new StringReader(xml));
				expresion = "response/result/Accounts/row[" + i + "]/FL[@val='Phone']/text()";
				String telefono = (String) xpath.evaluate(expresion, inputsource, XPathConstants.STRING);

				String web = null;
				Account a = new Account(nomeConta, telefono, web);
				accountsList.add(a);
			}

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return accountsList;
	}

	public static void listToExcell(ArrayList<Account> list, String ficheiro) {
		HSSFWorkbook libro = new HSSFWorkbook();
		HSSFSheet folla = libro.createSheet();
		HSSFRow fila = folla.createRow(0);
		HSSFCell cela0 = fila.createCell(0);
		HSSFCell cela1 = fila.createCell(1);

		cela0.setCellValue("Nome da conta");
		cela1.setCellValue("Teléfono da conta");

		int contadorFilas = 1;
		for (Account conta : list) {
			fila = folla.createRow(contadorFilas);
			cela0 = fila.createCell(0);
			cela1 = fila.createCell(1);
			cela0.setCellValue(conta.getAccountName());
			cela1.setCellValue(conta.getPhone());
		}

	}

	private static void gardarExcell(HSSFWorkbook libro, String ruta) {

		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(ruta);
			libro.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				libro.close();
				fos.close();
			} catch (IOException e) {

			}

		}
	}

	public static String toXml() {
		String xml;
		xml = "<Accounts>";
		xml += "<row no='1'>";
		xml += "<FL val='Account Name'>" + accountName + "</FL>";
		xml += "<FL val='Website'>" + webSite + "</FL>";
		xml += "<FL val='Phone'>" + phone + "</FL>";
		xml += "</row>";
		xml += "</Accounts>";
		return xml;
	}

	@Override
	public String toString() {
		return "Account [accountName=" + accountName + ", phone=" + phone + ", webSite=" + webSite + "]";
	}
}
