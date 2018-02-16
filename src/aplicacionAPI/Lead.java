package aplicacionAPI;

public class Lead {
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private String company;

	public Lead() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Lead(String lastName, String company) {
		this.lastName = lastName;
		this.company = company;
	}

	public Lead(String firstName, String lastName, String phone, String email, String company) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.company = company;
	}

	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLastName() {
		return lastName;
	}

	public String getCompany() {
		return company;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "Lead [lastName:" + lastName + ", company:" + company + "]";
	}

	public String toXml() {
		String xml;
		xml = "<Leads>";
		xml += "<row no='1'>";
		xml += "<FL val='Last Name'>" + lastName + "</FL>";
		xml += "<FL val='Company'>" + company + "</FL>";
		xml += "</row>";
		xml += "</Leads>";

		return xml;
	}

}
