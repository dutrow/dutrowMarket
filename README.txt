Dan Dutrow
Enterprise Java
Spring 2013

Project 3

A few convenient commands/bookmarks
	mvn clean pre-integration-test -DskipTests; mvn test -rf :dutrowBidbot
	mvn clean pre-integration-test -DskipTests; mvn verify -rf :dutrowBidbot -Dit.test=dutrow.bidbot.ejbclient.EndToEndIT
	from dutrowSalesImpl/ run  $mvn clean install -DskipTests; ant -f target/test-classes/jmsNotifier-ant.xml subscriber
	http://localhost:8080/dutrowBidbot
	http://localhost:8080/dutrowSales
	

1.		Provide all functionality from Projects 1 and 2.
			All functionality is maintained (as submitted) from projects 1 and 2. The following enhancements have been made over previous submissions
				- Changed the bidbot URL to http://localhost:8080/dutrowBidbot
				- All forms and EJBs that previously required userId inputs are now replaced with context/request-based user credentials.
				- Added remote calls from eBidbot to eSales
				- Moved EndToEndIT from eBidbot to eSales
				
2.		Enhance eSales with access restrictions.
	a.	Assign the EJB to the "other" security domain.
			see: jboss-ejb3.xml - dutrowSalesEJB/src/main/resources/META-INF
	b.	Restrict access to the EJB methods seller/buyer account-specific methods to to users within the esales role. 
			see: dutrow.sales.ejb.*EJB.java - dutrowSalesEJB/src/main/java

	c.	Restrict access beyond the main page to users with the esales role. You may use FORM or BASIC authentication. (I suggest FORM for easy logout/login as new user).
			see: web.xml - dutrowSalesWAR/src/main/webapp/WEB-INF <security-constraint>...</security-constraint>
			a login form is available for your convenience with a choice of users to select from
			a logout button is available on the main menu
			
	d.	Also restrict them to only working with their own account and derived the account name from their login.
			done: all methods that use to require a userId now gets that from the session context.
			
	c.	Allow any user to perform read operations.
			see: @PermitAll in *EJB.java
	d.	Restrict any administrative functions to the esales-admin role.
			see: @RolesAllowed({"esales-admin", "admin"}) in SupportEJB.java
			
3.		Extend your existing RMI Test project and RMI client(s) to address new authentication requirements.
	a.	Add a valid login to your existsing tests to re-enable them under the newly secured environment.
			see: runAs(String username, String password) in dutrow.sales.ejbclient.Support.java - dutrowSalesTest/src/test/java
			this method is invoked to pass the credentials of valid users for each of the actions
			
	b.	Add a new unit test that verifies the access controls of the protected and open methods.
			see: accessControlTest in dutrow.sales.ejbclient.SalesAccessControlIT - dutrowSalesTest/src/test/java
			this test mimics most functionality in EndToEndIT, except the expected case is failure to call EJBs
			look for "Caught EJBAccessException: good!" in test output
			
			
4.		Enhance eBidbot with access restrictions and the ability to log into eSales.
	a.	Assign the EJB to the "other" security domain.
			see: jboss-ejb3.xml - dutrowBidbotWAR/src/main/resources/META-INF
			
	b.	Restrict access to the OrderMgmt to users in the ebidbot role once they get beyond creating an account.
			see: dutrow.bidbot.ejb.OrderMgmtEJB.java in dutrowBidbotWAR/src/main/java
			
	c.	Run-as an esales-trusted user when making bids on behalf of a user. This may require the use of a "helper" EJB to encapsulate the scope of the run-as role/identity.
			see: dutrow.bidbot.ejb.OrderMgmtHelperEJB.java - dutrowBidbotWAR/src/main/java which has @RunAs("esales-trusted")
			that specific buyerManager.placeBid method will only run as the esales-trusted user because a sales account userid is parameterized
			
5.		Extend your existing RMI Test project and RMI client(s) to address new authentication requirements.
	a.	Add a valid login to your existsing tests to re-enable them under the newly secured environment.
			see: runAs(String username, String password) in dutrow.bidbot.ejbclient.Support.java - dutrowBidbotWAR/src/test/java
			this method is invoked to pass the credentials of valid users for each of the actions
			
	b.	Add a new unit test that verifies the access controls of the protected and open methods
			see: accessControlTest in dutrow.bidbot.ejbclient.BidbotAccessControlIT - dutrowBidbotWAR/src/test/java
			this test mimics most functionality in OrderMgmtIT, except the expected case is failure to call EJBs
			look for "Caught EJBAccessException: good!" in test output

