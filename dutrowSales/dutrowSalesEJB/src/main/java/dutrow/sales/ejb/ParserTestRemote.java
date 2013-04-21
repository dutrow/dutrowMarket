package dutrow.sales.ejb;

import javax.ejb.Remote;

@Remote
public interface ParserTestRemote {
   void ingest() throws Exception;
}
