/* $Id: GenericPublisherGraph.java,v 1.4 2002/05/15 23:27:44 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.graph;

import com.cyc.blue.event.GraphChangeEvent;
import com.cyc.event.IsEvent;
import com.cyc.event.IsEventService;

/**
 * A generic implementation of the IsPublisherGraph interface that publishes node and edge additions and removals.
 *
 * @author John Jantos
 * @date 2002/01/17
 * @version $Id: GenericPublisherGraph.java,v 1.4 2002/05/15 23:27:44 jantos Exp $
 */

public class GenericPublisherGraph extends GenericGraph implements IsPublisherGraph {
  private static final boolean DEBUG = false;
  private GraphChangeEvent graphChangeEvent = null;
  private IsEventService eventService = null;
  private boolean publishingPaused = false;

  public GenericPublisherGraph(Class _nodeClass, Class _edgeClass) {
    super(_nodeClass, _edgeClass);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsPublisher
  
  public void setEventService(IsEventService _eventService) {
    eventService = _eventService;
  }
  public IsEventService getEventService() {
    return eventService;
  }

  public GraphChangeEvent getCurrentGraphChangeEvent() {
    // guaranteed to return valid event
    if (graphChangeEvent == null) {
      graphChangeEvent = new GraphChangeEvent(this);
    }
    return graphChangeEvent;
  }
  public void clearCurrentGraphChangeEvent() {
    graphChangeEvent = null; // do not clear -- active event
  }

  private void pausePublishing() {
    publishingPaused = true;
  }
  private void unpausePublishing() {
    publishingPaused = false;
  }

  private void publish() {
    if (!publishingPaused) {
      if (eventService != null) {
	if (graphChangeEvent != null) {
	  eventService.publish(getCurrentGraphChangeEvent());
	  clearCurrentGraphChangeEvent();
	}
      }
    }
  }

  ////////////////////////////////////////////////////////////////////////////////
  // publishing methods

  public void clear() {
    pausePublishing();
    super.clear();
    unpausePublishing();
    publish();
  }

  public void addNode(IsNode _node) {
    if (DEBUG) { System.out.println(this + ".addNode(" + _node + ")"); }
    super.addNode(_node);
    getCurrentGraphChangeEvent().addAddedNode(_node);
    publish();
  }
  
  public void removeNode(IsNode _node) {
    if (DEBUG) { System.out.println(this + ".removeNode(" + _node + ")"); }
    super.removeNode(_node);
    getCurrentGraphChangeEvent().addRemovedNode(_node);
    publish();
  }
  
  public void addEdge(IsEdge _edge) {
    if (DEBUG) { System.out.println(this + ".addEdge(" + _edge + ")"); }
    super.addEdge(_edge);
    getCurrentGraphChangeEvent().addAddedEdge(_edge);
    publish();
  }

  public void removeEdge(IsEdge _edge) {
    if (DEBUG) { System.out.println(this + ".removeEdge(" + _edge + ")"); }
    super.removeEdge(_edge);
    getCurrentGraphChangeEvent().addRemovedEdge(_edge);
    publish();
  }
  
}
