<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">
            
<jsp:directive.page errorPage="/WEB-INF/content/ErrorPage.jsp"/>
<jsp:directive.page import="java.util.*"/>
<jsp:directive.page import="dutrow.bidbot.bo.*"/>
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
                    BidAccount a = (BidAccount)o;
                    String uid = a.getUserId();
                    String acct = a.getSalesAccount();
                    String passwd = a.getSalesPassword();
            </jsp:scriptlet>
            <li>User Id: <%= uid %></li>
            <li>Account Id: <%= acct %></li>
            <li>Password: <%= passwd %></li>
            <jsp:scriptlet>
                }
            </jsp:scriptlet>                      
        </ul>
        
        <p/><a href="<%=request.getContextPath()%>/index.jsp">Go to Main Page</a>        
    </body>
</html>
            
