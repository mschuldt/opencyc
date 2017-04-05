/* $Id: CycGraph.java,v 1.14 2002/04/11 17:25:18 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.cyc;

import com.cyc.blue.graph.GenericPublisherGraph;
import com.cyc.blue.event.BlueEventService;

/**
 * A graph consisting of CycNodes and CycEdges.
 *
 * @author John Jantos
 * @date 2002/03/11
 * @version $Id: CycGraph.java,v 1.14 2002/04/11 17:25:18 jantos Exp $
 */

public class CycGraph extends GenericPublisherGraph {
  private static final boolean DEBUG = false;

  public CycGraph() {
    super(CycNode.class, CycEdge.class);
    setEventService(BlueEventService.instantiate());
  }

}
