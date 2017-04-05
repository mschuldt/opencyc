/* $Id: BlueCycEventListener.java,v 1.10 2002/05/23 22:35:04 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.cyc;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import com.cyc.blue.IsPausable;
import com.cyc.blue.command.IsCommand;
import com.cyc.blue.command.IsCommandProcessor;
import org.opencyc.api.CycAccess;
import org.opencyc.api.CycApiException;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycSymbol;

/**
 * A thread that polls a CycAccess object for command lists, creates the
 * command, then intitiates processing.
 *
 * @author John Jantos
 * @date 2001/10/14
 * @version $Id: BlueCycEventListener.java,v 1.10 2002/05/23 22:35:04 jantos Exp $
 */

public class BlueCycEventListener extends Thread implements IsPausable {
  private static final boolean DEBUG = false;
  private static final CycSymbol BLUE_FETCH_UIA_BLUE_EVENT = new CycSymbol("BLUE-FETCH-UIA-BLUE-EVENT");

  private IsCommandProcessor commandProcessor;
  private String signature;
  private CycAccessInitializer cycAccessInitializer;

  private BlueCycAccess blueCycAccess = null;

  public BlueCycEventListener(IsCommandProcessor _commandProcessor,
			      String _signature,
			      CycAccessInitializer _cycAccessInitializer) {
    commandProcessor = _commandProcessor;
    signature = _signature;
    cycAccessInitializer = _cycAccessInitializer;
    setName("BlueCycEventListener for " + signature + " procesed by " + commandProcessor);
  }
    
  protected void finalize() throws Throwable { // throws Throwable because java.lang.Thread might? (Thread version 1.125 01/12/03 does not)
    super.finalize();
    disconnect();
  }

  public void disconnect() {
    if (DEBUG) { System.out.println("BlueCycEventListener[" + this + "].disconnect()"); }
    if (blueCycAccess != null) {
      blueCycAccess.close();
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Runnable interface

  public void run() {
    if (DEBUG) { System.out.println("BlueCycEventListener[" + this + "].run()"); }
    try {
      blueCycAccess = new BlueCycAccess(cycAccessInitializer);
      if (DEBUG) { System.out.println("BlueCycEventListener.run(): using " + blueCycAccess); }
      while (true) {
	if (!isPaused()) {
	  CycList commandArgList = new CycList();
	  commandArgList.add(new CycSymbol("QUOTE"));
	  commandArgList.add(signature);
	  CycList commandList = new CycList();
	  commandList.add(BLUE_FETCH_UIA_BLUE_EVENT);
	  commandList.add(commandArgList);
	  CycList blueEvent = null;
	  if (DEBUG) { System.out.println("commandList = " + commandList); }
	  try {
	    blueEvent = BlueCycAccess.current().converseList(commandList);
	  } catch (IOException e) {
	    System.err.println(e);
	    Thread.currentThread().dumpStack();
	  } catch (CycApiException e) { 
	    System.err.println(e);
	    Thread.currentThread().dumpStack(); 
	  }
	  if (DEBUG) { System.out.println("blueEvent = " + blueEvent); }
	  if (blueEvent != null && 
	      blueEvent instanceof CycList &&
	      blueEvent.size() == 2) {
	    IsCommand command = instantiateCommand((String)blueEvent.first(), (CycList)blueEvent.second()); // todo: check that String
	    if (DEBUG) { System.out.println("command = " + command); }
	    if (command != null) {
	      commandProcessor.processCommand(command);
	    }
	    try { sleep(50); } catch (InterruptedException e) {}
	  } else {
	    try { sleep(2000); } catch (InterruptedException e) {}
	  }
	} else {
	  try { sleep(5000); } catch (InterruptedException e) {}
//  	  synchronized (this) {
//  	    try { wait(200); } catch (InterruptedException e) {}
//  	    notify();
//  	  }
	}
      }
    } catch (IOException e) {
      System.err.println(e);
      Thread.currentThread().dumpStack();
    } catch (CycApiException e) {
      System.err.println(e);
      Thread.currentThread().dumpStack();
    }
  }
  
  ////////////////////////////////////////////////////////////////////////////////
  // @todo: review below code.

  public IsCommand instantiateCommand(String _commandClassName, CycList _constructorArgs) {
    Class commandClass;
    try {
      commandClass = Class.forName(_commandClassName);
    } catch (ClassNotFoundException e) { 
      System.err.println(e);
      Thread.currentThread().dumpStack(); 
      return null; 
    }
    if (commandClass != null) {
      Class[] parameterTypes = new Class[1];
      parameterTypes[0] = CycList.class;
      Constructor commandClassConstructor;
      try {
	commandClassConstructor = commandClass.getConstructor(parameterTypes);
      } catch (NoSuchMethodException e) { 
	System.err.println(e);
	Thread.currentThread().dumpStack(); 
	return null; 
      }
      Object constructorArgs[] = { _constructorArgs };
      Object newInstance;
      System.out.println("constructorArgs = " + _constructorArgs);
      try {
	newInstance = commandClassConstructor.newInstance(constructorArgs);
      } catch (InstantiationException e) { 
	System.err.println(e);
	Thread.currentThread().dumpStack(); 
	return null;
      } catch (IllegalAccessException e) { 
	System.err.println(e);
	Thread.currentThread().dumpStack(); 
	return null;
      } catch (InvocationTargetException e) { 
	System.err.println("e = " + e + ", message = " + e.getMessage() + " targetE = " + ((InvocationTargetException)e).getTargetException() + ", targetEMessage = " + ((InvocationTargetException)e).getTargetException().getMessage());
	Thread.currentThread().dumpStack(); 
	return null; 
      }
      if (newInstance instanceof IsCommand) {
	return (IsCommand)newInstance;
      }
    } else {
      Thread.currentThread().dumpStack(); 
      return null;
    }
    Thread.currentThread().dumpStack(); 
    return null;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // IsPausable

  private boolean isPaused = false;
  
  public void setPaused(boolean _isPaused) {
    isPaused = _isPaused; //synchronized (this) { notify(); }}
  }
  public boolean isPaused() { return isPaused; }

}
