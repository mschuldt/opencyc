/* $Id: GraphLayoutStrategy_SpringEdges.java,v 1.11 2002/05/23 22:30:15 jantos Exp $
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
import java.awt.geom.*;
import com.cyc.blue.gui.*;
import java.awt.Color;

/**
 * A graph layout strategy that causes edges to either pull or push the head and
 * tail nodes based on the length of the edge.
 *
 * @author John Jantos
 * @date 2001/08/23
 * @version $Id: GraphLayoutStrategy_SpringEdges.java,v 1.11 2002/05/23 22:30:15 jantos Exp $
 */

public class GraphLayoutStrategy_SpringEdges implements IsGraphLayoutStrategy {
  
  public double forceMultiplier = 1.0;
  public double forceMultiplierInternal = 0.55;
  public double forceMultiplierInternal2 = 0.0035;
  public double springLength = 90.0;
  public static final int NODE_WEIGHT = 0;

  public GraphLayoutStrategy_SpringEdges() {
  }

  public GraphLayoutStrategy_SpringEdges(double _forceMultiplier) {
    forceMultiplier = _forceMultiplier;
  }

  public void apply(IsRenderableGraph _rgraph) {
    apply4(_rgraph);
  }

  public Color getColor() { return Color.red; }

  public void apply1(IsRenderableGraph _rgraph) {

    // forces from edges
    Iterator edges = _rgraph.visibleEdgesIterator();
    while (edges.hasNext()) {
      IsRenderableEdge edge = (IsRenderableEdge)edges.next();
      IsRenderableNode head = (IsRenderableNode)edge.getHead();
      IsRenderableNode tail = (IsRenderableNode)edge.getTail();
      Point2D.Double headAttach = head.getAttachmentPoint(tail);
      Point2D.Double tailAttach = tail.getAttachmentPoint(head);
      double headAttachX = headAttach.getX();
      double headAttachY = -headAttach.getY();
      double tailAttachX = tailAttach.getX();
      double tailAttachY = -tailAttach.getY();
      double headCenterX = head.getCenterXD();
      double headCenterY = head.getCenterYD();
      double tailCenterX = tail.getCenterXD();
      double tailCenterY = tail.getCenterYD();
      double d = Math.sqrt(Math.pow(headAttachX - tailAttachX, 2) +
			   Math.pow(headAttachY - tailAttachY, 2));
      double edgeWeight = 0;
      if (edge.getGraph() != null) {
	edgeWeight = 50 * Math.min(edge.getGraph().incidentEdgesCount(head),
				  edge.getGraph().incidentEdgesCount(tail));
	edgeWeight = Math.min(1000, edgeWeight);
      }

      double xForce = forceMultiplier * forceMultiplierInternal * (d - springLength + edgeWeight) * (headAttachX - tailAttachX) / d;
      double yForce = forceMultiplier * forceMultiplierInternal * (d - springLength + edgeWeight) * (headAttachY - tailAttachY) / d;
//        System.out.println("forceMultiplier: " + forceMultiplier + "\n" +
//    			 "springLength: " + springLength + "\n" +
//    			 "d: " + d + "\n" +
//    			 "headAttachX - tailAttachX: " + (headAttachX - tailAttachX) + "\n" +
//    			 "headAttachY - tailAttachY: " + (headAttachY - tailAttachY));
//        System.out.println("x = " + xForce + ", y = " + yForce);
      head.addForce(-xForce, yForce, this);
      tail.addForce(xForce, -yForce, this);
    }
  }

  /** Don't make huge forces on nodes with a lot of incident edges */
  public void apply2(IsRenderableGraph _rgraph) {
    Iterator nodes = _rgraph.visibleNodesIterator();
    while (nodes.hasNext()) {
      IsRenderableNode node = (IsRenderableNode)nodes.next();   
      double nodeCenterX = node.getCenterXD();
      double nodeCenterY = node.getCenterYD();
      double nodeTargetCenterX = 0.0;
      double nodeTargetCenterY = 0.0;
      double targetMultiple = 0.0;
      nodeTargetCenterX += nodeCenterX; // * NODE_WEIGHT;
      nodeTargetCenterY += nodeCenterY; // * NODE_WEIGHT;
      targetMultiple += NODE_WEIGHT;
      Iterator incidentEdgesIterator = _rgraph.visibleIncidentEdgesIterator(node);
      IsRenderableEdge incidentEdge = null;
      if (incidentEdgesIterator != null) {
	while (incidentEdgesIterator.hasNext()) {
	  incidentEdge = (IsRenderableEdge)incidentEdgesIterator.next();
	  if (incidentEdge.getHead() == node) {
	    nodeTargetCenterX += ((IsRenderableNode)incidentEdge.getTail()).getCenterXD();
	    nodeTargetCenterY += ((IsRenderableNode)incidentEdge.getTail()).getCenterYD();
	    targetMultiple += 1.0;
	  } else {
	    nodeTargetCenterX += ((IsRenderableNode)incidentEdge.getHead()).getCenterXD();
	    nodeTargetCenterY += ((IsRenderableNode)incidentEdge.getHead()).getCenterYD();
	    targetMultiple += 1.0;
	  }
	}
//  	System.out.println("target for (x,y) = (" + nodeCenterX + "," + nodeCenterY + " )\n" +
//  			   "              is = (" + nodeTargetCenterX / targetMultiple + "," +
//  			   nodeTargetCenterY / targetMultiple + ")");

	if (targetMultiple > 0.0 + NODE_WEIGHT) {
  	  node.addForce( 
			forceMultiplier * forceMultiplierInternal2 * (nodeTargetCenterX / targetMultiple - nodeCenterX),
			forceMultiplier * forceMultiplierInternal2 * (nodeTargetCenterY / targetMultiple - nodeCenterY),
			this);      
	}
      }
    }
  }

