<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <title>Place Bid</title>
<body>
    <h2>Place Bid</h2>    
    
    <form method="GET" 
        action="<%=request.getContextPath()%>/model/user/handler">
        userId: <input type="text" name="userId" size="25" value="bidbotboy"/><p/>   
        auctionId : <input type="text" name="auctionId" size="25" value="1"/><p/>
        startBid : <input type="text" name="start" size="25" value="1.00"/><p/>
        maxBid : <input type="text" name="max" size="25" value="1.00"/><p/>
        
                 
        <input type="submit" name="command" value="Place Order"/>
    </form>
</body>
</html>
