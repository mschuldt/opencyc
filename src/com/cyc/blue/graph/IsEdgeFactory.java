/* $Id: IsEdgeFactory.java,v 1.4 2002/05/22 00:52:52 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.graph;

/**
 * Implement to obtain the ability to create edges.
 *
 * @author John Jantos
 * @date 2002/02/07
 * @version $Id: IsEdgeFactory.java,v 1.4 2002/05/22 00:52:52 jantos Exp $
 */

public interface IsEdgeFactory {
  
  /** sets the class that edges will be instance of. 
   * @param _edgeClass
   */
  public void setEdgeClass(Class _edgeClass);

  /** gets the class that edges are instances of.
   * @return the edge class. 
   */
  public Class getEdgeClass();

  /** instantiates a new edge (unless the edge is already instatiated).
   * @param _headNode
   * @param _tailNode
   * @param _relation
   * @return the edge. 
   */
  public IsEdge instantiate(IsNode _headNode, IsNode _tailNode, Object _relation);

  /** finds an instatiated edge.
   * @param _headNode
   * @param _tailNode
   * @param _relation
   * @return the found edge or null.
   */
  public IsEdge find(IsNode _headNode, IsNode _tailNode, Object _relation);
  
}

/*
public void setRelationClass(Class _relationClass);
public Class getRelationClass();
public void forget() {
  findEdgesByHead(head).remove(this);
  findEdgesByTail(tail).remove(this);
  findEdgesByRelation(relation).remove(this);
}
*/
