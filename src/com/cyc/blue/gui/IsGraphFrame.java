/* $Id: IsGraphFrame.java,v 1.9 2002/05/23 22:30:15 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import com.cyc.blue.command.IsCommandable;
import com.cyc.blue.graph.IsGraph;

/**
 * Implement to obtain the ability to be used as a graph frame.  A graph frame
 * includes a renderable graph and a status bar.
 *
 * @author John Jantos
 * @date 2001/10/18
 * @version $Id: IsGraphFrame.java,v 1.9 2002/05/23 22:30:15 jantos Exp $
 */

public interface IsGraphFrame extends IsApplicationInternalFrame, IsCommandable {

  /** sets the core graph represented in this graph frame.
   * @param _graph
   */
  public void setGraph(IsGraph _graph);

  /** gets the core graph represented in this graph frame.
   * @return the graph
   */
  public IsGraph getGraph();

  /** sets the renderable graph representing the core graph that should be
   * displayed in this graph frame.  This graph is usually created and kept in
   * sync from the core graph.
   * @param _renderableGraph
   */
  public void setRenderableGraph(IsRenderableGraph _renderableGraph);

  /** gets the renderable graph representing the core graph that should be
   * displayed in this graph frame.  This graph is usually created and kept in
   * sync from the core graph.
   * @return the renderable graph
   */
  public IsRenderableGraph getRenderableGraph();

  /** sets the status bar for this graph frame.
   * @param _graphStatusBar
   */
  public void setGraphStatusBar(IsGraphStatusBar _graphStatusBar);

  /** gets the status bar for this graph frame.
   * @return the status bar
   */
  public IsGraphStatusBar getGraphStatusBar();

}
