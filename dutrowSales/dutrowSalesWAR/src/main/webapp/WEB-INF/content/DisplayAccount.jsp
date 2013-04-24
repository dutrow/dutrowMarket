<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
            
<jsp:directive.page errorPage="/WEB-INF/content/ErrorPage.jsp"/>
<jsp:directive.page import="java.util.*"/>
<jsp:directive.page import="dutrow.sales.dto.*"/>
<html>
    <title>Account Display</title>
    <body>
        <h2>Account Display</h2>
        
        <jsp:scriptlet>
            Object o = request.getAttribute("account");
        </jsp:scriptlet>
        
        <ul>
            <jsp:scriptlet>
                if(o != null) {
                    AccountDTO a = (AccountDTO)o;
                    String userId = a.userId;
                    String email = a.email;
                    String url = "?id=" + userId + 
                        "&command=Get%20Account";
            </jsp:scriptlet>
            <li><a href="<%= url %>"><%= userId %>, &lt;<%= email %>&gt;</a></li>
            <li><%= a.firstName %> <%= a.middleName %> <%= a.lastName %></li>
            <li><%= a.email %></li>
            <jsp:scriptlet>
            if (a.addresses != null) for (AddressDTO d : a.addresses){
            </jsp:scriptlet>
            <li><%= d.name %>: <%= d.to %> <%= d.street %> <%= d.city %> <%= d.state %> <%= d.zip %> </li>
            <jsp:scriptlet>
            }
            </jsp:scriptlet>
            <jsp:scriptlet>
                }
            </jsp:scriptlet>                      
        </ul>
        
        <p/><a href="<%=request.getContextPath()%>/index.jsp">Go to Main Page</a>        
    </body>
</html>
            
