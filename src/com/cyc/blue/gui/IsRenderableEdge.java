/* $Id: IsRenderableEdge.java,v 1.6 2002/05/23 22:30:15 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import com.cyc.blue.graph.IsEdge;

/**
 * Implement to obtain the ability to be used as a graph edge and be renderable.
 * In addition the the methods inherited from IsEdge and IsRenderable
 * interfaces, IsRenderableNode adds two getters for getting the head and tail
 * of the edge as renderable nodes.
 *
 * @author John Jantos
 * @date 2001/08/23
 * @version $Id: IsRenderableEdge.java,v 1.6 2002/05/23 22:30:15 jantos Exp $
 */

public interface IsRenderableEdge extends IsEdge, IsRenderable {
  
  /** sets the renderable graph this node is a part of.
   * @param _renderableGraph
   */
  public void setRenderableGraph(IsRenderableGraph _renderableGraph);

  /** sets the renderable graph this node is a part of.
   * @return the renderable graph.
   */
  public IsRenderableGraph getRenderableGraph();

  /** gets the head of the node (as a renderable node)
   * @return the head (as a renderable node)
   */
  public IsRenderableNode getRenderableHead();

  /** gets the tail of the node (as a renderable node)
   * @return the tail (as a renderable node)
   */
  public IsRenderableNode getRenderableTail();

}
