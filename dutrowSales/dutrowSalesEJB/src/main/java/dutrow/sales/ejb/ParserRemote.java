package dutrow.sales.ejb;

import java.io.IOException;

import javax.ejb.Remote;

@Remote
public interface ParserRemote {
   void ingest() throws IOException;
}