6.		Enhance eSalesWAR with access restrictions.
	a.	Assign the WAR to the "other" security domain.
			see: jboss-ejb3.xml - dutrowBidbotWAR/src/main/webapp/WEB-INF
	
	b.	Restrict access beyond the main page to users with the esales role. You may use FORM or BASIC authentication. (I suggest FORM for easy logout/login as new user).	
			see: web.xml - dutrowSalesWAR/src/main/webapp/WEB-INF <security-constraint>...</security-constraint>
			a login form is available for your convenience with a choice of users to select from
			a logout button is available on the main menu
			
	c.	Permit only users to only ask the EJB tier for account information that is associated with their login.
			done: all methods that use to require a userId now gets that from the session context.

7.		Extend your eSales EJB implementations to publish changes to Auctions.
	a.	Use the emarket-esales-auction topic (JNDI name: topic/ejava/projects/emarket/esales-auction)
			Actually Jim, it's emarket-esales-action
	
	b.	Design your JMS Message. You can use any JMS Type and JMS/custom properties you wish. 
			dutrow.sales.ejb.SalesHelperEJB.java - dutrowSalesEJB/src/main/java
			void dutrow.sales.ejb.SalesHelperEJB.publishAuctionItem(Session session, AuctionItem item, String jmsType) throws JMSException{...}
			done: I implemented v1 with a MapMessage, but now am using an ObjectMessage -- the DTOs wont deserialize in the dutrowSalesImpl
			-- because they are only available in the EJB layer or through the ejb-client jar but that's okay since the Subscriber is allowed to be simple.
		
		However, you need to account for the fact that subscribers will be filtering on such things as the category or state of an auction.
			done: the message has a "category" string property and "open" boolean property
	
	c.	Have your eSales EJBs publish JMS Messages to the topic when the Auction changes state (created, bid, close).
			dutrow.sales.ejb.SellerMgmtEJB
				createAuction -> "forSale" (object payload: AuctionDTO)
				
			dutrow.sales.ejb.BuyerMgmtEJB
				placeBid -> "bid" (object payload: BidDTO)
	
			dutrow.sales.ejb.SalesHelperEJB::
				checkAuction -> "saleUpdate" (object payload: AuctionDTO)
				closeBidding -> "closed" (object payload: AuctionDTO)
				sellItem -> "sold" (object payload: AuctionDTO)
				
			
8.		Add a Java SE JMS subscriber to consume events about Auctions pertaining to a specific category.
			see: dutrow.sales.jms.Subscriber.java - dutrowSalesImpl/src/main/java [this is 95% the same as the jmsNotifier example -- I only added a more verbose printout]
			this was made as simple as I could conceive possible -- no local hornetq tests or anything
			output:     [java]  -subscriber Subscriber0 starting:durable=false, selector=JMSType in ('forSale', 'closed') username: user1 password: password
			
9.		Implement a Message Driven Bean within eBidbot to subscribe to auction closed events.
			dutrow.bidbot.ejb.BidbotMDB.java - dutrowBidbotWAR/src/main/java

	a.	Use a JMS Selector for the MDB to limit the types of messages consumed.
			@ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "JMSType in ('closed', forSale)"),
	
	b.	Update any orders as being closed and with results based on the contents of the JMS message.
			when the "closed" message comes in, BidbotMDB calls orderMgmt.endOrder(dto) to update and close the order;

10.		Implement an EJB Timer that will allow eSales to automatically wake-up and expire auctions.
			dutrow.sales.ejb.SellerMgmtEJB
				@Timeout @Transient	@Schedule(...)
				public void execute(Timer timer) { calls esalessys.checkAuction(); }
			SalesHelperEJB.checkAuction() expires auctions.
				
11.		Implement an EJB Timer that will allow eBidbot to periodically wake-up and check on the open auctions that it has bid orders for.
			dutrow.bidbot.ejb.OrderMgmtEJB
				@Timeout @Transient	@Schedule(...)
				public void execute(Timer timer) { calls orderMgmtHelper.checkAuction(); }
			OrderMgmtHelperEJB.checkAuction check on the open auctions that it has bid orders for - and bids if warranted
				
				
Testing
1.		Provide JUnit tests that verify the EJB functionality of eSales accessed through its remote interface using new access control restrictions.
			- All dutrow.sales.ejbclient.*IT tests have been updated to runAs with proper access
			- SalesAccessControlIT shows failures when attempting to get access controlled resources without proper access

2.		Provide JUnit tests that verify the EJB functionality of eBidbot using its new access control restrictions and ability to authenticate with eSales.
			- All dutrow.bidbot.ejbclient.*IT tests have been updated to runAs with proper access
			- OrderMgmtIT calls OrderMgmt classes which in turn calls eSales
			-- testPlaceBid calls OrderMgmtEJB.placeBid(long auctionId, BidAccount bidder, float bidAmount)
			-- which calls OrderMgmtHelper.placeBid(auctionId, bidder, bidAmount) which runs as "esales-trusted"
			-- which then autheticates with eSales to place a bid by the specified bidder.
			- BidbotAccessControlIT shows failures when attempting to get access controlled resources without proper access
			
3.		DONE!