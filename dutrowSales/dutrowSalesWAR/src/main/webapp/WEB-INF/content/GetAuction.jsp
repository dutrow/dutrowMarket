<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
            
<jsp:directive.page errorPage="/WEB-INF/content/ErrorPage.jsp"/>
<jsp:directive.page import="dutrow.sales.bo.*"/>
<html>
    <title>Auction Display</title>
    <body>
        <h2>Auction Display</h2>
        
        <jsp:scriptlet>
            AuctionItem auction = (AuctionItem)request.getAttribute("auction");
            long id = auction.getId();
            String title = auction.getTitle();
            String details = auction.toString();
        </jsp:scriptlet>
        
        Id: <%= id %><p/>
        Title: <%= title %><p/>
        Details: <%= details %><p/>
        
        
        <form method="GET" 
            action="<%=request.getContextPath()%>/model/anon/handler">
            Amount $: <input type="text" name="amount" size="25"/><p/>   
            <input type="hidden" name="accountNumber" value="<%= acctNum %>"/>
            <input type="submit" name="command" value="Deposit"/>
            <input type="submit" name="command" value="Withdraw"/>
            <input type="submit" name="command" value="Close Account"/>
        </form>
        
        <p/><a href="<%=request.getContextPath()%>/index.jsp">Go to Main Page</a>        
    </body>
</html>
            
