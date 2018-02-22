package aplicacionCars;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class myCarsJson {

	public static void main(String[] args) {

		jsonToCars();

	}

	public static void jsonToCars() {
		String jsonData = "";
		String modelo = "";
		int prezo = 0;

		String url = "http://localhost:4000/api/cars";
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(url);

		try {
			client.executeMethod(get);
			jsonData = get.getResponseBodyAsString();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// ======== parseo do json
		JsonParser parser = new JsonParser();
		JsonObject raiz = parser.parse(jsonData).getAsJsonObject();
		JsonArray data = raiz.get("data").getAsJsonArray();

		System.out.println("LISTADO DE COCHES");
		System.out.println("=======================");
		System.out.println("Modelo \t Prezo ");
		
		for (JsonElement date : data) {
			modelo = date.getAsJsonObject().get("modelo").getAsString();
			prezo = date.getAsJsonObject().get("prezo").getAsInt();
			System.out.println(modelo + " " + prezo +"€");
		}
	}
}
