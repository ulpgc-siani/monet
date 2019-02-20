import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import util.LibraryBase64;
import util.StreamHelper;


public class Main {

  /**
   * @param args
   * @throws IOException 
   * @throws NoSuchAlgorithmException 
   */
  public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
    AgentServiceClient client = AgentServiceClient.getInstance();
    
    if(args.length != 5) {
      System.out.println("Usage: [pdfFilename] [serviceUrl] [privateKeyFilename] [privateKeyPassword] [callbackUrl]");
      return;
    }
    
    String sourceFilename = args[0];
    
    FileInputStream documentStream = new FileInputStream(sourceFilename);
    String requestDocumentHash = new String(LibraryBase64.encode(StreamHelper.calculateHash(documentStream)));
    StreamHelper.close(documentStream);
    documentStream = new FileInputStream(sourceFilename);
    
    client.callService(args[1], 
                       documentStream, 
                       requestDocumentHash, 
                       args[2], args[3],
                       args[4]);
  }

}
