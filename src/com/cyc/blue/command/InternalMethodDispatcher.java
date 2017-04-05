/* $Id: InternalMethodDispatcher.java,v 1.3 2002/05/23 22:30:14 jantos Exp $
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
 * Subclass this class to inherit the dispatchByNameAndArg method which finds
 * methods of a certain name by argument type.  Note that currently caching is
 * not implemented which makes this class somewhat inefficient.
 *
 * @author John Jantos
 * @date 2001/09/19
 * @version $Id: InternalMethodDispatcher.java,v 1.3 2002/05/23 22:30:14 jantos Exp $
 */

public class InternalMethodDispatcher {
  private static final boolean DEBUG = false;

  /** finds the method with specified name that accepts the specified object as
   * an argument the calls the method and returns the resulting object.
   * @param _methodName
   * @param _object the parameter to pass to the method if one is found
   * @return the result object or null if no method found.
   */
  public Object dispatchByNameAndArg(String _methodName, Object _object) {
    try {
      if (DEBUG) { System.out.println("_object.getClass() -> " + _object.getClass()); }
      Method method = getMethodByNameAndArgType(_methodName, _object.getClass());
      if (DEBUG) { System.out.println("getMethodByNameAndArgType returned: " + method); }
      if (method != null) {
	return method.invoke(this, new Object[] { _object });
      } else {
	return null;
      }
    } catch (InvocationTargetException e) {
      e.getTargetException().printStackTrace();
      //throw new RuntimeException(e.toString()); // @todo: nasty
    } catch (Exception e) {
      //throw new RuntimeException(e.toString()); // @todo: nasty
    }
    return null;
  }

  /** determines the most specific method with specified name that accepts an
   * argument of the spacified type.
   * @param _methodName
   * @param _class
   * @return the method found or null in none found.
   */
  protected Method getMethodByNameAndArgType(String _methodName, Class _class) {
    if (DEBUG) { System.out.println("--> InternalMethodDispatcher[" + this + "].getMethodByNameAndArgType(" + _methodName + ", " + _class + ")"); }
    Method method = null;
    try {
      if (DEBUG) { System.out.println("-- testing arg " + _class); }
      method = getClass().getMethod(_methodName, new Class[] { _class });
    } catch (NoSuchMethodException e) {
    }
    // search for methods on interfaces
    if (method == null) { 
      Class[] interfaces = _class.getInterfaces();
      for (int i = 0; method == null && i < interfaces.length; i++) {
	try {
	  if (DEBUG) { System.out.println("--- testing " +  interfaces[i]); }
	  method = getClass().getMethod(_methodName, new Class[] { interfaces[i] });
	} catch (NoSuchMethodException e) {
	  method = getMethodByNameAndArgType(_methodName, interfaces[i]);
	}
      }
    }
    // search for methods on superclass (recurse)
    if (method == null) {
      Class superclass = _class.getSuperclass();
      if (method == null && superclass != null) {
	if (DEBUG) { System.out.println("--- recursing"); }
	method = getMethodByNameAndArgType(_methodName, superclass);
	if (DEBUG) { System.out.println("--- continuing"); }
      }
    }
    
    if (DEBUG) { System.out.println("--> getMethodByNameAndArgType(" + _methodName + ", " + _class + "): returning " + method); }
    return method;
  }
      

}

/*  
  // copied from source on the internet.  needs to be rewritten (for style as well as efficiency.) 
  protected static Hashtable getMethodCacheMap = new Hashtable();
  protected Method getMethodByNameAndArgType2(String _methodName, Class _class) {
    if (DEBUG) { System.out.println("GenericInternalDispatcher.getMethodByNameAndArgType(" + _methodName + ", " + _class + ")"); }
    if (_class == null) {
      return null;
    }
    Method method = null;
//      // chech cache @todo abstract
//      Hashtable methodsMap = (Hashtable) getMethodCacheMap.get(getClass());
//      if(methodsMap == null) {
//        getMethodCacheMap.put(getClass(), methodsMap = new Hashtable(10));
//      }
    
//      method = (Method) methodsMap.get(_class);
    
//      if (method != null) {
//        return method;
//      }

    Class thisclass = getClass();
    Class newClass = _class;
    // Try the superclasses
    while (method == null && newClass != null && newClass != Object.class) {
      String methodString = newClass.getName();
      try {
	//System.out.println("method = " + thisclass + ".getMethod(" + methodString + ", new Class[] {" + newClass + "})");
	method = thisclass.getMethod(_methodName, new Class[] {newClass});
      } catch (NoSuchMethodException e) {
	newClass = newClass.getSuperclass();
      }
    }
    // Try the interfaces
    if (method == null) {
      newClass = _class;
    }
    while (method == null && newClass != null && newClass != Object.class) {
      Class[] interfaces = newClass.getInterfaces();
      for (int i = 0; method == null && i < interfaces.length; i++) {
	String methodString = interfaces[i].getName();
	methodString = methodString.substring(methodString.lastIndexOf('.') + 1);
	try {
	  //System.out.println("method = " + thisclass + ".getMethod(" + methodString + ", new Class[] { " + interfaces[i] + "})");
	  method = thisclass.getMethod(_methodName, new Class[] {interfaces[i]});
	}
	catch (NoSuchMethodException e) {
	  Method rval = getMethodByNameAndArgType(_methodName, interfaces[i]);
	  if (rval != null) {
	    method = rval;
	  }
	}
      }
      newClass = newClass.getSuperclass();
    }
    if (!_class.isInterface() && method == null) {
      try {
	method = thisclass.getMethod(_methodName, new Class[] {Object.class});
      } catch (Exception e) {
	// this shouldn't happen
      }
    }

    //  // add to method cache    
//      if (method != null) {
//        methodsMap.put(_class, method);
//      }
    
    if (DEBUG) { System.out.println("--> getMethodByNameAndArgType(" + _methodName + ", " + _class + "): returning " + method); }
    return method;
  }
*/
