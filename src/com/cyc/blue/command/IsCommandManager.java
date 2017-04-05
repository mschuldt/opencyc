/* $Id: IsCommandManager.java,v 1.6 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.command;

import java.util.*;

/**
 * Implement to obtain the ability to queue commands for execution as well as
 * undo and redo executed commands.  The command manager is typically a thread
 * that manages a command queue.  Commands are executed by sending them to the
 * associated command executor.
 *
 * @author John Jantos
 * @date 2001/09/19
 * @version $Id: IsCommandManager.java,v 1.6 2002/05/23 22:30:14 jantos Exp $
 */

public interface IsCommandManager extends IsCommandProcessor {

  /** sets the object that this command manager manages commands for.
   * @param _commandable
   */
  public void setCommandable(IsCommandable _commandable);

  /** gets the object that this command manager manages commands for.
   * @return the commanded object.
   */
  public IsCommandable getCommandable();

  /** sets the command executor associated with this command manager.
   * @param _commandExecutor
   */
  public void setCommandExecutor(IsCommandExecutor _commandExecutor);

  /** gets the command executor associated with this command manager.
   * @return the command executor
   */
  public IsCommandExecutor getCommandExecutor();

  /** queues the command.
   * @param _command
   */
  public void queueCommand(IsCommand _command);

  /** gets the size of the current command queue
   * @return the command queue size
   */
  public int getCommandQueueSize();

  /** gets the undo status of the command manager.
   * @return <code>true</code> if the last command can be undone, <code>false</code> otherwise.
   */
  public boolean canUndo();

  /** undos (unexecutes) the last command executed.  If there are commands in
   * the command queue this call is ignored.
   */
  public void undo();

  /** gets the redo status of the command manager.
   * @return <code>true</code> if the last undone command can be redone, <code>false</code> otherwise.
   */
  public boolean canRedo();

  /** redos (reexecutes) the last command unexecuted.  If there are commands in
   * the command queue this call is ignored.
   */
  public void redo();  

  //????
  //public Collection addCommandType();
  //public Collection getCommandTypes();

}
