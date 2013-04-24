<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
            "http://www.w3.org/TR/html4/strict.dtd">

<jsp:directive.page errorPage="/WEB-INF/content/ErrorPage.jsp" />
<jsp:directive.page import="java.util.*" />
<jsp:directive.page import="dutrow.sales.dto.*" />
<html>
<title>Auction Display</title>
<body>
	<h2>Auction Display</h2>

	<jsp:scriptlet>Object o = request.getAttribute("auction");</jsp:scriptlet>

	<ul>
		<jsp:scriptlet>if (o != null) {
				AuctionDTO a = (AuctionDTO) o;
				String url = "?id=" + a.id + "&command=Get%20Auction";
		</jsp:scriptlet>
		<li>title <%=a.title%> <a href="<%=url%>">[<%=a.id%>]</a></li>
		<li>category <%=a.category%></li>
		<li>description <%=a.description%></li>
		<li>startTime <%=a.startTime%></li>
		<li>endTime <%=a.endTime%></li>
		<li>seller <%=a.seller%></li>
		<li>seller_email <%=a.seller_email%></li>
		<li>buyer <%=a.buyer%></li>
		<li>buyer_email <%=a.buyer_email%></li>
		<li>askingPrice <%=a.askingPrice%></li>
		<li>purchasePrice <%=a.purchasePrice%></li>
		<li>isOpen <%=a.isOpen%></li>
		<jsp:scriptlet>
		if (a.shipTo != null) {
		</jsp:scriptlet>
		<li>shipTo <%=a.shipTo.to%> <%=a.shipTo.name%> <%=a.shipTo.street%>
			<%=a.shipTo.city%> <%=a.shipTo.state%> <%=a.shipTo.zip%></li>
		<jsp:scriptlet>
		}
		if (a.images != null) {
		</jsp:scriptlet>
			<li>#images : <%=a.images.size()%></li>
			<jsp:scriptlet>for (ImageDTO image : a.images) {</jsp:scriptlet>
			<li>image <img src="data:image/gif;base64,<%=image%>" /></li>
		<jsp:scriptlet>
			}
		}
		</jsp:scriptlet>
		<li>isExpired <%=a.isExpired%></li>
	</ul>
	<h3>Bids:</h3>
	<ul>
		<jsp:scriptlet>
				if (a.bids != null) {
					for (BidDTO bid : a.bids) {
		</jsp:scriptlet>
						<li>bid: <%=bid.amount%></li>
		<jsp:scriptlet>
					}
				}
		</jsp:scriptlet>
	</ul>
	<jsp:scriptlet>
		}			
	</jsp:scriptlet>

	<p />
	
	<a href="<%=request.getContextPath()%>/index.jsp">Go to Main Page</a>
</body>
</html>

