package dutrow.sales.web;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;

import dutrow.sales.ejb.AccountMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemote;
import dutrow.sales.ejb.ParserRemote;
import dutrow.sales.ejb.SellerMgmtRemote;
import dutrow.sales.ejb.SupportRemote;

/**
 * This class is used to define the injection type for RegistrarRemote types
 */
public class WebConfig {
	@Produces
	@EJB
	public AccountMgmtRemote accountMgmt;

	@Produces
	@EJB
	public BuyerMgmtRemote buyerMgmt;

	@Produces
	@EJB
	public ParserRemote parser;

	@Produces
	@EJB
	public SellerMgmtRemote sellerMgmt;

	@Produces
	@EJB
	public SupportRemote support;

}