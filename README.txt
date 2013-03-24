Dan Dutrow
Enterprise Java
Spring 2013

	
Technical Details:
1.	Design 2 sets of database schema
	A.	eSales Database Schema (dutrowSalesImpl annotations in src/main/java/dutrow.sales.bo and src/main/resources/ddl)
	B.	eBidbot Database Schema (dutrowBidbotImpl annotations in src/main/java/dutrow.bidbot.bo and src/main/resources/ddl)
2.	Design a set of business objects to represent the data in the system
	A.	eSales Business Objects (dutrowSalesImpl src/main/java/dutrow.sales.bo)
	B.	eBidbot Business Objects (dutrowBidbotImpl src/main/java/dutrow.bidbot.bo)
3.	Design and implement a mechanism to ingest a starting state (dutrowSalesImpl src/main/java/dutrow.sales.bl.impl.Ingestor)
4.	Design and implement a DAO layer
	AccountDAO and AuctionDAO (dutrowSalesImpl src/main/java/dutrow.sales.dao)
	JPAAccountDAO and JPAAuctionDAO (dutrowSalesImpl src/main/java/dutrow.sales.dao.jpa)
	BidAccountDAO (dutrowBidbotImpl src/main/java/dutrow.bidbot.dao)
	JPABidAccountDAO (dutrowBidbotImpl src/main/java/dutrow.bidbot.dao.jpa)
5.	Design an initial business interface and business logic
	A.	eSales Candidate Business Logic (dutrowSalesImpl src/main/java/dutrow.sales.bl and dutrow.sales.bl.impl)
	B.	eBidbot Candidate Business Logic (dutrowBidbotImpl src/main/java/dutrow.bidbot.bl and dutrow.bidbot.bl.impl)

Testing:
	All you will have to do is run mvn clean install from the dutrowMarket directory.
	You can run with the h2db or h2srv profiles.

1.	Provide a JUnit test for your business objects that test the manipulation of the data.
	(dutrowSalesImpl src/test/java/dutrow.sales.bo/*)
	(dutrowBidbotImpl src/test/java/dutrow.bidbot.bo/*)
2.	Provide a JUnit test for your JPA DAOs.
	(dutrowSalesImpl src/test/java/dutrow.sales.dao/*)
	(dutrowBidbotImpl src/test/java/dutrow.bidbot.dao/*)
3.	Provide a set of JUnit test programs to verify the following end-to-end functional scenario in eSales.
	(dutrowSalesImpl src/test/java/dutrow.sales.bl.EndToEndTest)
4.	Provide a set of JUnit test programs to verify the following end-to-end functional scenario in eBidbot.	
	(dutrowBidbotImpl src/test/java/dutrow.sales.bl.EndToEndTest)
