Dan Dutrow
Enterprise Java
Spring 2013

Intro:
	eSales is planning an on-line auction site to allow sellers to 
	auction items and buyers to place bids and ultimately purchase products. 
	At the same time, eBidbot is planning an on-line automated bidding site 
	that will help bidders make bids with eSales.
	
	Sellers can start an auction using the eSales on-line auction site. 
	They specify a title, category, description, asking price, start/stop time, 
	and any images. Using the eSales buyer interface, buyers can manually
	bid on auctions until they close.
	
	With eBidbot, buyers also have the option of using an independent site 
	to place their bids. Buyers place orders with eBidbot, providing them 
	the auction and min/max bidding parameters. eBidbot will become a trusted 
	client of eSales and be able to make bids on behalf of the actual buyer.
	
	Both eSales and eBidbot have come to you to develop the initial phase of 
	their applications. You are tasked with implementing a low-cost prototype,
	based on current standards, to automate much of this activity. 
	At this point in the project we are primarily looking to build the data 
	access tiers for both the eSales and eBidbot (two separate systems). 
	We will also add a minor amount of business logic to coordinate the 
	data access between the individual data access objects.
	
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
1.	Provide a JUnit test for your business objects that test the manipulation of the data.
	(dutrowSalesImpl src/test/java/dutrow.sales.bo)
	(dutrowBidbotImpl src/test/java/dutrow.bidbot.bo)
2.	Provide a JUnit test for your JPA DAOs.
	(dutrowSalesImpl src/test/java/dutrow.sales.dao)
	(dutrowBidbotImpl src/test/java/dutrow.bidbot.dao)
3.	Provide a set of JUnit test programs to verify the following end-to-end functional scenario in eSales.
	(dutrowSalesImpl src/test/java/dutrow.sales.bl.EndToEndTest)
4.	Provide a set of JUnit test programs to verify the following end-to-end functional scenario in eBidbot.	
	(dutrowBidbotImpl src/test/java/dutrow.sales.bl.EndToEndTest)