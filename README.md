# walmartAssignment
Walmart Coding Assignment
WALMART CODING ASSIGNMENT
- DHEERAJ REDDY GANGIDI

For running the solution in Command Prompt (Windows):
PROJECT_FOLDER>mvn package

PROJECT_FOLDER>java -cp target/walmartAssignment-1.0-SNAPSHOT.jar walmartAssignment.Main


Solution example:
Enter the Item to be searched
ipod
*********************************** Metadata*************************
....getting ID....42608121
.........getting recommendation id's.......
25857866
30146246
42608125
39875894
47055692
45804400
42807912
31232984
42608106
45804384
..... getting review ratings....5.0
..... getting review ratings....4.78
..... getting review ratings....4.56
..... getting review ratings....4.15
..... getting review ratings....4.76
..... getting review ratings....3.98
..... getting review ratings....0.0
..... getting review ratings....4.56
..... getting review ratings....4.42
..... getting review ratings....4.38
***********************************************************************
Output:

OtterBox Defender Series Case for Apple iPod touch 5th Generation--Review Rating: 5.0
Apple iPad mini 2 16GB WiFi--Review Rating: 4.78
Sandisk microSD 32GB Class 10 Memory Card--Review Rating: 4.76
Apple iPod touch 32GB, Assorted Colors--Review Rating: 4.56
Beats by Dr. Dre  Drenched Solo On-Ear Headphones, Assorted Colors--Review Rating: 4.56
Apple iPod nano 16GB, Assorted Colors--Review Rating: 4.42
RCA Viking Pro 10.1" 2-in-1 Tablet 32GB Quad Core--Review Rating: 4.38
Griffin Survivor Extreme-Duty Case for Apple iPod touch 5G, Blue--Review Rating: 4.15
RCA 7" Tablet 16GB Quad Core--Review Rating: 3.98
OtterBox Vibrant Screen Protector for Apple iPod Touch 5G--Review Rating: Not available


Solution Overview:
-> The application calls the search API to retrieve the product id. - searchProduct(String item)
-> Then the application calls the Product recommendation API to retrieve top 10 Recommended product ID's - getRecommendations(String productId)
-> For each recommended product, the application calls the Review API to retrieve the product name and it's average overall ratings - getReviews(ArrayList<Integer> recommendedProds)
-> The product names are ranked according to the ratings and are displayed. - rankReviews(Map<Double, ArrayList<String>> prodReviews)

** The WalmartConnector class is used to connect to the WALMARTLABS API and return the Document.
** ApplicationTest class is used to do the unit testing of the Application using JUnit.
JUnit version 4.11 Dependency is used for performing JUnit testing
org.mockito.mockito-core version 1.9.5 is the Mockito framework used. For mocking the dependency classes during unt testing.
