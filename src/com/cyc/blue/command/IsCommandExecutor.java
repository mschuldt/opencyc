/* $Id: IsCommandExecutor.java,v 1.4 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.command;

/**
 * Implement to obtain the ability to execute commands.  Command executors work
 * closely with commands by essentially creating an abstraction layer over the
 * commands they execute.  Methods that are shared between multiple commands'
 * execution methods should be put into the command executor.  Also, with this
 * scheme the command executor can also enforce invariants that the commands do
 * not need to be aware of.
 *
 * @author John Jantos
 * @date 2001/09/19
 * @version $Id: IsCommandExecutor.java,v 1.4 2002/05/23 22:30:14 jantos Exp $
 */

public interface IsCommandExecutor {

  /** executes the command.  this is typically called by a command manager and
   * this method at the very least will call the execute method of the command
   * argument.
   * @param _command
   */
  public Object execute(IsCommand _command);

  /** unexecutes the command.  this is typically called by a command manager and
   * this method at the very least will call the unexecute method of the command
   * argument.
   * @param _command
   */
  public boolean unexecute(IsCommand _command);

  /** reexecutes the command.  this is typically called by a command manager and
   * this method at the very least will call the reexecute method of the command
   * argument.
   * @param _command
   */
  public Object reexecute(IsCommand _command);
}
