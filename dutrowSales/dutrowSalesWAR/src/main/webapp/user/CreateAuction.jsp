<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <title>Say Name</title>
<body>
    <h2>Say Name</h2>    
    
    <form method="GET" 
        action="<%=request.getContextPath()%>/model/user/handler">
        First Name: <input type="text" name="firstName" size="25"/><p/>   
        Last Name : <input type="text" name="lastName" size="25"/><p/>         
        <input type="submit" name="command" value="Say Name"/>
    </form>
</body>
</html>
