/* $Id: IsNode.java,v 1.12 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.graph;

/**
 * Implement to obtain the ability to be used as a graph node.  Nodes are just
 * objects that have a "core" object (any Object).  Once added to a graph, they
 * have a graph value.  (Nodes can not be members of more than one graph).
 * @see GenericNode
 *
 * @author John Jantos
 * @date 2001/08/09
 * @version $Id: IsNode.java,v 1.12 2002/05/23 22:30:14 jantos Exp $
 */

public interface IsNode {
  
  /** sets the core object that this node represents.
   * @param _object The core object. 
   */
  public void setCore(Object _nodeCore);

  /** gets the core object that this node represents.
   * @return the core object. 
   */
  public Object getCore();

  /** sets the graph this node is a part of.
   * @param _graph
   */
  public void setGraph(IsGraph _graph);
  
  /** gets the graph this node is a part of. 
   * @return the graph.
   */
  public IsGraph getGraph();
  
  /** sets the focus status of the node.
   * @param _isFocus
   */
  public void setFocus(boolean _isFocus);

  /** gets the focus status of the node.
   * @return the focus status
   */
  public boolean isFocus();

  /** sets the label of the node.
   * @param _label
   */
  public void setLabel(String _label);

  /** gets the label of the node.
   * @return the node label
   */
  public String getLabel();

}
