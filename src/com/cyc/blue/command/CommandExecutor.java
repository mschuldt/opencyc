/* $Id: CommandExecutor.java,v 1.4 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.command;

import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * A basic implementation of IsCommandExecutor.
 *
 * @author John Jantos
 * @date 2001/10/18
 * @version $Id: CommandExecutor.java,v 1.4 2002/05/23 22:30:14 jantos Exp $
 */

public class CommandExecutor implements IsCommandExecutor {
  private static final boolean DEBUG = false;

  ////////////////////////////////////////////////////////////////////////////////
  // IsCommandExecutor interface

  public Object execute(IsCommand _command) {
    if (DEBUG) System.err.println("CommandExecutor.execute(" + _command + ")");
    return _command.execute(this);
  }

  public boolean unexecute(IsCommand _command) {
    if (DEBUG) System.err.println("CommandExecutor.unexecute(" + _command + ")");
    return _command.unexecute(this);
  }

  public Object reexecute(IsCommand _command) {
    if (DEBUG) System.err.println("CommandExecutor.reexecute(" + _command + ")");
    return _command.reexecute(this);
  }
  
}



