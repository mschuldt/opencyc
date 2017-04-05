/* $Id: IsCommand.java,v 1.6 2002/05/23 22:30:14 jantos Exp $
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
 * Implement to obtain the ability to be used as a command.  A command is an
 * object that is typically processed then queued to a command manager, where it
 * is then sent to a command executor for execution.  A command can support
 * execute, unexecute, and reexecute methods, the latter two of which enable
 * undo/redo.  A command can be part of a command tree, with a singlre super
 * command and/or multiple sub commands.  A command can be undoable, in which
 * case unexecute and reexcute must be implemented, or destructive in which case
 * any previous commands run can no longer be undone.
 *
 * @author John Jantos
 * @date 2001/09/19
 * @version $Id: IsCommand.java,v 1.6 2002/05/23 22:30:14 jantos Exp $
 */

public interface IsCommand {

  /** processes a command with respect to the specified command processor.
   * typically this method sets up internal state then either adds the command
   * to a command manager or passes it to another command processor
   * @param _commandProcessor
   */
  public void process(IsCommandProcessor _commandProcessor);

  /** executes this command possibly calling methods from the specified command
   * executor.
   * @param _commandExecutor
   */
  public Object execute(IsCommandExecutor _commandExecutor);

  /** unexecutes this command possibly calling methods from the specified
   * command executor.
   * @param _commandExecutor
   */
  public boolean unexecute(IsCommandExecutor _commandExecutor);

  /** reexecutes this command possibly calling methods from the specified
   * command executor.
   * @param _commandExecutor
   */
  public Object reexecute(IsCommandExecutor _commandExecutor);

  /** adds a sub-command to this command.
   * @param _subCommand
   */
  public void addSubCommand(IsCommand _subCommand);

  /** gets a list of the sub-commands of this command in order of when added
   * (most recently added commands are iterated over last).
   * @return a list of the sub-commands.
   */
  public List getSubCommands();

  /** gets an iterator over the sub-commands of this command in order of when
   * added (most recently added commands are iterated over last).
   * @return an iterator over the sub-commands.
   */
  public Iterator getSubCommandsIterator();

  /** sets the super-command of this command.
   * @param _superCommand
   */
  public void setSuperCommand(IsCommand _superCommand);

  /** gets the super-command of this command.
   * @return the super command.
   */
  public IsCommand getSuperCommand();

  /** gets the root command of the command tree this command is part of.
   * essentially traverses up the super commands until it find the most-super
   * command.
   * @return the root command
   */
  public IsCommand getRootCommand();

  /** gets the current execution state.  this can die if not useful.
   * @deprecated
   */
  public CommandExecutionState getExecutionState();

  public boolean isUndoable();
  public boolean isDestructive();
}

