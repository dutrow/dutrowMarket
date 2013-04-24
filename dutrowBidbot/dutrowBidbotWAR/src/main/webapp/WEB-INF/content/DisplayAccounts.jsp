<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
            
<jsp:directive.page errorPage="/WEB-INF/content/ErrorPage.jsp"/>
<jsp:directive.page import="java.util.*"/>
<jsp:directive.page import="dutrow.sales.dto.*"/>
<html>
    <title>Accounts Display</title>
    <body>
        <h2>Accounts Display</h2>
        
        <jsp:scriptlet>
            Collection accounts = (Collection)request.getAttribute("accounts");
            int index = ((Integer)request.getAttribute("index")).intValue();
            int count = ((Integer)request.getAttribute("count")).intValue();
            int nextIndex = ((Integer)request.getAttribute("nextIndex")).intValue();
        </jsp:scriptlet>
        
        <ul>
            <jsp:scriptlet>
                for(Object o: accounts) {
                    AccountDTO a = (AccountDTO)o;
                    String userId = a.userId;
                    String email = a.email;
                    String url = "?id=" + userId + 
                        "&command=Get%20Account";
            </jsp:scriptlet>
            <li><a href="<%= url %>"><%= userId %>, &lt;<%= email %>&gt;</a></li>
            <jsp:scriptlet>
                }
            </jsp:scriptlet>   
            <li>...</li>         
        </ul>
        
        <form method="GET" 
            action="<%=request.getContextPath()%>/model/admin/handler">
            Index: <%= index %><p/>
            Count: <%= count %><p/>
            <input type="hidden" name="index" value="<%= nextIndex %>"/>
            <input type="hidden" name="count" value="<%= count %>"/>
            <input type="submit" name="command" value="Get Accounts"/>
        </form>
        
        <p/><a href="<%=request.getContextPath()%>/index.jsp">Go to Main Page</a>        
    </body>
</html>
            
