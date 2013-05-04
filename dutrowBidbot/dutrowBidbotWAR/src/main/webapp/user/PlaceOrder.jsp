<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <title>Place Bid Order</title>
<body>
    <h2>Place Bid Order</h2>    
    
    <form method="GET" 
        action="<%=request.getContextPath()%>/model/user/handler">   
        auctionId : <input type="text" name="auctionId" size="25" value="1"/><p/>
        startBid : <input type="text" name="start" size="25" value="1.00"/><p/>
        maxBid : <input type="text" name="max" size="25" value="1.00"/><p/>
                 
        <input type="submit" name="command" value="Place Order"/>
    </form>
</body>
</html>
