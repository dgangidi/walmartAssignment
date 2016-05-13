package walmartAssignment;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;
import static org.mockito.Mockito.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class ApplicationTest {
	
	@Test
	public void testRankReviews() {
		Main app = new Main();
		Map<Double, ArrayList<String>> prodReviews = getReviewsToSort();
		String newLine = System.getProperty("line.separator");
		String expectedOutput = "iphone--Review Rating: 5.0"+ newLine+"ipod--Review Rating: 5.0"+ newLine
				+"iwatch--Review Rating: 5.0"+ newLine+"ipod case--Review Rating: 3.0"+ newLine
				+"iwatch case--Review Rating: 3.0"+ newLine+"iphone screen gaurd--Review Rating: Not available"+ newLine;
		assertEquals(app.rankReviews(prodReviews), expectedOutput);
	}
	
	@Test
	public void testRankReviewsForNull() {
		Main app = new Main();
		Map<Double, ArrayList<String>> prodReviews = new TreeMap<Double, ArrayList<String>>();
		String expectedOutput = "";
		assertEquals(app.rankReviews(prodReviews), expectedOutput);
	}

	@Test
	public void testSearchProduct() throws ParserConfigurationException{
		Document doc = createDocument1();
		Main app = new Main();
		WalmartConnector tester = mock(WalmartConnector.class);
		when(tester.getSearchResult(anyString())).thenReturn(doc);
		app.setApicall(tester);
		String result = app.searchProduct("test input");
		assertEquals(result, "12345");
	}
	
	@Test
	public void testSearchProductNoResults() throws ParserConfigurationException{
		Document doc = createDocument2();
		Main app = new Main();
		WalmartConnector tester = mock(WalmartConnector.class);
		when(tester.getSearchResult(anyString())).thenReturn(doc);
		app.setApicall(tester);
		assertEquals(app.searchProduct("test input"), "");
	}
	
	@Test (expected = NullPointerException.class)
	public void testSearchProductForNull() throws ParserConfigurationException{
		Main app = new Main();
		WalmartConnector tester = mock(WalmartConnector.class);
		when(tester.getSearchResult(anyString())).thenReturn(null);
		app.setApicall(tester);
		app.searchProduct("test input");
	}
	
	@Test
	public void testGetRecommendations() throws ParserConfigurationException{
		Document doc = createDocument3();
		Main app = new Main();
		WalmartConnector tester = mock(WalmartConnector.class);
		when(tester.getRecommendationResult(anyString())).thenReturn(doc);
		app.setApicall(tester);
		List<Integer> expectedResult = new ArrayList<Integer>(Arrays.asList(1111, 2222, 3333));
		assertEquals(app.getRecommendations("12345"), expectedResult);
	}
	
	@Test
	public void testGetRecommendationsForNullDoc(){
		Main app = new Main();
		WalmartConnector tester = mock(WalmartConnector.class);
		when(tester.getSearchResult(anyString())).thenReturn(null);
		app.setApicall(tester);
		assertEquals(app.getRecommendations("12345"), null);
	}
	
	@Test
	public void testGetReviews() throws ParserConfigurationException{
		Document doc = createDocument4();
		Main app = new Main();
		WalmartConnector tester = mock(WalmartConnector.class);
		when(tester.getReviewResult(anyInt())).thenReturn(doc);
		app.setApicall(tester);
		ArrayList<Integer> input = new ArrayList<Integer>(Arrays.asList(1111, 2222, 3333));
		Map<Double, ArrayList<String>> prodReviews = new TreeMap<Double, ArrayList<String>>();
		ArrayList<String> prods = new ArrayList<String>();
		prods.add("test product"); prods.add("test product"); prods.add("test product");
		prodReviews.put(4.67, prods);
		assertEquals(app.getReviews(input), prodReviews);
	}
	
	@Test
	public void testGetReviewsNoReviews() throws ParserConfigurationException{
		Document doc = createDocument5();
		Main app = new Main();
		WalmartConnector tester = mock(WalmartConnector.class);
		when(tester.getReviewResult(anyInt())).thenReturn(doc);
		app.setApicall(tester);
		ArrayList<Integer> input = new ArrayList<Integer>(Arrays.asList(1111, 2222));
		Map<Double, ArrayList<String>> prodReviews = new TreeMap<Double, ArrayList<String>>();
		ArrayList<String> prods = new ArrayList<String>();
		prods.add("test product"); prods.add("test product");
		prodReviews.put(0.0, prods);
		assertEquals(app.getReviews(input), prodReviews);
	}
	
	@Test (expected = NullPointerException.class)
	public void testGetReviewsForNullDoc() throws ParserConfigurationException{
		Main app = new Main();
		WalmartConnector tester = mock(WalmartConnector.class);
		when(tester.getReviewResult(anyInt())).thenReturn(null);
		app.setApicall(tester);
		ArrayList<Integer> input = new ArrayList<Integer>(Arrays.asList(1111, 2222));
		assertEquals(app.getReviews(input), null);
	}
	
	
	@Test (expected = NullPointerException.class)
	public void testGetReviewsForNull() throws ParserConfigurationException{
		Main app = new Main();
		WalmartConnector tester = mock(WalmartConnector.class);
		when(tester.getReviewResult(anyInt())).thenReturn(null);
		app.setApicall(tester);
		app.getReviews(null);
	}
	
	/***************** END OF TESTS*********************/
	
	/*********** Methods To Create return Objects for Mocked Methods*******/
	
	private Map<Double, ArrayList<String>> getReviewsToSort() {
		Map<Double, ArrayList<String>> prodReviews = new TreeMap<Double, ArrayList<String>>();
		ArrayList<String> prods1 = new ArrayList<String>();
		prods1.add("iphone"); prods1.add("ipod"); prods1.add("iwatch");
		prodReviews.put(5.0, prods1);
		ArrayList<String> prods2 = new ArrayList<String>();
		prods2.add("iphone screen gaurd");
		prodReviews.put(0.0, prods2);
		ArrayList<String> prods3 = new ArrayList<String>();
		prods3.add("ipod case"); prods3.add("iwatch case");
		prodReviews.put(3.0, prods3);
		return prodReviews;
	}

	private Document createDocument1() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element rootElement = doc.createElement("searchresponse");
		doc.appendChild(rootElement);
		Element totalResultsElement = doc.createElement("totalResults");
		rootElement.appendChild(totalResultsElement);
		totalResultsElement.insertBefore(doc.createTextNode("1"), totalResultsElement.getLastChild());
		Element itemsElement = doc.createElement("items");
		rootElement.appendChild(itemsElement);
		Element itemElement = doc.createElement("item");
		itemsElement.appendChild(itemElement);
		Element itemIdElement = doc.createElement("itemId");
		itemElement.appendChild(itemIdElement);
		itemIdElement.insertBefore(doc.createTextNode("12345"), itemIdElement.getLastChild());
		return doc;
	}
	
	private Document createDocument2() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element rootElement = doc.createElement("searchresponse");
		doc.appendChild(rootElement);
		Element totalResultsElement = doc.createElement("totalResults");
		rootElement.appendChild(totalResultsElement);
		totalResultsElement.insertBefore(doc.createTextNode("0"), totalResultsElement.getLastChild());
		return doc;
	}
	
	private Document createDocument3() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element rootElement = doc.createElement("nextBestProducts");
		doc.appendChild(rootElement);
		Element itemElement = doc.createElement("item");
		rootElement.appendChild(itemElement);
		Element itemIdElement = doc.createElement("itemId");
		itemElement.appendChild(itemIdElement);
		itemIdElement.insertBefore(doc.createTextNode("1111"), itemIdElement.getLastChild());
		Element itemElement2 = doc.createElement("item");
		rootElement.appendChild(itemElement2);
		Element itemIdElement2 = doc.createElement("itemId");
		itemElement2.appendChild(itemIdElement2);
		itemIdElement2.insertBefore(doc.createTextNode("2222"), itemIdElement2.getLastChild());
		Element itemElement3 = doc.createElement("item");
		rootElement.appendChild(itemElement3);
		Element itemIdElement3 = doc.createElement("itemId");
		itemElement3.appendChild(itemIdElement3);
		itemIdElement3.insertBefore(doc.createTextNode("3333"), itemIdElement3.getLastChild());
		return doc;
	}
	
	private Document createDocument4() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element rootElement = doc.createElement("itemReview");
		doc.appendChild(rootElement);
		Element nameElement = doc.createElement("name");
		rootElement.appendChild(nameElement);
		nameElement.insertBefore(doc.createTextNode("test product"), nameElement.getLastChild());
		Element reviewStatisticsElement = doc.createElement("reviewStatistics");
		rootElement.appendChild(reviewStatisticsElement);
		Element averageOverallRatingElement = doc.createElement("averageOverallRating");
		reviewStatisticsElement.appendChild(averageOverallRatingElement);
		averageOverallRatingElement.insertBefore(doc.createTextNode("4.67"), averageOverallRatingElement.getLastChild());
		return doc;
	}
	
	private Document createDocument5() throws ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element rootElement = doc.createElement("itemReview");
		doc.appendChild(rootElement);
		Element nameElement = doc.createElement("name");
		rootElement.appendChild(nameElement);
		nameElement.insertBefore(doc.createTextNode("test product"), nameElement.getLastChild());
		return doc;
	}
}