  public void apply3(IsRenderableGraph _rgraph) {

    // forces from edges
    Iterator edges = _rgraph.visibleEdgesIterator();
    while (edges.hasNext()) {
      IsRenderableEdge edge = (IsRenderableEdge)edges.next();
      IsRenderableNode node1 = (IsRenderableNode)edge.getHead();
      IsRenderableNode node2 = (IsRenderableNode)edge.getTail();
      Point2D.Double node1Point = node1.getAttachmentPoint(node2);
      Point2D.Double node2Point = node2.getAttachmentPoint(node1);
      double node1X = node1Point.getX();
      double node1Y = node1Point.getY();
      double node2X = node2Point.getX();
      double node2Y = node2Point.getY();
      double d = Math.sqrt(Math.pow(node2X - node1X, 2) +
			   Math.pow(node2Y - node1Y, 2));
//        System.out.println("forceMultiplier: " + forceMultiplier + "\t" +
//  			 "springLength: " + springLength + "\t" +
//  			 "d: " + d + "\t" +
//  			 "node1X - node2X: " + (node1X - node2X));
      double xForce = forceMultiplier * (d - springLength) * (node2X - node1X + 2) / d;
      double yForce = forceMultiplier * (d - springLength) * (node2Y - node1Y + 2) / d;
      //System.out.println("x = " + xForce + ", y = " + yForce);
      node1.addForce(xForce, yForce, this);
      node2.addForce(-xForce, -yForce, this);
    }
  }

  
  public void apply4(IsRenderableGraph _rgraph) {
    Iterator nodesIterator = _rgraph.visibleNodesIterator();
    if (nodesIterator != null) {
      while (nodesIterator.hasNext()) {
	IsRenderableNode thisNode = (IsRenderableNode)nodesIterator.next();   
	Iterator incidentEdgesIterator = _rgraph.visibleIncidentEdgesIterator(thisNode);
	if (incidentEdgesIterator != null) {
	  double numEdges = 0;
	  double xForce = 0;
	  double yForce = 0;
	  while (incidentEdgesIterator.hasNext()) {
	    numEdges++;
	    IsRenderableEdge incidentEdge = (IsRenderableEdge)incidentEdgesIterator.next();
	    IsRenderableNode otherNode = (IsRenderableNode)incidentEdge.getOtherNode(thisNode);
	    Point2D.Double thisNodeAttach = thisNode.getLastAttachmentPoint(otherNode);
	    Point2D.Double otherNodeAttach = otherNode.getLastAttachmentPoint(thisNode);
	    double thisNodeAttachX = thisNodeAttach.getX();
	    double thisNodeAttachY = -thisNodeAttach.getY();
	    double otherNodeAttachX = otherNodeAttach.getX();
	    double otherNodeAttachY = -otherNodeAttach.getY();
	    double thisNodeCenterX = thisNode.getCenterXD();
	    double thisNodeCenterY = thisNode.getCenterYD();
	    double otherNodeCenterX = otherNode.getCenterXD();
	    double otherNodeCenterY = otherNode.getCenterYD();
	    double d = Math.sqrt(Math.pow(thisNodeAttachX - otherNodeAttachX, 2) +
				 Math.pow(thisNodeAttachY - otherNodeAttachY, 2));
	    xForce += forceMultiplier * forceMultiplierInternal * (d - springLength) * (thisNodeAttachX - otherNodeAttachX) / d;
	    yForce += forceMultiplier * forceMultiplierInternal * (d - springLength) * (thisNodeAttachY - otherNodeAttachY) / d;
	  }
	  xForce /= numEdges;
	  yForce /= numEdges;
	  thisNode.addForce(-xForce, yForce, this);
	}
      }
    }
  }

}
