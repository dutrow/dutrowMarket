<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <title>Create Account</title>
<body>
    <h2>Create Account</h2>    
    
    <form method="GET" 
        action="<%=request.getContextPath()%>/model/anon/handler">
        User ID: <input type="text" name="id" size="25" value="seller"/><p/>
        First Name: <input type="text" name="first" size="25" value="Seller"/><p/>
        Middle Name: <input type="text" name="middle" size="25" value="B"/><p/>
        Last Name: <input type="text" name="last" size="25" value="Good"/><p/>
        E-mail: <input type="text" name="email" size="25" value="seller@jhuapl.edu"/><p/>   
        <input type="submit" name="command" value="Create Account"/>
    </form>
</body>
</html>