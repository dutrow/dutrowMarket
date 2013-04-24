<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <title>Place Bid</title>
<body>
    <h2>Place Bid</h2>    
    
    <form method="GET" 
        action="<%=request.getContextPath()%>/model/user/handler">
        auction: <input type="text" name="auction" size="25" value="1"/><p/>   
        bidder : <input type="text" name="bidder" size="25" value="buyer1"/><p/>
        amount : <input type="number" name="amount" size="25" value="2.0"/><p/>         
        <input type="submit" name="command" value="Place Bid"/>
    </form>
</body>
</html>
