/* $Id: CycGraphCommandExecutor.java,v 1.5 2002/05/23 22:35:04 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.cyc;

import com.cyc.blue.command.GraphCommandExecutor;
import com.cyc.blue.command.IsCommand;
import com.cyc.blue.command.IsGraphCommandExecutor;
import com.cyc.blue.graph.IsGraph;
import org.opencyc.api.CycAccess;

/**
 * An implementation of IsGraphCommandExecutor that is tailored for graph of Cyc-related graph objects.
 *
 * @author John Jantos
 * @date 2001/10/04
 * @version $Id: CycGraphCommandExecutor.java,v 1.5 2002/05/23 22:35:04 jantos Exp $
 */

public class CycGraphCommandExecutor extends GraphCommandExecutor implements IsGraphCommandExecutor {
  
  CycAccessInitializer cycAccessInitializer;
  BlueCycAccess blueCycAccess;

  public CycGraphCommandExecutor(IsGraph _graph, CycAccessInitializer _cycAccessInitializer) {
    super(_graph);
    cycAccessInitializer = _cycAccessInitializer;
  }

  /** connects to Cyc, executes a command, then disconnects
   */
  public Object execute(IsCommand _command) {
    Object result;
    connect();
    result = super.execute(_command);
    disconnect();
    return result;
  }

  /** connects to Cyc, unexecutes a command, then disconnects
   */
  public boolean unexecute(IsCommand _command) {
    boolean result;
    connect();
    result = super.unexecute(_command);
    disconnect();
    return result;
  }

  /** connects to Cyc, reexecutes a command, then disconnects
   */
  public Object reexecute(IsCommand _command) {
    Object result;
    connect();
    result = super.reexecute(_command);
    disconnect();
    return result;
  }

  /** connects to a Cyc image.
   */
  protected void connect() {
    try {
      //System.err.println("before new BlueCycAccess");
      blueCycAccess = new BlueCycAccess(cycAccessInitializer);
      //System.err.println("after new BlueCycAccess");
    } catch (java.io.IOException e) {
      System.err.println("Blue: " + e);
      Thread.currentThread().dumpStack();
      //stop();//Runtime.getRuntime().exit(-1);
    } catch (org.opencyc.api.CycApiException e) {
      System.err.println("Blue: " + e);
      Thread.currentThread().dumpStack();
      //stop();//Runtime.getRuntime().exit(-1);
    }
  }
  
  /** disconnects from a cyc image
   */
  protected void disconnect() {
    if (blueCycAccess != null) {
      blueCycAccess.close();
    }
  }    

}
