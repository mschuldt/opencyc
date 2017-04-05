/* $Id: SimpleCommandManagerThread.java,v 1.4 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.command;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;
import com.cyc.blue.gui.*;

/**
 * An implementation of IsCommandManager.
 *
 * @author John Jantos
 * @date 2002/03/06
 * @version $Id: SimpleCommandManagerThread.java,v 1.4 2002/05/23 22:30:14 jantos Exp $
 */

public class SimpleCommandManagerThread extends Thread implements IsCommandManager {
  private static final boolean DEBUG = false;
  private IsCommandable commandable;
  private IsCommandExecutor commandExecutor;

  private LinkedList commandQueue = new LinkedList();
  private LinkedList commandHistoryPast = new LinkedList();
  private LinkedList commandHistoryFuture = new LinkedList();

  public SimpleCommandManagerThread(IsCommandable _commandable, IsCommandExecutor _commandExecutor) {
    setCommandable(_commandable);
    setCommandExecutor(_commandExecutor);
    setName("SimpleCommandManagerThread for " + _commandable);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsCommandManager

  public void setCommandable(IsCommandable _commandable) { commandable = _commandable; }
  public IsCommandable getCommandable() { return commandable; }

  public void setCommandExecutor(IsCommandExecutor _commandExecutor) { commandExecutor = _commandExecutor; }
  public IsCommandExecutor getCommandExecutor() { return commandExecutor; }

  private Object execute(IsCommand _command) {
    if (DEBUG) { System.out.println("SimpleCommandManagerThread[" + this + "].execute(" + _command + ")"); }
    Object result = null;
    boolean commandToRemember = false;
    if (getCommandExecutor() != null) {
      result = getCommandExecutor().execute(_command);
    } else {
      // ???
    } 
    if (_command.isUndoable()) {
      commandHistoryPast.add(_command);
    }
    if (_command.isDestructive()) {
      // @todo: should verify destructive commands before even queueing
      commandHistoryFuture.clear();
    }
    return result;
  }
  
  private boolean unexecute(IsCommand _command) {
    if (DEBUG) { System.out.println("SimpleCommandManagerThread[" + this + "].unexecute(" + _command + ")"); }
    boolean result = false;
    if (getCommandExecutor() != null) {
      result = getCommandExecutor().unexecute(_command);
    } 
    return result;
  }

  private Object reexecute(IsCommand _command) {
    if (DEBUG) { System.out.println("SimpleCommandManagerThread[" + this + "].reexecute(" + _command + ")"); }
    Object result = null;
    if (getCommandExecutor() != null) {
      result = getCommandExecutor().reexecute(_command);
    } 
    if (_command.isUndoable()) {
      commandHistoryPast.add(_command);
    }
    if (_command.isDestructive()) {
      commandHistoryFuture.clear();
    }
    return result;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // command queues...

  public LinkedList getCommandQueue() {
    return commandQueue;
  }

  public void queueCommand(IsCommand _command) {
    if (DEBUG) { System.out.println("--> SimpleCommandManagerThread[" + this + "].queueCommand(" + _command + ")"); }
    synchronized (commandQueue) {
      getCommandQueue().add(_command);
    }
  }

  public int getCommandQueueSize() {
    int size;
    synchronized (commandQueue) {
      size = commandQueue.size();
    }
    return size;
  }

  public boolean canUndo() {
    if (commandHistoryPast.isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  public void undo() {
    IsCommand command = (IsCommand)commandHistoryPast.removeFirst();
    unexecute(command);
  }
  
  public boolean canRedo() {
    if (commandHistoryFuture.isEmpty()) {
      return false;
    } else {
      return true;
    }
  }
    
  public void redo() {
    IsCommand command = (IsCommand)commandHistoryFuture.removeFirst();
    reexecute(command);
  }

  
  ////////////////////////////////////////////////////////////////////////////////
  // Runnable interface

  protected boolean executeNextCommand() {
    if (DEBUG) { System.out.println("SimpleCommandManagerThread[" + this + "].executeNextCommand()"); }
    IsCommand command = null;
    synchronized (getCommandQueue()) {
      if (!getCommandQueue().isEmpty()) {
	command = (IsCommand)getCommandQueue().removeFirst();
      }
    }
    if (command != null) {
      Object executeResult = execute(command);
      return true;
    } else {
      return false;
    }
  }
  
  public void run() {
    if (DEBUG) { System.out.println("SimpleCommandManagerThread[" + this + "].run()"); }
    while (true) {
      if (!isPaused()) {
	boolean wasCommandExecuted = executeNextCommand();
	try { sleep(1000); } catch (InterruptedException e) {}
      } else {
	try { sleep(1000); } catch (InterruptedException e) {}
	//  	  synchronized (this) {
	//  	    try { wait(200); } catch (InterruptedException e) {}
	//  	    notify();
	//  	  }
      }
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsCommandProcessor

  public void processCommand(IsCommand _command) {
    if (DEBUG) { System.out.println("--> SimpleCommandManagerThread[" + this + "].processCommand(" + _command + ")"); }
    _command.process(this);
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // IsPausable

  private boolean isPaused = false;
  
  public void setPaused(boolean _isPaused) {
    isPaused = _isPaused; //synchronized (this) { notify(); }}
  }
  public boolean isPaused() { return isPaused; }


}
