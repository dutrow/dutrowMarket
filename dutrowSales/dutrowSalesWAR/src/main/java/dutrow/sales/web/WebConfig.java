package dutrow.sales.web;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;

import dutrow.sales.ejb.AccountMgmtRemote;
import dutrow.sales.ejb.BuyerMgmtRemote;

/**
 * This class is used to define the injection type for RegistrarRemote
 * types
 */
public class WebConfig {
    @Produces 
    @EJB 
    public AccountMgmtRemote accountMgmt;
    
    @Produces 
    @EJB 
    public BuyerMgmtRemote buyerMgmt;
}