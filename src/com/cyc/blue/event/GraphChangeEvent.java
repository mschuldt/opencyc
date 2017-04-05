/*{{{
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 * @author John Jantos
 * @date 2001/10/23
 *
 * @version $Id: GraphChangeEvent.java,v 1.4 2002/05/23 22:30:14 jantos Exp $
 *
 *****************************************************************
 * Describe the class functionality
 *
 *****************************************************************
 * External interface:
 *
 * list of the public methods
 *
 *****************************************************************
 *}}}
 */

package com.cyc.blue.event;

import java.util.*;
import com.cyc.event.*;
import com.cyc.blue.graph.*;

public class GraphChangeEvent extends BlueApplicationEvent implements IsEvent {
  private IsGraph graph = null;
  private HashSet addedNodes = new HashSet();
  private HashSet addedEdges = new HashSet();
  private HashSet removedNodes = new HashSet();
  private HashSet removedEdges = new HashSet();

  public GraphChangeEvent(IsGraph _graph) {
    setGraph(_graph);
  }

  public void setGraph(IsGraph _graph) {
    graph = _graph;
  }

  public IsGraph getGraph() {
    return graph;
  }

  public void addAddedNode(IsNode _node) { addedNodes.add(_node); }
  public void addAddedEdge(IsEdge _edge) { addedEdges.add(_edge); }
  public void addRemovedNode(IsNode _node) { removedNodes.add(_node); }
  public void addRemovedEdge(IsEdge _edge) { removedEdges.add(_edge); }

  public Iterator addedNodesIterator() { return addedNodes.iterator(); }
  public Iterator addedEdgesIterator() { return addedEdges.iterator(); }
  public Iterator removedNodesIterator() { return removedNodes.iterator(); }
  public Iterator removedEdgesIterator() { return removedEdges.iterator(); }
  
}
