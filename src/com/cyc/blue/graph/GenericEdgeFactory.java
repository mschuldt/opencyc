/* $Id: GenericEdgeFactory.java,v 1.3 2002/04/10 22:29:54 jantos Exp $
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A generic implementation of the IsEdgeFactory interface.
 * @see GenericEdge
 *
 * @author John Jantos
 * @date 2002/02/07
 * @version $Id: GenericEdgeFactory.java,v 1.3 2002/04/10 22:29:54 jantos Exp $
 */

public class GenericEdgeFactory implements IsEdgeFactory {
  private static final boolean DEBUG = false;
  private Map edgesByHeads = Collections.synchronizedMap(new HashMap());
  private Map edgesByTails = Collections.synchronizedMap(new HashMap());
  private Map edgesByRelations = Collections.synchronizedMap(new HashMap());
  private Class edgeClass;
  
  public GenericEdgeFactory(Class _edgeClass) {
    setEdgeClass(_edgeClass);
  }

  ////////////////////////////////////////////////////////////////////////////////

  public void setEdgeClass(Class _edgeClass) { edgeClass = _edgeClass; }
  public Class getEdgeClass() { return edgeClass; }
  
  public IsEdge instantiate(IsNode _tailNode, IsNode _headNode, Object _relation) {
    if (DEBUG) { System.out.println("--> " + this + ".instantiate(" + _tailNode + ", " + _headNode + ", " + _relation + ")"); }
    if (DEBUG) { System.out.println("--- GenericEdgeFactory.instantiate: edgeClass = " + edgeClass); }
    IsEdge edge = find(_tailNode, _headNode, _relation);
    if (edge == null) {
      if (edgeClass != null) {
	Class[] parameterTypes = new Class[3];
	parameterTypes[0] = IsNode.class;
	parameterTypes[1] = IsNode.class;
	parameterTypes[2] = Object.class;
	
	Constructor edgeConstructor;
	try {
	  edgeConstructor = edgeClass.getConstructor(parameterTypes);
	} catch (NoSuchMethodException e) { System.err.println(e); Thread.currentThread().dumpStack(); return null; }
	
	Object initArgs[] = { _tailNode, _headNode, _relation };
	try {
	  edge = (IsEdge)edgeConstructor.newInstance(initArgs);
	  
	} catch (InstantiationException e) { System.err.println(e); Thread.currentThread().dumpStack(); return null;
	} catch (IllegalAccessException e) { System.err.println(e); Thread.currentThread().dumpStack(); return null;
	} catch (IllegalArgumentException e) { System.err.println(e); Thread.currentThread().dumpStack(); return null;
	} catch (InvocationTargetException e) { System.err.println(e); Thread.currentThread().dumpStack(); return null; }
	
	if (edge != null) {
	  Set edgesByHead = findEdgesByHead(_headNode);
	  if (edgesByHead == null) {
	    edgesByHead = new HashSet();
	    edgesByHead.add(edge);
	    edgesByHeads.put(_headNode, edgesByHead);
	  } else {
	    edgesByHead.add(edge);
	  }
	  
	  Set edgesByTail = findEdgesByTail(_tailNode);
	  if (edgesByTail == null) {
	    edgesByTail = new HashSet();
	    edgesByTail.add(edge);
	    edgesByTails.put(_tailNode, edgesByTail);
	  } else {
	    edgesByTail.add(edge);
	  }
	  
	  Set edgesByRelation = findEdgesByRelation(_relation);
	  if (edgesByRelation == null) {
	    edgesByRelation = new HashSet();
	    edgesByRelation.add(edge);
	    edgesByRelations.put(_relation, edgesByRelation);
	  } else {
	    edgesByRelation.add(edge);
	  }
	}
      }
    }
    if (DEBUG) { System.out.println("--- GenericEdgeFactory.instantiate: returning = " + edge); }
    return edge;
  }
  
  public IsEdge find(IsNode _tailNode, IsNode _headNode, Object _relation) {
    if (DEBUG) { System.out.println(this + ".find(" + _tailNode + ", " + _headNode + "," + _relation + ")"); }
    IsEdge foundEdge = null;
    Set possibleEdges = findEdgesByTail(_tailNode);
    if (possibleEdges != null && !possibleEdges.isEmpty() && findEdgesByHead(_headNode) != null) {
      possibleEdges.retainAll(findEdgesByHead(_headNode));
      if (possibleEdges != null && !possibleEdges.isEmpty()) {
	Iterator possibleEdgesIterator = possibleEdges.iterator();
	while (possibleEdgesIterator.hasNext()) {
	  IsEdge possibleEdge = (IsEdge)possibleEdgesIterator.next();
	  if (possibleEdge.getRelation().equals(_relation)) {
	    if (foundEdge == null) {
	      foundEdge = (IsEdge)possibleEdge;
	    } else {
	      System.err.println("Blue: GenericEdge.find had error: found two edges, " + foundEdge + " and " + possibleEdge);
	      //throw CycEdgeStructureInvalid;
	    }
	  }
	}
      }
    }
    return foundEdge;
  }

  private Set findEdgesByHead(IsNode _head) {
    if (DEBUG) { System.out.println("--> " + this + ".findEdgesByHead(" + _head + ")"); }
    Set result = null;
    if (edgesByHeads != null) {
      result = (Set)edgesByHeads.get(_head);
    }
    if (DEBUG) { System.out.println("<-- findEdgesByHead: returning " + result); }
    return result;
  }

  private Set findEdgesByTail(IsNode _tail) {
    if (DEBUG) { System.out.println("--> " + this + ".findEdgesByTail(" + _tail + ")"); }
    Set result = null;
    if (edgesByTails != null) {
      result = (Set)edgesByTails.get(_tail);
    }
    if (DEBUG) { System.out.println("<-- findEdgesByTail: returning " + result); }
    return result;
  }

  private Set findEdgesByRelation(Object _relation) {
    if (DEBUG) { System.out.println("--> GenericEdge.findEdgesByRelation(" + _relation + ")"); }
    Set result = null;
    if (edgesByRelations != null) {
      result = (Set)edgesByRelations.get(_relation);
    }
    if (DEBUG) { System.out.println("<-- findEdgesByRelation: returning " + result); }
    return result;
  }


}
