/**
 * 
 */
package dutrow.bidbot.web;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dutrow.bidbot.ejb.BidbotUtilRemote;
import dutrow.bidbot.ejb.OrderMgmtRemote;

public abstract class Handler {

	public abstract void handle(HttpServletRequest request,
			HttpServletResponse response, ServletContext context,
			OrderMgmtRemote orderMgmt, BidbotUtilRemote support) throws ServletException, IOException;
}