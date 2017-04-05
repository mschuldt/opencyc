/* $Id: CommandExecutionState.java,v 1.4 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.command;

/**
 * An object to store the current execution state of a command.  Untested and
 * thus deprecated for now.
 *
 * @author John Jantos
 * @date 2001/09/19
 * @version $Id: CommandExecutionState.java,v 1.4 2002/05/23 22:30:14 jantos Exp $
 * @deprecated
 */

public class CommandExecutionState {
  private String name;
  public static final CommandExecutionState NEW = new CommandExecutionState("new");
  public static final CommandExecutionState EXECUTED = new CommandExecutionState("executed");
  public static final CommandExecutionState UNEXECUTED = new CommandExecutionState("unexecuted");
  public static final CommandExecutionState REEXECUTED = new CommandExecutionState("reexecuted");
  
  private CommandExecutionState(String _name) {
    name = _name;
  }

  public String toString() {
    return name;
  }
}
