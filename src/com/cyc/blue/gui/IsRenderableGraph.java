/* $Id: IsRenderableGraph.java,v 1.11 2002/05/23 22:30:15 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.util.Iterator;
import java.util.Set;
import com.cyc.blue.graph.IsGraph;
import com.cyc.blue.graph.IsNode;

/**
 * Implement to obtain the ability to be used as a graph and be renderable.  In
 * addition to the methods inherited from IsGraph, IsRenderable, and
 * IsContainer, IsRenderableGraph adds: getter/setter for the graph frame the
 * graph is in, getting nodes selected, focused, or visible, getting edges
 * visible, and iterators for those last three.
 *
 * @author John Jantos
 * @date 2001/08/23
 * @version $Id: IsRenderableGraph.java,v 1.11 2002/05/23 22:30:15 jantos Exp $
 */

public interface IsRenderableGraph extends IsGraph, IsRenderable, IsContainer {

  /** sets the graph frame.
   * @param _graphFrame
   */
  public void setGraphFrame(IsGraphFrame _graphFrame);

  /** gets the graph frame.
   * @return the graph frame
   */
  public IsGraphFrame getGraphFrame();

  /** gets the selected nodes in the graph.
   * @return the set of selected nodes
   */
  public Set getSelectedNodes();

  //public Set getSelectedEdges();

  /** gets the focus nodes in the graph.
   * @return the set of focus nodes
   */
  public Set getFocusNodes();

  //public Set getFocusEdges();

  /** gets the visible nodes in the graph.
   * @return the set of visible nodes
   */
  public Set getVisibleNodes();
  /** gets the visible nodes in the graph.
   * @return an iterator over the set of visible nodes
   */
  public Iterator visibleNodesIterator();

  /** gets the visible edges in the graph.
   * @return the set of visible edges
   */
  public Set getVisibleEdges();
  /** gets the visible edges in the graph.
   * @return an iterator over the set of visible edges
   */
  public Iterator visibleEdgesIterator();

  /** gets the visible edges in the graph incident on a specific node.
   * @param _node
   * @return the set of visible edges
   */
  public Set getVisibleIncidentEdges(IsRenderableNode _node);
  /** gets the visible edges in the graph incident on a specific node.
   * @param _node
   * @return an iterator over the set of visible edges
   */
  public Iterator visibleIncidentEdgesIterator(IsRenderableNode _node);
  
  /** gets the visibility of a node in the graph.
   * @param _node
   * @return the visibility.
   * @deprecated call isVisible on the node instead.
   */
  public boolean isNodeVisible(IsNode _node);

}
