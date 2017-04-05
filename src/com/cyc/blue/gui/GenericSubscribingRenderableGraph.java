/* $Id: GenericSubscribingRenderableGraph.java,v 1.2 2002/05/22 01:33:22 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.util.Iterator;
import com.cyc.blue.event.BlueEventService;
import com.cyc.blue.event.EventFilter_AllowGraphChangeEvents;
import com.cyc.blue.event.GraphChangeEvent;
import com.cyc.blue.graph.IsEdge;
import com.cyc.blue.graph.IsGraph;
import com.cyc.blue.graph.IsNode;
import com.cyc.event.IsEvent;
import com.cyc.event.IsEventService;
import com.cyc.event.IsEventServiceSubscriber;
 
/**
 * An extension of a generica renderable graph that adds event service subscriber functionality.
 *
 * @author John Jantos
 * @date 2002/04/10
 * @version $Id: GenericSubscribingRenderableGraph.java,v 1.2 2002/05/22 01:33:22 jantos Exp $
 */

public class GenericSubscribingRenderableGraph extends GenericRenderableGraph implements IsRenderableGraph, IsEventServiceSubscriber {
  private static final boolean DEBUG = false;

  private IsGraph sourceGraph;

  public GenericSubscribingRenderableGraph(IsGraph _sourceGraph, Class _nodeClass, Class _edgeClass) {
    super(_nodeClass, _edgeClass);
    sourceGraph = _sourceGraph;
    // subscribe to events from sourceGraph
    IsEventService blueEventService = BlueEventService.current();
    if (blueEventService != null) { 
      blueEventService.subscribe(GraphChangeEvent.class, 
				 new EventFilter_AllowGraphChangeEvents(sourceGraph),
				 this);
    }
  }

  public GenericSubscribingRenderableGraph(IsGraph _sourceGraph, IsGraphFrame _graphFrame, Class _nodeClass, Class _edgeClass) {
    this(_sourceGraph, _nodeClass, _edgeClass);
    setGraphFrame(_graphFrame);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsEventServiceSubscriber

  private IsEdge instantiateEdge(IsEdge _edge) {
    IsNode head = _edge.getHead();
    IsNode tail = _edge.getTail();
    Object relation = _edge.getRelation(); // no mapping??
    IsNode newHead = instantiateNode(head);
    IsNode newTail = instantiateNode(tail);
    return instantiateEdge(newHead, newTail, relation);
  }

  public void inform(IsEvent _event) {
    if (DEBUG) { System.out.println("-->" + this + ".inform(" + _event + ")"); }
    if (_event instanceof GraphChangeEvent &&
	((GraphChangeEvent)_event).getGraph() == sourceGraph) { // somewhat redundant since we filtered
      // recast
      GraphChangeEvent graphChangeEvent = (GraphChangeEvent)_event;

      Iterator removedEdgesIterator = graphChangeEvent.removedEdgesIterator();
      while (removedEdgesIterator.hasNext()) {
	IsEdge removedEdge = (IsEdge)removedEdgesIterator.next();
	if (removedEdge != null) {
  	  IsEdge newEdge = instantiateEdge(removedEdge);
  	  if (newEdge != null) {
  	    removeEdge(newEdge);
  	  }
	}
      }

      Iterator removedNodesIterator = graphChangeEvent.removedNodesIterator();
      while (removedNodesIterator.hasNext()) {
	IsNode removedNode = (IsNode)removedNodesIterator.next();
	if (removedNode != null) {
  	  IsNode newNode = instantiateNode(removedNode);
  	  if (newNode != null) {
  	    removeNode(newNode);
  	  }
	}
      }

      Iterator addedNodesIterator = graphChangeEvent.addedNodesIterator();
      while (addedNodesIterator.hasNext()) {
	IsNode addedNode = (IsNode)addedNodesIterator.next();
	if (addedNode != null) {
  	  IsNode newNode = instantiateNode(addedNode);
  	  if (newNode != null) {
  	    addNode(newNode);
  	  }
	}
      }
      

      Iterator addedEdgesIterator = graphChangeEvent.addedEdgesIterator();
      while (addedEdgesIterator.hasNext()) {
	IsEdge addedEdge = (IsEdge)addedEdgesIterator.next();
	if (addedEdge != null) {
  	  IsEdge newEdge = instantiateEdge(addedEdge);
  	  if (newEdge != null) {
  	    addEdge(newEdge);
  	  }
	}
      }
      
    }
  }
  
}
