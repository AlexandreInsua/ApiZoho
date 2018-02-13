package aplicacionAPI;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.xpath.*;

import org.xml.sax.InputSource;

public class Contact {

	private String lastName;
	private String firstName;
	private String contactOwnwer;
	private String email;
	private String phone;

	
	public Contact(String lastName, String firstName) {
		this.lastName = lastName;
		this.firstName = firstName;
	}
	
	public Contact(String lastName, String firstName, String contactOwnwer, String email, String phone) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.contactOwnwer = contactOwnwer;
		this.email = email;
		this.phone = phone;
	}

	public String getLastName() {
		return lastName;
	}

	
	

	public String getFirstName() {
		return firstName;
	}

	public String getContactOwnwer() {
		return contactOwnwer;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setContactOwnwer(String contactOwnwer) {
		this.contactOwnwer = contactOwnwer;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Contact [lastName=" + lastName + ", firstName=" + firstName + ", contactOwnwer=" + contactOwnwer
				+ ", email=" + email + ", phone=" + phone + "]";
	}

	public static ArrayList<Contact> xmltoContactList(String xml) {
		ArrayList<Contact> lista = new ArrayList<Contact>();
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			InputSource inputSourcerContador = new InputSource(new StringReader(xml));
			String expresion = "count(response/result/Contacts/row)";

			Double numeroContactos = (Double) xPath.evaluate(expresion, inputSourcerContador, XPathConstants.NUMBER);

			System.out.println("Numero contactos=" + numeroContactos);

			for (int i = 1; i <= numeroContactos; i++) {
				InputSource inputSourcer1 = new InputSource(new StringReader(xml));

				expresion = "response/result/Contacts/row[" + i + "]/FL[@val='First Name']/text()";
				String nombre = (String) xPath.evaluate(expresion, inputSourcer1, XPathConstants.STRING);

				InputSource inputSourcer2 = new InputSource(new StringReader(xml));

				expresion = "response/result/Contacts/row[" + i + "]/FL[@val='Last Name']/text()";
				String apellido = (String) xPath.evaluate(expresion, inputSourcer2, XPathConstants.STRING);

				Contact contacto = new Contact(nombre, apellido);
				lista.add(contacto);

			
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return lista;
	}

}
