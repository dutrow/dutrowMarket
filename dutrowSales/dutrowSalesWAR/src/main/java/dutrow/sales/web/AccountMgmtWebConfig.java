package dutrow.sales.web;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;

import dutrow.sales.ejb.AccountMgmtRemote;

/**
 * This class is used to define the injection type for RegistrarRemote
 * types
 */
public class AccountMgmtWebConfig {
    @Produces 
    @EJB 
    public AccountMgmtRemote registrar;
}