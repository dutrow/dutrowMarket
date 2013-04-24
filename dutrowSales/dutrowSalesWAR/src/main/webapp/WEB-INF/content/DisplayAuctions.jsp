<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">

<jsp:directive.page errorPage="/WEB-INF/content/ErrorPage.jsp" />
<jsp:directive.page import="java.util.*" />
<jsp:directive.page import="dutrow.sales.dto.*" />
<html>
<title>Auctions Display</title>
<body>
	<h2>Auctions Display</h2>

	<jsp:scriptlet>Collection auctions = (Collection) request.getAttribute("auctions");</jsp:scriptlet>

	<ul>
		<jsp:scriptlet>for (Object o : auctions) {
				AuctionDTO a = (AuctionDTO) o;

				String url = "?id=" + a.id + "&command=Get%20Auction";</jsp:scriptlet>
		<li><%=a.title%><a href="<%=url%>">[<%=a.id%>]
		</a></li>
		<jsp:scriptlet>}</jsp:scriptlet>
	</ul>
	<p />
	<a href="<%=request.getContextPath()%>/index.jsp">Go to Main Page</a>
</body>
</html>

