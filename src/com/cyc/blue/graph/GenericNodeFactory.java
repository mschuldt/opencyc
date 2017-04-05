/* $Id: GenericNodeFactory.java,v 1.3 2002/04/10 22:29:58 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.graph;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A generic implementation of the IsNodeFactory interface.
 * @see GenericNode
 *
 * @author John Jantos
 * @date 2002/02/07
 * @version $Id: GenericNodeFactory.java,v 1.3 2002/04/10 22:29:58 jantos Exp $
 */

public class GenericNodeFactory implements IsNodeFactory {
  private static final boolean DEBUG = false;

  private Map nodesByCores = Collections.synchronizedMap(new HashMap());
  private Class nodeClass;
  
  public GenericNodeFactory(Class _nodeClass) {
    setNodeClass(_nodeClass);
  }

  ////////////////////////////////////////////////////////////////////////////////

  public void setNodeClass(Class _nodeClass) { nodeClass = _nodeClass; }
  public Class getNodeClass() { return nodeClass; }

  protected void mapCoreToNode(Object _core, IsNode _node) {
    synchronized (nodesByCores) {
      nodesByCores.put(_core, _node);
    }
  }
  
  protected IsNode getNodeFromCore(Object _core) {
    synchronized (nodesByCores) {
      return (IsNode)nodesByCores.get(_core);
    }
  }
  
  public IsNode instantiate(Object _core) {
    if (DEBUG) { System.out.println("--> " + this + ".instantiate(" + _core + ")"); }
    if (DEBUG) { System.out.println("--- GenericNodeFactory.instantiate: nodeClass = " + nodeClass); }
    IsNode node = find(_core);
    if (node == null) {
      if (nodeClass != null) {
	Class[] parameterTypes = new Class[1];
	parameterTypes[0] = Object.class;

	Constructor nodeConstructor;
	try {
	  nodeConstructor = nodeClass.getConstructor(parameterTypes);
	} catch (NoSuchMethodException e) { System.err.println(e); Thread.currentThread().dumpStack(); return null; }
	
	Object initArgs[] = { _core };
	try {
	  node = (IsNode)nodeConstructor.newInstance(initArgs);
	  
	} catch (InstantiationException e) { 
	  System.err.println(e); Thread.currentThread().dumpStack(); return null;
	} catch (IllegalAccessException e) { 
	  System.err.println(e); Thread.currentThread().dumpStack(); return null;
	} catch (IllegalArgumentException e) { 
	  System.err.println(e); Thread.currentThread().dumpStack(); return null;
	} catch (InvocationTargetException e) { 
	  System.err.println(e); System.err.println(e.getTargetException()); Thread.currentThread().dumpStack(); return null; 
	} catch (Exception e) { 
	  System.err.println(e); Thread.currentThread().dumpStack(); return null; 
	}
	if (node != null) {
	  mapCoreToNode(_core, node);
	}
      }
    }
    if (DEBUG) { System.out.println("--- GenericNodeFactory.instantiate: returning = " + node); }
    return node;
  }

  public IsNode find(Object _core) {
    return getNodeFromCore(_core);
  }

}
