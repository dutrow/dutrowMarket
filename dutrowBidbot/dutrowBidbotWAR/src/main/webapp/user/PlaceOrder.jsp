<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <title>Place Bid</title>
<body>
    <h2>Place Bid</h2>    
    
    <form method="GET" 
        action="<%=request.getContextPath()%>/model/user/handler">
        userId: <input type="text" name="userId" size="25" value="1"/><p/>   
        accountId : <input type="text" name="account" size="25" value="buyer1"/><p/>
        password : <input type="number" name="password" size="25" value="2.0"/><p/>         
        <input type="submit" name="command" value="Create Account"/>
    </form>
</body>
</html>
