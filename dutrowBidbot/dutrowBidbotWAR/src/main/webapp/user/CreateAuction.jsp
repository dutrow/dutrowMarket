<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <title>Create Auction</title>
<body>
    <h2>Create Auction</h2>    
    
    <form method="GET" 
        action="<%=request.getContextPath()%>/model/user/handler">
        Title: <input type="text" name="title" size="25" value="Course notes"/><p/>   
        Category:
        <select name="category">
        <option>Alcohol & Tobacco</option>
        <option>Audio & Video</option>
        <option>Fresh Foods</option>
        <option>Health & Beauty</option>
        <option>Home & Kitchen</option>
        <option>Office Products</option>
        <option selected="true">PC Computers</option>
        <option>Science & Toys</option>
        </select>
        <p/>
        Description: <input type="text" name="description" size="75" value="several pounds of course notes and over 16,000 online yahoo forum messages"/><p/>
        Start Time: <input type="text" name="startTime" size="25" value="2001-02-12 00:00:00.0"/><p/>
        End Time: <input type="text" name="endTime" size="25" value="2050-01-01 00:00:00.0"/><p/>
        seller: <input type="text" name="seller" size="25" value="seller"/><p/>
        asking price: <input type="number" name="price" size="25" value="1"/><p/>
        open: <input type="checkbox" name="open" value="true" checked="checked"/><p/>
        
        <input type="submit" name="command" value="Create Auction"/>
    </form>
</body>
</html>
