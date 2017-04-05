/* $Id: GraphLayoutStrategy_RepellingBorders.java,v 1.7 2002/05/23 22:30:15 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.layout;

import java.util.*;
import com.cyc.blue.gui.*;
import java.awt.Color;

/**
 * A graph layout strategy that tries to make the graph fit in the visible area
 * by making nodes repel from the border.  This strategy has not been tested
 * with a graph in a scrollable panel and should probably be deprecated.
 *
 * @author John Jantos
 * @date 2001/08/23
 * @version $Id: GraphLayoutStrategy_RepellingBorders.java,v 1.7 2002/05/23 22:30:15 jantos Exp $
 */

public class GraphLayoutStrategy_RepellingBorders implements IsGraphLayoutStrategy {
  
  public double internalForceMultiplier = 20.0;
  public double forceMultiplier = 0.0;

  public GraphLayoutStrategy_RepellingBorders() {
  }

  public GraphLayoutStrategy_RepellingBorders(double _forceMultiplier) {
    forceMultiplier = _forceMultiplier;
  }

  public Color getColor() { return Color.pink; }

  public void apply(IsRenderableGraph _rgraph) {
    double graphWidthDiv2 = _rgraph.getWidthD()/2;
    double graphHeightDiv2 = _rgraph.getHeightD()/2;
    
    Iterator nodes = _rgraph.visibleNodesIterator();
    while (nodes.hasNext()) {
      IsRenderableNode node = (IsRenderableNode)nodes.next();
      
      double xForce = -forceMultiplier * internalForceMultiplier * Math.pow((node.getCenterXD()-graphWidthDiv2)/graphWidthDiv2,3);
      double yForce = -forceMultiplier * internalForceMultiplier * Math.pow((node.getCenterYD()-graphHeightDiv2)/graphHeightDiv2,3);
      
//        if (xForce > 10.0) { xForce = 10.0; }
//        if (xForce < -10.0) { xForce = -10.0; }
//        if (yForce > 10.0) { yForce = 10.0; }
//        if (yForce < -10.0) { yForce = -10.0; }
//        System.out.println("xForce = " + xForce);
//        System.out.println("yForce = " + yForce);
      node.addForce(xForce, yForce, this);
    }
  }
  
}
