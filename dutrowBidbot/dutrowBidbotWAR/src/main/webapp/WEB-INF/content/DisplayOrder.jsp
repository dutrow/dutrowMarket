<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">

<jsp:directive.page errorPage="/WEB-INF/content/ErrorPage.jsp" />
<jsp:directive.page import="java.util.*" />
<jsp:directive.page import="dutrow.bidbot.bo.*" />
<html>
<title>Auction Display</title>
<body>
	<h2>Auction Display</h2>

	<jsp:scriptlet>Object o = request.getAttribute("order");</jsp:scriptlet>

	<ul>
		<jsp:scriptlet>if (o != null) {
				BidOrder a = (BidOrder) o;
				long aid = a.getAuctionId();
				String bid = a.getBidder().getUserId();
				long bo = a.getBidOrderId();
				float sb = a.getStartBid();
				float mb = a.getMaxBid();
				float fb = a.getFinalBid();
				
		</jsp:scriptlet>
		<li>auction id <%=aid%> </a></li>
		<li>bidder id <%=bid%></li>
		<li>bid order <%=bo%></li>
		<li>startBid <%=sb%></li>
		<li>maxBid <%=mb%></li>
		<li>finalBid <%=fb%></li>
		
	</ul>
	<p />
	
	<a href="<%=request.getContextPath()%>/index.jsp">Go to Main Page</a>
</body>
</html>

