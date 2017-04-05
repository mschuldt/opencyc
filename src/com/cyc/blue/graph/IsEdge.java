/* $Id: IsEdge.java,v 1.13 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.graph;

/**
 * Implement to obtain the ability to be used as a graph edge.
 * @see GenericEdge
 *
 * @author John Jantos
 * @date 2001/01/17
 * @version $Id: IsEdge.java,v 1.13 2002/05/23 22:30:14 jantos Exp $
 */

public interface IsEdge {

  /** sets the head node.
   * @param _headNode
   */
  public void setHead(IsNode _headNode);

  /** gets the head node.
   * @return the head node.
   */
  public IsNode getHead();

  /** sets the tail node.
   * @param _tailNode
   */
  public void setTail(IsNode _tailNode);

  /** gets the tail node.
   * @return the tail node.
   */
  public IsNode getTail();

  /** sets the relation represented by this edge.
   * @param _relation The relation
   */
  public void setRelation(Object _relation);

  /** gets the relation represented by this edge.
   * @return the relation.
   */
  public Object getRelation();

  /** gets the head node if given the tail, get the tail node if given the head.
   * @param _node the head or tail node.
   * @return the tail or head node.
   */
  public IsNode getOtherNode(IsNode _node);

  /** sets the graph this node is a part of.
   * @param _graph
   */
  public void setGraph(IsGraph _graph);
  
  /** gets the graph this node is a part of. 
   * @return the graph.
   */
  public IsGraph getGraph();
}
