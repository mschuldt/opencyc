/* $Id: EventFilter_AllowGraphCommandEvents.java,v 1.2 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.event;

import com.cyc.event.*;
import com.cyc.blue.graph.*;

/**
 * The event filter that allows all GraphCommandEvents from a specified graph.
 *
 * @author John Jantos
 * @date 2001/10/23
 * @version $Revision: 1.2 $ $Date: 2002/05/23 22:30:14 $
 */

public class EventFilter_AllowGraphCommandEvents implements IsEventFilter {
  private static final boolean DEBUG = false;
  private IsGraph graph;

  public EventFilter_AllowGraphCommandEvents(IsGraph _graph) {
    graph = _graph;
  }

  /**
   * Return true for any GraphCommandEvent from graph.
   *
   * @return <code>true</code> iff _event instanceof GraphCommandEvent && from specified graph
   *     o/w <code>false</code>
   */
  public boolean apply(IsEvent _event) {
    if (DEBUG) { System.out.println(this + "apply(" + _event + ")"); }
    if (DEBUG) { System.out.println("mygraph = " + graph); }
    if (DEBUG) { System.out.println("thegraph = " + ((GraphCommandEvent)_event).getGraph()); }
    if (_event instanceof GraphCommandEvent &&
	((GraphCommandEvent)_event).getGraph() == graph) {
      if (DEBUG) { System.out.println("--> apply returning true"); }
      return true;
    } else {
      if (DEBUG) { System.out.println("--> apply returning false"); }
      return false;
    }
  }
  
}
