<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <title>Get User Auctions</title>
<body>
    <h2>Get User Auctions</h2>    
    
    <form method="GET" 
        action="<%=request.getContextPath()%>/model/user/handler">
        User id: <input type="text" name="id" size="25"/><p/>
        <input type="submit" name="command" value="Get User Auctions"/>
    </form>
    
</body>
</html>
