/* $Id: GraphLayoutStrategy_MagneticFieldEdges.java,v 1.10 2002/05/23 22:30:15 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.layout;

import java.util.*;
import java.io.*;
import java.awt.*;
import com.cyc.blue.gui.*;
import java.awt.Color;
//import com.cyc.blue.cyc.*; // todo: remove

/**
 * A graph layout strategy that tries to make edges point in a certain
 * direction.  Currently this direction is to the right, but this should come
 * from the edge's relation somehow.
 *
 * @author John Jantos
 * @date 2001/08/23
 * @version $Id: GraphLayoutStrategy_MagneticFieldEdges.java,v 1.10 2002/05/23 22:30:15 jantos Exp $
 */

public class GraphLayoutStrategy_MagneticFieldEdges implements IsGraphLayoutStrategy {
  private static final boolean DEBUG = false;
  private double forceMultiplier = 1.0;
  private double internalForceMultiplier = 5.05;

  public GraphLayoutStrategy_MagneticFieldEdges() {
  }
  public GraphLayoutStrategy_MagneticFieldEdges(double _forceMultiplier) {
    forceMultiplier = _forceMultiplier;
  }

  public Color getColor() { return Color.magenta; }

  public void apply(IsRenderableGraph _rgraph) {
    Iterator edges = _rgraph.visibleEdgesIterator();
    while (edges.hasNext()) {
      IsRenderableEdge edge = (IsRenderableEdge)edges.next();

      // todo: get angle from edge somehow.
      double targetAngle = Math.PI;

      // ATTEMPT 1
//        if (BlueCycAccess.genls.equals(edge.getRelation())) {
//    	targetAngle = Math.PI;
//        } else if (BlueCycAccess.isa.equals(edge.getRelation())) {
//    	targetAngle = Math.PI / 2;
//        } else if (BlueCycAccess.genlMt.equals(edge.getRelation())) {
//    	targetAngle = Math.PI;
//  	//} else if (BlueCycAccess.current().getConstantByName("genlPreds").equals(edge.getRelation())) {
//  	//  targetAngle = Math.PI;
//        } else {
//    	return;
//        }

      // ATTEMPT 2
      //double targetAngle = Math.toRadians(Double.parseDouble((String)edge.getRelation()));

      IsRenderableNode head = (IsRenderableNode)edge.getHead();
      IsRenderableNode tail = (IsRenderableNode)edge.getTail();
      int headIncidentEdgesInCount = _rgraph.incidentEdgesInCount(head);
      int tailIncidentEdgesOutCount = _rgraph.incidentEdgesOutCount(tail);
      double headAttachX = head.getCenterXD();
      double headAttachY = -head.getCenterYD();
      double tailAttachX = tail.getCenterXD();
      double tailAttachY = -tail.getCenterYD();
      double edgeCenterX = (headAttachX + tailAttachX) / 2;
      double edgeCenterY = (headAttachY + tailAttachY) / 2;
      double deltaX = (headAttachX - tailAttachX);
      double deltaY = (headAttachY - tailAttachY);
      if (deltaX == 0.0) { deltaX = Double.MIN_VALUE; }
      if (DEBUG) { System.out.println("deltaX: " + deltaX); }
      if (DEBUG) { System.out.println("deltaY: " + deltaY); }
      if (DEBUG) { System.out.println("targetAngle: " + targetAngle); }

      double edgeAngle = Math.atan2(deltaY, deltaX);
      if (DEBUG) { System.out.println("edgeAngle: " + edgeAngle); }
      if (edgeAngle < -Math.PI) { edgeAngle += 2*Math.PI; }
      else if (edgeAngle > Math.PI) { edgeAngle -= 2*Math.PI; }
      if (DEBUG) { System.out.println("edgeAngle: " + edgeAngle); }

      double differenceAngle = targetAngle - edgeAngle;
      if (differenceAngle < -Math.PI) { differenceAngle += 2*Math.PI; }
      else if (differenceAngle > Math.PI) { differenceAngle -= 2*Math.PI; }
      if (DEBUG) { System.out.println("differenceAngle: " + differenceAngle); }

      double forceAngle = edgeAngle + differenceAngle / 5;
      if (DEBUG) { System.out.println("forceAngle: " + forceAngle); }

      double d = Math.sqrt(Math.pow(headAttachX - tailAttachX, 2) +
			   Math.pow(headAttachY - tailAttachY, 2));
      if (DEBUG) { System.out.println("d: " + d); }
      // todo: cache node attach and edge lengths in renderable node and edge classes

      double forceAngleX = d * Math.cos(forceAngle) / 2;
      double forceAngleY = d * Math.sin(forceAngle) / 2;
      if (DEBUG) { System.out.println("forceAngleX: " + forceAngleX); }
      if (DEBUG) { System.out.println("forceAngleY: " + forceAngleY); }

      double deltaForceX = (forceAngleX - deltaX / 2);
      double deltaForceY = (forceAngleY - deltaY / 2);
      if (DEBUG) { System.out.println("deltaForceX: " + deltaForceX); }
      if (DEBUG) { System.out.println("deltaForceY: " + deltaForceY); }

      if (DEBUG) { System.out.println("GraphLayoutStrategy_MagneticFieldEdges force: (" + deltaForceX + ", " + deltaForceY + ")"); }
      if (headIncidentEdgesInCount <= 0) {
	//System.out.println("AAA");
	headIncidentEdgesInCount = 1;
      }
      head.addForce(deltaForceX / headIncidentEdgesInCount, -deltaForceY / headIncidentEdgesInCount, this);
      if (tailIncidentEdgesOutCount <= 0) {
	//System.out.println("YYY");
	tailIncidentEdgesOutCount = 1;
      }
      tail.addForce(-deltaForceX / tailIncidentEdgesOutCount, deltaForceY / tailIncidentEdgesOutCount, this);
    }

  }
}
