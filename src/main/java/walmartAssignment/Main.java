package walmartAssignment;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.w3c.dom.Document;

public class Main {
	
	private WalmartConnector apicall = new WalmartConnector();
    // WalmartConnector class is used to get connection to Walmart API's and return data.
	public WalmartConnector getApicall() {
		return apicall;
	}

	public void setApicall(WalmartConnector apicall) {
		this.apicall = apicall;
	}
	
	/** Main Method**/
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(System.getProperty("line.separator")+"Enter the Item to be searched");
		Scanner in = new Scanner(System.in);
		String item = in.next();
		Main app = new Main();
		if(item.equals("")) System.out.println("No product searched");
		else{ 
			String productId = app.searchProduct(item);
			if (productId.equals("")) System.out.println("Your search for "+item+" did not match any products.");
			else{
				ArrayList<Integer> recommendedProds = app.getRecommendations(productId);
				if(recommendedProds == null || recommendedProds.size() == 0) System.out.println("No recommendations available");
				else {
					Map<Double, ArrayList<String>> prodReviews = app.getReviews(recommendedProds);
					System.out.println(System.getProperty("line.separator")+"Output:"+System.getProperty("line.separator"));
					System.out.println(app.rankReviews(prodReviews));
				}
			}
		}
	}
	
	/*** Method to Rank Reviews based on Review rating****/
	public String rankReviews(Map<Double, ArrayList<String>> prodReviews){
		ArrayList<Double> keys = new ArrayList<Double>(prodReviews.keySet());
		String output = "";
		String newLine = System.getProperty("line.separator");
		for (int i=keys.size()-1; i>=0;i--){       //get the keys in reverse sorted order and retrieve product names
			Double rating = keys.get(i);
			for(String temp: prodReviews.get(rating)){
				if (rating == 0.0) output += temp+"--Review Rating: Not available" + newLine;
				else output +=  temp+"--Review Rating: "+ rating + newLine;
			}
		}
		return output;
	}
	
	/****** Method to return Recommended product names & Reviews. Input: Recommended product Id's*****/
	public Map<Double, ArrayList<String>> getReviews(ArrayList<Integer> recommendedProds) {
		Integer prods = recommendedProds.size();
		Map<Double, ArrayList<String>> productRatings = new TreeMap<Double, ArrayList<String>>();
		for(int i=0; i<prods; i++){
			Integer prodId = recommendedProds.get(i);
			boolean resp = true;
			Document doc = null; int j =0;
			while(resp){
				doc = apicall.getReviewResult(prodId); resp = false;
				if(doc == null){ //doc  = null => an exception calling API. wait for 1 second, and call the API again
					resp = true; j++;
					if(j ==2) break; //If Document is still null after 3 calls=> problem with server. Stop re-trying
					try {
						Thread.sleep(1000);    
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			Double rating = 0.0;          // Default rating if no reviews are available
			if (doc.getElementsByTagName("averageOverallRating").getLength() >0){
				rating = Double.parseDouble(doc.getElementsByTagName("averageOverallRating").item(0).getTextContent());
			}
			System.out.println("..... getting review ratings...."+rating);
			String name = doc.getElementsByTagName("name").item(0).getTextContent();
			ArrayList temp = null;        //Store the product names of a particular rating in an array.
			if (productRatings.containsKey(rating)) {
				temp = productRatings.get(rating);
				if(temp == null)
					temp = new ArrayList();
				temp.add(name);
			}
			else {
				temp = new ArrayList();
				temp.add(name);               
			}
			productRatings.put(rating, temp);
		}
		return productRatings;
	}

	/******** Method to get top 10 recommended product Id's. Input: Searched product ID***********/
	public ArrayList<Integer> getRecommendations(String productId) {
		ArrayList<Integer> recommendedProd = new ArrayList<Integer>();
		Document doc = apicall.getRecommendationResult(productId);
		if(doc == null) return null;
		int results = doc.getElementsByTagName("itemId").getLength();
		int totalItems = Math.min(results,10);              //If less than 10 recommendations, then take all recommendations. Recommendations limit: First 10
		System.out.println(".........getting recommendation id's.......");
		for(int i=0; i<totalItems; i++){                // Store the recommendations
			recommendedProd.add(Integer.parseInt(doc.getElementsByTagName("itemId").item(i).getTextContent()));
			System.out.println(recommendedProd.get(i));
		}
		return recommendedProd;
	}
	
	/********* Method to retrieve product id of searched string. Input: Searched string************/
	public String searchProduct(String item) {
		String productId = "";
		Document doc = apicall.getSearchResult(item);    //Connect to walmart search API and retrieve first product ID.
		int results = Integer.parseInt(doc.getElementsByTagName("totalResults").item(0).getTextContent());
		if(results > 0){
			productId = doc.getElementsByTagName("itemId").item(0).getTextContent();
			System.out.println("....getting product ID...."+productId);
		}
		return productId;
	}
}