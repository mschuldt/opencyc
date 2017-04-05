/* $Id: IsGraphLayoutManager.java,v 1.3 2002/05/23 22:30:15 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.layout;

import java.util.*;
import java.awt.*;
import com.cyc.blue.*; // todo: remove
import com.cyc.blue.gui.*;

/**
 * Implement to obtain the ability to manage the layout of a renderable graph.
 *
 * @author John Jantos
 * @date 2002/05/06
 * @version $Id: IsGraphLayoutManager.java,v 1.3 2002/05/23 22:30:15 jantos Exp $
 */

public interface IsGraphLayoutManager {

  /**
   * Add a strategy to the layout manager.
   * @param _graphLayoutStrategy
   */
  public void addStrategy(IsGraphLayoutStrategy _graphLayoutStrategy);

  /**
   * Remove a strategy from the layout manager.
   * @param _graphLayoutStrategy
   */
  public void removeStrategy(IsGraphLayoutStrategy _graphLayoutStrategy);

  /**
   * Get the set of current strategies.
   */
  public Set getStrategies();

  /**
   * Get and iterator over current strategies.
   */
  public Iterator strategiesIterator();
  
  /**
   * Apply the strategies to all the nodes in a graph.
   * @param _renderableGraph
   */
  public void applyStrategies(IsRenderableGraph _renderableGraph);

  /**
   * Apply the strategies to a single node in a graph.
   * @param _renderableGraph
   * @param _renderableNode
   */
  // commented out since the two argument apply method in IsGraphLayoutStrategy is commented out.
  //public void applyStrategies(IsRenderableGraph _renderableGraph, IsRenderableNode _renderableNode);
  
}
