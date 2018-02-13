package aplicacionAPI;

public class Lead {

	public String lastName;
	public String company;
	
	public Lead() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Lead(String lastName, String company) {
		this.lastName = lastName;
		this.company = company;
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
	
	
	public String toXml(){
		String xml;
		xml = "<Leads>";
		xml += "<row no='1'>";
		xml += "<FL val='Last Name'>"+ lastName +"</FL>";
		xml += "<FL val='Company'>"+ company+"</FL>";
		xml += "</row>";
		xml += "</Leads>";
		
		return xml;
	}
	
	
}
