/* $Id: GraphCommandExecutor.java,v 1.9 2002/05/23 22:40:58 jantos Exp $
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
import com.cyc.blue.graph.*;
import com.cyc.blue.gui.*;
import com.cyc.blue.event.*;

/**
 * An implementation of IsGraphCommandExecutor.
 *
 * @author John Jantos
 * @date 2001/09/24
 * @version $Id: GraphCommandExecutor.java,v 1.9 2002/05/23 22:40:58 jantos Exp $
 */

public class GraphCommandExecutor implements IsGraphCommandExecutor {
  private static final boolean DEBUG = false;
  private IsGraph graph;

  public GraphCommandExecutor(IsGraph _graph) {
    graph = _graph;
  }

  ////////////////////////////////////////////////////////////////////////////////
  // IsCommandExecutor interface

  public Object execute(IsCommand _command) {
    Object result = null;
    if (DEBUG) System.err.println("GraphCommandExecutor.execute(" + _command + ")");
    if (BlueEventService.current() != null) {
      BlueEventService.current().publish(new GraphCommandEvent_Begin(getGraph(), _command));
    }
    try {
      result = _command.execute(this);
      if (BlueEventService.current() != null) {
	BlueEventService.current().publish(new GraphCommandEvent_End(getGraph(), _command));
      }
    } catch (Exception e) {
      System.err.println(e); 
      Thread.currentThread().dumpStack();
      if (BlueEventService.current() != null) {
	BlueEventService.current().publish(new GraphCommandEvent_Fail(getGraph(), _command));
      }
    }
    return result;
  }

  public boolean unexecute(IsCommand _command) {
    if (DEBUG) System.err.println("GraphCommandExecutor.unexecute(" + _command + ")");
    return _command.unexecute(this);
  }

  public Object reexecute(IsCommand _command) {
    if (DEBUG) System.err.println("GraphCommandExecutor.reexecute(" + _command + ")");
    return _command.reexecute(this);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsGraphCommandExecutor

  public void setGraph(IsGraph _graph) {
    graph = _graph;
  }
  public IsGraph getGraph() {
    return graph;
  }


  ////////////////////////////////////////////////////////////////////////////////
  // Justifications

  HashMap nodeJustificationsMap = new HashMap();
  
  protected void addNodeJustification(IsNode _node, IsCommand _command) {
    HashSet nodeJustifications = getNodeJustifications(_node);
    if (nodeJustifications == null) {
      nodeJustifications = new HashSet();
      nodeJustifications.add(_command);
      nodeJustificationsMap.put(_node, nodeJustifications);
    } else {
      nodeJustifications.add(_command);
    }
  }

  protected HashSet getNodeJustifications(IsNode _node) {
    return (HashSet)nodeJustificationsMap.get(_node);
  }

  public Iterator getNodeJustificationsIterator(IsNode _node) {
    HashSet nodeJustifications = getNodeJustifications(_node);
    if (nodeJustifications != null) {
      return nodeJustifications.iterator();
    } else {
      return null;
    }
  }

  public boolean isNodeJustified(IsNode _node) {
    HashSet nodeJustifications = getNodeJustifications(_node);
    if (nodeJustifications == null || nodeJustifications.isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  protected boolean removeNodeJustification(IsNode _node, IsCommand _command) {
    HashSet nodeJustifications = getNodeJustifications(_node);
    if (nodeJustifications != null) {
      return nodeJustifications.remove(_command);
    } else {
      return false;
    }
  }

  public void addNode(IsCommand _command, IsNode _node) {
    if (DEBUG) System.err.println("GraphCommandExecutor.addNode(" + _command + ", " + _node + ")");
    if (!isNodeJustified(_node)) {
      getGraph().addNode(_node);
    }
    addNodeJustification(_node, _command);
  }

  public boolean removeNode(IsCommand _command, IsNode _node) {
    if (DEBUG) System.err.println("GraphCommandExecutor.removeNode(" + _command + ", " + _node + ")");
    if (removeNodeJustification(_node, _command)) {
      if (!isNodeJustified(_node)) {
	getGraph().removeNode(_node);
	return true;
      }
    }
    return false;
  }

     
  HashMap edgeJustificationsMap = new HashMap();
  
  protected void addEdgeJustification(IsEdge _edge, IsCommand _command) {
    HashSet edgeJustifications = getEdgeJustifications(_edge);
    if (edgeJustifications == null) {
      edgeJustifications = new HashSet();
      edgeJustifications.add(_command);
      edgeJustificationsMap.put(_edge, edgeJustifications);
    } else {
      edgeJustifications.add(_command);
    }
  }

  protected HashSet getEdgeJustifications(IsEdge _edge) {
    return (HashSet)edgeJustificationsMap.get(_edge);
  }

  public Iterator getEdgeJustificationsIterator(IsEdge _edge) {
    HashSet edgeJustifications = getEdgeJustifications(_edge);
    if (edgeJustifications != null) {
      return edgeJustifications.iterator();
    } else {
      return null;
    }
  }

  public boolean isEdgeJustified(IsEdge _edge) {
    HashSet edgeJustifications = getEdgeJustifications(_edge);
    if (edgeJustifications == null || edgeJustifications.isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  protected boolean removeEdgeJustification(IsEdge _edge, IsCommand _command) {
    HashSet edgeJustifications = getEdgeJustifications(_edge);
    if (edgeJustifications != null) {
      return edgeJustifications.remove(_command);
    } else {
      return false;
    }
  }
  
  public void addEdge(IsCommand _command, IsEdge _edge) {
    if (DEBUG) System.err.println("GraphCommandExecutor.addEdge(" + _command + ", " + _edge + ")");
    if (!isEdgeJustified(_edge)) {
      getGraph().addEdge(_edge);
    }
    addEdgeJustification(_edge, _command);
  }

  /*
  public IsEdge addEdge(IsCommand _command, IsNode _tailNode, IsNode _headNode, Object _relation) {
    if (DEBUG) System.err.println("GraphCommandExecutor.addEdge(" + _command + ", " + _tailNode + ", " + _headNode + ", " + _relation + ")");
    IsNode tailNode = addNode(_command, _tailNode);
    IsNode headNode = addNode(_command, _headNode);
    IsEdge edge = graph.instantiateEdge(tailNode, headNode, _relation);
    if (!isEdgeJustified(edge)) {
      graph.addEdge(edge);
    }
    addEdgeJustification(edge, _command);
    return edge;
  }
  */
  
  public boolean removeEdge(IsCommand _command, IsEdge _edge) {
    if (removeEdgeJustification(_edge, _command)) {
      if (!isEdgeJustified(_edge)) {
	getGraph().removeEdge(_edge);
	return true;
      }
    }
    return false;
  }
  
}



