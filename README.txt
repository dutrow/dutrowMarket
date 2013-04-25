Dan Dutrow
Enterprise Java
Spring 2013

Project 2

Technical Details:

1.		Continue to provide all functionality from Project 1. [dutrowMarket/dutrowSales/dutrowSalesImpl and dutrowMarket/dutrowBidbot/dutrowBidbotImpl]
2.		Create an EJB tier to host your eSales business logic and data access tiers. [dutrowMarket/dutrowSales/dutrowSalesEJB]
2.A.	EJBs: [dutrow.sales.ejb/cdi :: AccountMgmtEJB, BuyerMgmtEJB, SellerMgmtEJB, ParserEJB (for ESalesParser injest()), SupportEJB (for TestSupport)]
2.B.	DTOs: [dutrow.sales.dto :: as prescribed. Added a DTOConversionUtil static utility class for converting between DTOs and BOs]
2.C.	EAR:  [dutrow.sales.ear :: dutrowMarket/dutrowSales/dutrowEAR]
2.D.	Test: [dutrow.sales.ejbclient :: dutrowMarket/dutrowSales/dutrowTest]
3.		eBidbot Business Logic :: [dutrowMarket/dutrowBidbot/dutrowBidbotWAR -- implemented as a single WAR module]
3.A.	OrderMgmt/OrderMgmtImpl :: [dutrowBidbotWAR/dutrow.bidbot.bl.OrderMgmt]/[dutrowBidbotWAR/dutrow.bidbot.blimpl.OrderMgmtImpl]
4.		Create an EJB tier to host your eBidbot business logic and data access tiers. 
4.A.	WAR:  [dutrowBidbotWAR/dutrow.bidbot.web]
4.B.	EJBs: [dutrowBidbotWAR/dutrow.bidbot.cdi] [dutrowBidbotWAR/dutrow.bidbot.ejb]
4.B.a.	OrderMgmtEJB: [dutrowBidbotWAR/dutrow.bidbot.ejb.OrderMgmtEJB]
4.C.	DTOs: [used business objects as suggested by the instructor]
5.		Add a Web UI to the eSales application. [Uses Remote interfaces, controller is dutrow.sales.web.MgmtServlet]
			Anonymous User
				List open auctions [dutrow.sales.web.ListOpenAuctions.java, main/webapp/anon/ListOpenAuctions.jsp, main/WEB-INF/content/DisplayAuctions.jsp]
				Get details for specific auction. [dutrow.sales.web.GetAuction.java, main/webapp/anon/GetAuction.jsp, main/WEB-INF/content/DisplayAuction.jsp]
				createAccount (using AccountMgmtEJB) [dutrow.sales.web.CreateAccount.java, main/webapp/anon/CreateAccount.jsp, main/WEB-INF/content/DisplayAccount.jsp]
			Test Admin
				reset All tables [dutrow.sales.web.ResetAll.java, main/webapp/admin/ResetAll.jsp]
				populate tables (using Ingestor) [dutrow.sales.web.Populate.java, main/webapp/admin/PopulateAll.jsp]
			Seller
				createAuction (using SellerMgmtEJB) [dutrow.sales.web.CreateAuction.java, main/webapp/user/CreateAuction.jsp]
				getUserAuctions (using SellerMgmtEJB) [dutrow.sales.web.GetUserAuctions.java, main/webapp/user/GetUserAuctions.jsp, main/WEB-INF/content/DisplayAuctions.jsp]
				getAuction (using SellerMgmtEJB) [dutrow.sales.web.GetAuction.java, main/webapp/user/GetAuction.jsp, main/WEB-INF/content/DisplayAuction.jsp]
			Buyer
				Place bid (using BuyerMgmtEJB) [dutrow.sales.web.PlaceBid.java, main/webapp/user/PlaceBid.jsp, main/WEB-INF/content/DisplayAuction.jsp]
6.		Add a WebUI to the eBidbot application [http://localhost:8080/dutrowBidbotWAR]
			Admin
				createAccount (using OrderMgmtEJB) [http://localhost:8080/dutrowBidbotWAR/admin/CreateBidAccount.jsp]
			Bidder
				placeOrder (using OrderMgmtEJB) [http://localhost:8080/dutrowBidbotWAR/user/PlaceOrder.jsp]
7.		Add transaction properties to the EJBs.
			Transaction Scope
				[@Stateless @TransactionAttribute(TransactionAttributeType.REQUIRED) public class AccountMgmtEJB]
				[@Stateless @TransactionAttribute(TransactionAttributeType.REQUIRED) public class BuyerMgmtEJB]
				[@Stateless @TransactionAttribute(TransactionAttributeType.REQUIRED) public class ParserEJB]
				[@Stateless @TransactionAttribute(TransactionAttributeType.REQUIRED) public class SellerMgmtEJB]
				[@Stateless @TransactionAttribute(TransactionAttributeType.REQUIRED) public class SupportEJB]
			Transaction Integrity [BuyerMgmtEJB::public BidResultDTO placeBid ... throws EJBException]
			Demonstration of Rollback
				Create a demonstration of transactions and the capability of rollback by implementing a scenario that adds something to the database and then a *follow-on* rollback causes the changes to be undone.
					[dutrowSalesTest/src/test/java/dutrow.sales.ejbclient.TransactionDemonstrationIT.java][@Test public void transactionDemonstration()]
					[Multiple bids placed in BuyerSupportEJB::placeMultiBid()]
					[third bid invalid causes whole transaction to roll back]
					[EJBException thrown in BuyerMgmtEJB::placeBid, caught in transactionDemonstration]
					[Assert requiring evidence of exception + Asssert requiring no persisted data]
				
Testing:
		The eSales application is configured to deploy on Jboss localhost:8080 at the /dutrowSales context. http://localhost:8080/dutrowSales

1.	Provide JUnit tests that verify the EJB functionality of eSales accessed through its remote interface.
		[dutrowSalesTest/src/test/java/dutrow.sales.ejbclient.AccountMgmtIT]
		[dutrowSalesTest/src/test/java/dutrow.sales.ejbclient.BuyerMgmtIT]
		[dutrowSalesTest/src/test/java/dutrow.sales.ejbclient.ParserServerIT]
		[dutrowSalesTest/src/test/java/dutrow.sales.ejbclient.SellerMgmtIT]
		[dutrowSalesTest/src/test/java/dutrow.sales.ejbclient.Support -- used in setUp for every other class]
2.	Provide JUnit tests that verify the extra business logic functionality of eBidbot interfacing with eSales.
		[INCOMPLETE]
3.	Provide JUnit tests that verify the EJB functionality of eBidbot accessed through its remote interface.
		[INCOMPLETE]
4.	Perform an end-to-end use case to do the following. 
		This must be demonstrated in an automated JUnit test 
			[dutrowSalesTest/src/test/java/dutrow.sales.ejbclient.EndToEndIT]
		Then be manually implementable using the Web UI.
				


Notes:
	TestSupport is itself tested through the other *IT tests.
	There was no explanation what the listMy* methods were supposed to do, so I made the "me" a userId parameter
	I couldn't think of why we would want a transaction other than REQUIRED, so	all transactions are @TransactionAttribute(TransactionAttributeType.REQUIRED)
	All of my DTOs have public attributes and no getters/setters. I know this is non-standard in Java, 
		but it reduces code bloat for what are just simple structs.
		