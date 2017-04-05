/* $Id: GenericEdge.java,v 1.4 2002/05/15 23:27:42 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.graph;

/**
 * A generic implementation of the IsEdge interface.
 *
 * @author John Jantos
 * @date 2002/01/17
 * @version $Id: GenericEdge.java,v 1.4 2002/05/15 23:27:42 jantos Exp $
 */

public class GenericEdge implements IsEdge {
  private static final boolean DEBUG = false;

  private IsGraph graph;
  private IsNode head;
  private IsNode tail;
  private Object relation;

  public GenericEdge(IsNode _tailNode, IsNode _headNode, Object _relation) {
    setTail(_tailNode);
    setHead(_headNode);
    setRelation(_relation);
  }

  ////////////////////////////////////////////////////////////////////////////////.
  // Qua IsEdge

  public void setGraph(IsGraph _graph) { graph = _graph; }
  public IsGraph getGraph() { return graph; }

  public void setHead(IsNode _head) { head = _head; }
  public IsNode getHead() { return head; }
  public void setTail(IsNode _tail) { tail = _tail; }
  public IsNode getTail() { return tail; }
  public void setRelation(Object _relation) { relation = _relation; }
  public Object getRelation() { return relation; }
  
  public IsNode getOtherNode(IsNode _node) {
    if (_node == head) {
      return tail;
    } else if (_node == tail) {
      return head;
    } else {
      return null;
    } 
  }

}
