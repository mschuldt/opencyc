/* $Id: GenericGraph.java,v 1.6 2002/05/15 23:27:43 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A generic implementation of the IsGraph interface.
 *
 * @author John Jantos
 * @date 2002/01/17
 * @version $Id: GenericGraph.java,v 1.6 2002/05/15 23:27:43 jantos Exp $
 */

public class GenericGraph implements IsGraph {
  private static final boolean DEBUG = false;

  private Set nodes = Collections.synchronizedSet(new HashSet());
  private Set edges = Collections.synchronizedSet(new HashSet());
  private Map nodeInEdgesMap = Collections.synchronizedMap(new HashMap());
  private Map nodeOutEdgesMap = Collections.synchronizedMap(new HashMap());

  private IsNodeFactory nodeFactory = null;
  private IsEdgeFactory edgeFactory = null;

  public GenericGraph(Class _nodeClass, Class _edgeClass) {
    setNodeFactory(new GenericNodeFactory(_nodeClass));
    setEdgeFactory(new GenericEdgeFactory(_edgeClass)); //@todo GenericRelationClass??
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsGraph

  public void setNodeFactory(IsNodeFactory _nodeFactory) {
    nodeFactory = _nodeFactory;
  }
  public IsNodeFactory getNodeFactory() {
    return nodeFactory;
  }

  public void setEdgeFactory(IsEdgeFactory _edgeFactory) {
    edgeFactory = _edgeFactory;
  }
  public IsEdgeFactory getEdgeFactory() {
    return edgeFactory;
  }

  public synchronized void clear() {
    if (DEBUG) { System.err.println("GenericGraph.clear()"); }
    synchronized (edges) {
      Iterator edgesIterator = edges.iterator();
      while (edgesIterator.hasNext()) {
	IsEdge edge = (IsEdge)edgesIterator.next();
	removeEdge(edge);
      }
    }
    synchronized (nodes) {
      Iterator nodesIterator = nodes.iterator();
      while (nodesIterator.hasNext()) {
	IsNode node = (IsNode)nodesIterator.next();
	removeNode(node);
      }
    }
  }

  // methods for nodes

  public IsNode instantiateNode(Object _core) {
    if (nodeFactory != null) {
      return nodeFactory.instantiate(_core);
    } else {
      System.err.println("ERROR " + this + ".instantiateNode(" + _core + ") failed because nodeFactory is null!");
      return null;
    }
  }
  
  public IsNode findNode(Object _core) {
    if (nodeFactory != null) {
      return nodeFactory.find(_core);
    } else {
      System.err.println("ERROR " + this + ".findNode(" + _core + ") failed because nodeFactory is null!");
      return null;
    }
  }

  public boolean containsNode(IsNode _node) {
    return nodes.contains(_node);
  }

  public void addNode(IsNode _node) {
    if (DEBUG) { System.out.println("GenericGraph.addNode(" + _node + ")"); }
    synchronized (this) {
      nodes.add(_node);
      _node.setGraph(this);
    }
  }
  
  public void removeNode(IsNode _node) {
    if (DEBUG) { System.out.println("GenericGraph.removeNode(" + _node + ")"); }
    synchronized (this) {
      nodes.remove(_node);
      nodeInEdgesMap.remove(_node);
      nodeOutEdgesMap.remove(_node);
    }
  }
  
  public Set getNodes() {
    if (DEBUG) { System.out.println("GenericGraph.getNodes()"); }
    return nodes; // todo: clone?
  }

  public Iterator nodesIterator() { 
    if (DEBUG) { System.out.println("GenericGraph.nodesIterator()"); }
    return getNodes().iterator();
  }

  // methods for edges

  public IsEdge instantiateEdge(IsNode _tailNode, IsNode _headNode, Object _relation) {
    if (edgeFactory != null) {
      return edgeFactory.instantiate(_tailNode, _headNode, _relation);
    } else {
      System.err.println("ERROR " + this + ".instantiateEdge(" + _tailNode + ", " + _headNode + ", " + _relation + ") failed because edgeFactory is null!");
      return null;
    }
  }

  public IsEdge findEdge(IsNode _tailNode, IsNode _headNode, Object _relation) {
    if (edgeFactory != null) {
      return edgeFactory.find(_tailNode, _headNode, _relation);
    } else {
      System.err.println("ERROR " + this + ".findEdge(" + _tailNode + ", " + _headNode + ", " + _relation + ") failed because edgeFactory is null!");
      return null;
    }
  }

  public boolean containsEdge(IsEdge _edge) {
    if (DEBUG) { System.err.println("GenericGraph.containsEdge(" + _edge + ")"); }
    synchronized (edges) {
      return edges.contains(_edge);
    }
  }
  
  public void addEdge(IsEdge _edge) {
    if (DEBUG) { System.err.println("GenericGraph.addEdge(" + _edge + ")"); }
    synchronized (this) {
      // add nodes if they're not in the graph
      if (!containsNode(_edge.getTail())) { addNode(_edge.getTail()); }
      if (!containsNode(_edge.getHead())) { addNode(_edge.getHead()); }
      edges.add(_edge);
      addIncidence(_edge);
      _edge.setGraph(this);
    }
  }

  public void removeEdge(IsEdge _edge) {
    if (DEBUG) { System.err.println("GenericGraph.removeEdge(" + _edge + ")"); }
    synchronized (this) {
      removeIncidence(_edge);
      edges.remove(_edge);
    }
  }
  
  public Set getEdges() {
    if (DEBUG) { System.err.println("GenericGraph.getEdges()"); }
    return edges;
  }

  public Iterator edgesIterator() {
    if (DEBUG) { System.err.println("GenericGraph.edgesIterator()"); }
    synchronized (edges) {
      return edges.iterator();
    }
  }

  public Iterator incidentEdgesIterator(IsNode _node) {
    if (DEBUG) { System.out.println("--> (GenericGraph)" + this + ".incidentEdgesIterator(" + _node + ")"); }
    HashSet incidentEdgesIn = incidentEdgesIn(_node);
    HashSet incidentEdgesOut = incidentEdgesOut(_node);
    HashSet incidentEdges = new HashSet();
    if (DEBUG) { System.out.println("!!! incidentEdgesIn = " + incidentEdgesIn); }
    if (DEBUG) { System.out.println("!!! incidentEdgesOut = " + incidentEdgesOut); }
    if (incidentEdgesIn != null) {
      incidentEdges.addAll(incidentEdgesIn);
    }
    if (incidentEdgesOut != null) {
      incidentEdges.addAll(incidentEdgesOut);
    }
    if (DEBUG) { System.out.println("!!! incidentEdges = " + incidentEdges); }
    return incidentEdges.iterator();
  }

  public Iterator incidentEdgesOutIterator(IsNode _node) {
    if (DEBUG) { System.out.println("GenericGraph.incidentEdgesOutIterator(" + _node + ")"); }
    HashSet incidentEdgesOut = incidentEdgesOut(_node);
    if (incidentEdgesOut == null) {
      return null;
    } else {
      return incidentEdgesOut.iterator();
    }
  }

  public Iterator incidentEdgesInIterator(IsNode _node) {
    if (DEBUG) { System.out.println("GenericGraph.incidentEdgesInIterator(" + _node + ")"); }
    HashSet incidentEdgesIn = incidentEdgesIn(_node);
    if (incidentEdgesIn == null) {
      return null;
    } else {
      return incidentEdgesIn.iterator();
    }
  }

  public int incidentEdgesCount(IsNode _node) {
    return incidentEdgesInCount(_node) + incidentEdgesOutCount(_node);
  }

  public int incidentEdgesInCount(IsNode _node) {
    HashSet incidentEdgesIn = incidentEdgesIn(_node);
    if (incidentEdgesIn == null) {
      return 0;
    } else {
      if (DEBUG) { System.out.println("incidentEdgesInCount(" + _node + ") =" + incidentEdgesIn.size()); }
      return incidentEdgesIn.size();
    }
  }

  public int incidentEdgesOutCount(IsNode _node) {
    HashSet incidentEdgesOut = incidentEdgesOut(_node);
    if (incidentEdgesOut == null) {
      return 0;
    } else {
      if (DEBUG) { System.out.println("incidentEdgesOutCount(" + _node + ") =" + incidentEdgesOut.size()); }
      return incidentEdgesOut.size();
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // support methods

  private void addIncidence(IsEdge _edge) {
    if (DEBUG) { System.err.println("--> " + this + ".addIncidence(" +  _edge + "): head = " + _edge.getHead() + "  tail = " + _edge.getTail()); }
    synchronized (nodeInEdgesMap) {
      IsNode headNode = _edge.getHead();
      HashSet nodeInEdges = (HashSet)nodeInEdgesMap.get(headNode);
      if (nodeInEdges == null) {
	HashSet inEdges = new HashSet();
	inEdges.add(_edge);
	nodeInEdgesMap.put(headNode, inEdges);
      } else {
	nodeInEdges.add(_edge);
      }
    }
    synchronized (nodeOutEdgesMap) {
      IsNode tailNode = _edge.getTail();
      HashSet nodeOutEdges = (HashSet)nodeOutEdgesMap.get(tailNode);
      if (nodeOutEdges == null) {
	HashSet outEdges = new HashSet();
	outEdges.add(_edge);
	nodeOutEdgesMap.put(tailNode, outEdges);
      } else {
	nodeOutEdges.add(_edge);
      }
    }
  }

  private void removeIncidence(IsEdge _edge) {
    if (DEBUG) { System.err.println("GenericGraph.removeIncidence(" + _edge + ")"); }
    synchronized (nodeInEdgesMap) {
      HashSet inEdges = (HashSet)nodeInEdgesMap.get(_edge.getHead());
      if (inEdges != null) {
	inEdges.remove(_edge);
      }
    }
    synchronized (nodeOutEdgesMap) {
      HashSet outEdges = (HashSet)nodeOutEdgesMap.get(_edge.getHead());
      if (outEdges != null) {
	outEdges.remove(_edge);
      }
    }
  }    

  private HashSet incidentEdgesIn(IsNode _node) {
    return (HashSet)nodeInEdgesMap.get(_node);
  }

  private HashSet incidentEdgesOut(IsNode _node) {
    return (HashSet)nodeOutEdgesMap.get(_node);
  }

}
