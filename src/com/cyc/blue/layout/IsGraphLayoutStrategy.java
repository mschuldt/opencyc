/* $Id: IsGraphLayoutStrategy.java,v 1.6 2002/05/23 22:30:15 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.layout;

import com.cyc.blue.gui.*;
import java.awt.Color;

/**
 * Implement to obtain the ability to apply a layout strategy to a graph by
 * adding forces to nodes in that graph.
 *
 * @author John Jantos
 * @date 2001/08/23
 * @version $Id: IsGraphLayoutStrategy.java,v 1.6 2002/05/23 22:30:15 jantos Exp $
 */

public interface IsGraphLayoutStrategy {
  
  /** applies one ineration of a strategy to a specified renderable graph.
   * @param _renderableGraph
   */
  public void apply(IsRenderableGraph _renderableGraph);

  /** applies one ineration of a strategy to a specified renderable node of a
   * specified renderable graph.  this method is meant to be used to determine a
   * good initial placement of a node.
   * @param _renderableGraph
   * @param _renderableNode
   */
  //public void apply(IsRenderableGraph _renderableGraph, IsRenderableNode _renderableNode);

  /** gets the color that the force should be rendered by if we draw forces on the node.
   */
  public Color getColor();
  
}
