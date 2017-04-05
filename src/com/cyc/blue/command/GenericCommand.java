/* $Id: GenericCommand.java,v 1.3 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.command;

import java.util.*;
import java.lang.reflect.*;

/**
 * A basic implementation of the IsCommand interface that provides a functional
 * "null" command.  Subclass to inherit the basic functionalities of a command.
 *
 * @author John Jantos
 * @date 2001/09/19
 * @version $Id: GenericCommand.java,v 1.3 2002/05/23 22:30:14 jantos Exp $
 */

public class GenericCommand extends InternalMethodDispatcher implements IsCommand {
  private static final boolean DEFAULT_UNDOABLE = true;
  private static final boolean DEFAULT_DESTRUCTIVE = false;
  
  private IsCommand superCommand = null;
  private List subCommands = new LinkedList();

  /** processes a command with respect to the specified command processor.  this
   * method does internal method dispatching and calls an appropriate method
   * named "processInternal" that accepts the specified command processor as an
   * argument.  If there are no appropriate "processInternal" methods, nothing
   * is done.
   */
  public void process(IsCommandProcessor _commandProcessor) {
    dispatchByNameAndArg("processInternal", _commandProcessor);
  }

  public List getSubCommands() {
    return subCommands;
  }
  
  public void addSubCommand(IsCommand _command) {
    if (!getSubCommands().contains(_command)) {
      getSubCommands().add(_command);
      _command.setSuperCommand(this);
    }
  }
  public Iterator getSubCommandsIterator() {
    return getSubCommands().iterator();
  }

  public void setSuperCommand(IsCommand _command) {
    superCommand = _command;
  }
  public IsCommand getSuperCommand() {
    return superCommand;
  }

  public IsCommand getRootCommand() {
    IsCommand upCommand = this;
    while (upCommand.getSuperCommand() != null) {
      upCommand = upCommand.getSuperCommand();
    }
    return upCommand;
  }
  
  public Object execute(IsCommandExecutor _commandExecutor) {
    Iterator subCommandsIterator = getSubCommandsIterator();
    if (subCommandsIterator != null) {
      while (subCommandsIterator.hasNext()) {
	IsCommand subCommand = (IsCommand)subCommandsIterator.next();
	subCommand.execute(_commandExecutor);
      }
    } else {
    }
    return null;
  }    

  public boolean unexecute(IsCommandExecutor _commandExecutor) {
    boolean isUnexecuted = true;
    Iterator subCommandsIterator = getSubCommandsIterator();
    while (subCommandsIterator.hasNext()) {
      IsCommand subCommand = (IsCommand)subCommandsIterator.next();
      if (!subCommand.unexecute(_commandExecutor)) {
	isUnexecuted = false;
      }
    }
    return isUnexecuted;
  }

  public Object reexecute(IsCommandExecutor _commandExecutor) {
    return execute(_commandExecutor);
  }

  CommandExecutionState commandExecutionState = CommandExecutionState.NEW;

  public CommandExecutionState getExecutionState() {
    return commandExecutionState;
  }

  public boolean isUndoable() {
    return DEFAULT_UNDOABLE;
  }
  public boolean isDestructive() {
    return DEFAULT_DESTRUCTIVE;
  }
  
}
