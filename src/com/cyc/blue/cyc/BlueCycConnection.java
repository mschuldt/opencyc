/* $Id: BlueCycConnection.java,v 1.8 2002/05/23 22:35:04 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.cyc;

import java.io.InputStream;
import java.io.IOException;
import org.opencyc.api.CycAccess;
import org.opencyc.api.CycApiException;
import org.opencyc.api.CycConnection;
import org.opencyc.cycobject.CycSymbol;

/**
 * An extension of org.opencyc.CycConnection that adds a method to load a cfasl
 * file.  This functionality has not been needed for a long time and this class
 * could be deprecated.
 *
 * @author John Jantos
 * @date 2001/09/06
 * @version $Id: BlueCycConnection.java,v 1.8 2002/05/23 22:35:04 jantos Exp $
 */

public class BlueCycConnection extends CycConnection {
  public BlueCycAccess cycAccess;

  BlueCycConnection(String _hostName, int _port, int _communicationsMode, BlueCycAccess _cycAccess) throws IOException, CycApiException {
    super(_hostName, _port, _communicationsMode, _cycAccess);
  }

    /**
     * Receives an object from the CYC server.
     *
     * @return an array of two objects, the first is a Boolean response, and the second is the
     * response object or error string.
     */
    public Object[] receiveBinary () throws IOException, CycApiException {
        Object[] answer =  {
            null, null
        };
        Object status = cfaslInputStream.readObject();
        Object response = cfaslInputStream.readObject();
        if (status == null ||
            status.equals(new CycSymbol("NIL"))) {
            answer[0] = Boolean.FALSE;
            answer[1] = response;
            return answer;
        }
        answer[0] = Boolean.TRUE;
        answer[1] = cycAccess.completeObject(response);
        return  answer;
    }

  /**
   * Imports the cfasl codes in an InputStream directly to the server.
   *
   * @exception CycException  if the server signals that an error occurred
   * @exception IOException   if some network error occurs
   * @see CycAPI#cfaslImport
   */

  synchronized protected void cfaslImport(InputStream in) throws IOException, CycApiException {
    byte[] buffer = new byte[1024];

//      if (trace > API_TRACE_NONE)
//        System.out.println("Cfasl exporting from " + in);
    for (int len=in.read(buffer); len != -1; len=in.read(buffer))
      cfaslOutputStream.write(buffer, 0, len);
    cfaslOutputStream.flush();
//      if (trace > API_TRACE_NONE)
//        System.out.println("Finished exporting.");
    
    receiveBinary();
  }

}
