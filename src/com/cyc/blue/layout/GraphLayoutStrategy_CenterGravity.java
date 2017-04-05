/* $Id: GraphLayoutStrategy_CenterGravity.java,v 1.6 2002/05/23 22:30:15 jantos Exp $
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
 * A graph layout strategy that tries to pull all nodes to the coordinates (0,0)
 * which then becomes the center of the graph.
 *
 * @author John Jantos
 * @date 2001/10/17
 * @version $Id: GraphLayoutStrategy_CenterGravity.java,v 1.6 2002/05/23 22:30:15 jantos Exp $
 */

public class GraphLayoutStrategy_CenterGravity implements IsGraphLayoutStrategy {
  
  private double forceMultiplier = 1.0;
  private double internalForceMultiplier = 0.3;

  public GraphLayoutStrategy_CenterGravity() {
  }

  public GraphLayoutStrategy_CenterGravity(double _forceMultiplier) {
    forceMultiplier = _forceMultiplier;
  }

  public Color getColor() { return Color.green; }

  public void apply(IsRenderableGraph _rgraph) {
    double centerX = 0;
    double centerY = 0;
    
    {
      double minX = Double.MAX_VALUE;
      double maxX = Double.MIN_VALUE;
      double minY = Double.MAX_VALUE;
      double maxY = Double.MIN_VALUE;
      Iterator nodesIterator = _rgraph.visibleNodesIterator();
      if (nodesIterator != null) {
	while (nodesIterator.hasNext()) {
	  IsRenderableNode node = (IsRenderableNode)nodesIterator.next();
	  if (node.getMinYD() < minY) { minY = node.getMinYD(); }
	  if (node.getMaxYD() > maxY) { maxY = node.getMaxYD(); }
	  if (node.getMinXD() < minX) { minX = node.getMinXD(); }
	  if (node.getMaxXD() > maxX) { maxX = node.getMaxXD(); }
	}
      }
      double currCenterX = (maxX - minX) / 2;
      double currCenterY = (maxY - minY) / 2;
      if (minY < 0) {
	//centerY -= minY;
      }
      if (minX < 0) {
	//centerX -= minX;
      }
    }

    Iterator nodes = _rgraph.visibleNodesIterator();
    while (nodes.hasNext()) {
      IsRenderableNode node = (IsRenderableNode)nodes.next();
      
      double nodeX = node.getCenterXD();
      double nodeY = node.getCenterYD();

      double xDistance = centerX - nodeX;
      double yDistance = centerY - nodeY;

      double xSign, ySign;
      if (xDistance > 0) { xSign = 1.0; } else { xSign = -1.0; } 
      if (yDistance > 0) { ySign = 1.0; } else { ySign = -1.0; } 

      double xDistanceFactor = Math.abs(xDistance);
      double yDistanceFactor = Math.abs(yDistance);

      if (xDistanceFactor < 1) { 
	xDistanceFactor = xDistanceFactor * xDistanceFactor;
      } else {
	xDistanceFactor = Math.sqrt(xDistanceFactor);
      }
      if (yDistanceFactor < 1) { 
	yDistanceFactor = yDistanceFactor * yDistanceFactor; 
      } else {
	yDistanceFactor = Math.sqrt(yDistanceFactor);
      }

//        double d = Math.sqrt(Math.pow(xDistance, 2) +
//  			   Math.pow(yDistance, 2));
//        System.out.println("forceMultiplier = " + forceMultiplier);
//        System.out.println("internalForceMultiplier = " + internalForceMultiplier);
//        System.out.println("xSign = " + xSign);
      //        System.out.println("xDistance = " + xDistance);
//        System.out.println("Math.sqrt(Math.abs(xDistance)) = " + Math.sqrt(Math.abs(xDistance)));

      double xForce = forceMultiplier * internalForceMultiplier * xSign * xDistanceFactor;
      double yForce = forceMultiplier * internalForceMultiplier * ySign * yDistanceFactor;
      
      //        System.out.println("xForce = " + xForce);
      //        System.out.println("yForce = " + yForce);
      node.addForce(xForce, yForce, this);
    }
  }


  
}
