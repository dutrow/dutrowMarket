<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <title>Create Bid Account</title>
<body>
    <h2>Create Bid Account</h2>    
    
    <form method="GET" 
        action="<%=request.getContextPath()%>/model/admin/handler">
		userId: <input type="text" name="userId" size="25" value="user3"/><p/>   
        accountId : <input type="text" name="accountId" size="25" value="user1"/><p/>
        password : <input type="password" name="password" size="25" value="password"/><p/>
		<input type="submit" name="command" value="Create Account"/>
    </form>
</body>
</html>
