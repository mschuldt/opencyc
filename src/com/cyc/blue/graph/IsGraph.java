/* $Id: IsGraph.java,v 1.18 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.graph;

import java.util.Iterator;
import java.util.Set;

/**
 * Implement to obtain the ability to be used as a graph.
 *
 * @author John Jantos
 * @date 2001/08/09
 * @version $Id: IsGraph.java,v 1.18 2002/05/23 22:30:14 jantos Exp $
 */

public interface IsGraph {

  ////////////////////////////////////////////////////////////////////////////////
  // general methods

  /** clears this graph by removing all nodes and edges. 
   */  
  public void clear();

  ////////////////////////////////////////////////////////////////////////////////
  // methods for nodes

  /** sets the factory that nodes will be created by. 
   * @param _nodeFactory
   */
  public void setNodeFactory(IsNodeFactory _nodeFactory);

  /** gets the factory that nodes are created by.
   * @return the node factory. 
   */
  public IsNodeFactory getNodeFactory();

  /** sets the factory that edges will be created by. 
   * @param _edgeFactory
   */
  public void setEdgeFactory(IsEdgeFactory _edgeFactory);

  /** gets the factory that edges are created by.
   * @return the edge factory. 
   */
  public IsEdgeFactory getEdgeFactory();

  /** instantiates a new node
   * @param _nodeCore the new node's core object.
   * @return the new node. 
   */
  public IsNode instantiateNode(Object _nodeCore);

  /** finds a node by its core object.
   * @param _nodeCore the node's core object.
   * @return the exisitng node. 
   */
  public IsNode findNode(Object _nodeCore);
  
  /** checks whether a node is in this graph.
   * @param _node
   * @return <code>true</code> if it's in there, <code>false</code> if not. 
   */
  public boolean containsNode(IsNode _node);

  /** adds a node to this graph.  the node should be an instance of the specified node class.
   * @param _node
   */
  public void addNode(IsNode _node);

  /** removes a node from this graph.
   * @param _node
   */
  public void removeNode(IsNode _node);

  /** gets a set of the nodes in this graph
   * @return a set containing the nodes in this graph.
   */
  public Set getNodes();

  /** gets an iterator over the nodes in this graph.
   * @return an iterator over the nodes in this graph.
   */
  public Iterator nodesIterator();
  
  ////////////////////////////////////////////////////////////////////////////////
  // methods for edges

  /** instantiates a new edge
   * @param _headNode
   * @param _tailNode
   * @param _relation
   * @return the new edge.
   */
  public IsEdge instantiateEdge(IsNode _nodeIn, IsNode _nodeOut, Object _relation);

  /** finds an edge by its nodes and relation.
   * @param _headNode
   * @param _tailNode
   * @param _relation
   * @return the new edge.
   */
  public IsEdge findEdge(IsNode _nodeIn, IsNode _nodeOut, Object _relation);

  /** checks whether the edge is in this graph
   * @param _edge
   * @return <code>true</code> if it's in there, <code>false</code> if not. 
   */
  public boolean containsEdge(IsEdge _edge);

  /** adds an edge to this graph
   * @param _edge
   */
  public void addEdge(IsEdge _edge);

  /** removes an edge from this graph
   * @param _edge 
   */
  public void removeEdge(IsEdge _edge);

  /** gets a set of the edges in this graph
   * @return a set containing the edges in this graph.
   */
  public Set getEdges();

  /** gets an iterator over the edges in this graph.
   * @return an iterator over the edges in this graph.
   */
  public Iterator edgesIterator();

  ////////////////////////////////////////////////////////////////////////////////
  // methods for incidence

  /** gets an iterator over the edges incident to the specified node in this graph.
   * @param _node
   * @return an iterator over the edges.
   */
  public Iterator incidentEdgesIterator(IsNode _node);

  /** gets an iterator over the edges with the specified node as head in this graph.
   * @param _node
   * @return an iterator over the edges.
   */
  public Iterator incidentEdgesInIterator(IsNode _node);

  /** gets an iterator over the edges with the specified node as tail in this graph.
   * @param _node
   * @return an iterator over the edges.
   */
  public Iterator incidentEdgesOutIterator(IsNode _node);

  /** gets an count of the edges incident to the specified node.
   * @param _node
   * @return an iterator over the edges.
   */
  public int incidentEdgesCount(IsNode _node);

  /** gets an count of the edges with the specified node as head.
   * @param _node
   * @return an iterator over the edges.
   */

  public int incidentEdgesInCount(IsNode _node);

  /** gets an count of the edges with the specified node as tail.
   * @param _node
   * @return an iterator over the edges.
   */
  public int incidentEdgesOutCount(IsNode _node);

}
