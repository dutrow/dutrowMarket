package dutrow.sales.ejb;

import javax.ejb.Remote;

@Remote
public interface ParserRemote {
   void ingest() throws Exception;
}
