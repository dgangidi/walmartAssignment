package walmartAssignment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
// This class is used to connect to walmart API and return xml Document.
public class WalmartConnector {
	public Document getSearchResult(String item){
		String urlAddress = "http://api.walmartlabs.com/v1/search?query="+item+"&format=xml&apiKey=spqkfzys5hxubmct5tgbs9ne";
		return getData(urlAddress);
	}
	
	public Document getRecommendationResult(String productId){
		String urlAddress = "http://api.walmartlabs.com/v1/nbp?apiKey=spqkfzys5hxubmct5tgbs9ne&itemId="+productId+"&format=xml";
		try{
			return getData(urlAddress);
		} catch(Exception e){
			return null;
		}
	}
	
	public Document getReviewResult(Integer prodId){
		String urlAddress = "http://api.walmartlabs.com/v1/reviews/"+prodId+"?format=xml&apiKey=spqkfzys5hxubmct5tgbs9ne";
		return getData(urlAddress);
	}
	/********* Method to return Document from the connection********/
	private Document getData(String urlAddress) {
		Document doc = null;
		try {
			URL url = new URL(urlAddress);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/xml");
			if (conn.getResponseCode() != 200) {
				//throw new RuntimeException("Failed, could not search product : HTTP error code : "
				//		+ conn.getResponseCode());
				return doc;          //If HTTP error: Instead of throwing exception, return null document
			}
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			try{
			doc = dBuilder.parse(conn.getInputStream());
			} catch(SAXException e){
				return doc;
			}
			conn.disconnect();
		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return doc;
	}
}
