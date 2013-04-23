<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <title>Dutrow Sales</title>
<body>
    <h2>Dutrow Sales Main Menu</h2>
    
    <!-- 
    Anonymous User
	List open auctions
	Get details for specific auction.
	createAccount (using AccountMgmtEJB)
	Test Admin
	reset All tables
	populate tables (using Ingestor)
	Seller
	createAuction (using SellerMgmtEJB)
	getUserAuctions (using SellerMgmtEJB)
	getAuction (using SellerMgmtEJB)
	Buyer
	Place bid (using BuyerMgmtEJB)
    
     -->
    
    <h2>Anonymous User</h2>
    <ul>
    	<li><a href="anon/ListOpenAuctions.jsp">List Open Auctions</a></li>
    	<li><a href="anon/GetAuction.jsp">Get Auction</a></li>
    	<li><a href="anon/CreateAccount.jsp">Create Account</a></li>
    </ul>
    
    <h2>Test Admin</h2>
    <ul>
    	<li><a href="admin/ResetAll.jsp">Reset All Tables</a></li>
    	<li><a href="admin/PopulateAll.jsp">Populate Tables</a></li>
    </ul>
    
    <h2>Seller</h2>
    <ul>
    	<li><a href="user/CreateAuction.jsp">Create Auction</a></li>
    	<li><a href="user/GetUserAuctions.jsp">Get User Auctions</a></li>
    	<li><a href="user/GetAuction.jsp">Get Auction Details</a></li>
    </ul>
    
    <h2>Buyer</h2>
    <ul>
    	<li><a href="user/PlaceBid.jsp">Place Bid</a></li>
    </ul>
    
    
</body>
</html>