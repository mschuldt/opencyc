/* $Id: GraphLayoutStrategy_RepellingNodes.java,v 1.11 2002/05/23 22:30:15 jantos Exp $
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
 * A graph layout strategy that causes nodes to repel each other.
 *
 * @author John Jantos
 * @date 2001/08/23
 * @version $Id: GraphLayoutStrategy_RepellingNodes.java,v 1.11 2002/05/23 22:30:15 jantos Exp $
 */

public class GraphLayoutStrategy_RepellingNodes implements IsGraphLayoutStrategy {
  
  private double xScale = 1, yScale = 1, xTranslate = 0, yTranslate = 0;

  public double forceMultiplier = 1.0;
  public double forceMultiplierInternal = 11.0;

  public GraphLayoutStrategy_RepellingNodes() {
  }

  public GraphLayoutStrategy_RepellingNodes(double _forceMultiplier) {
    forceMultiplier = _forceMultiplier;
  }

  public Color getColor() { return Color.blue; }

  public void apply(IsRenderableGraph _rgraph) {
    apply1(_rgraph);
  }

  /** adds forces to nodes based on the distance between the nodes using an
   * inverse cubic of the distance between the nodes.
   */
  public void apply1(IsRenderableGraph _rgraph) {
    //getGraphScalings(_rgraph);

    // forces from nodes
    ArrayList nodeArray = new ArrayList(_rgraph.getVisibleNodes());
    
    for (int i = 0; i < nodeArray.size() - 1; i++) {
      IsRenderableNode node1 = (IsRenderableNode)nodeArray.get(i);
      for (int j = i+1; j < nodeArray.size(); j++) {
	IsRenderableNode node2 = (IsRenderableNode)nodeArray.get(j);
	if (node1 != node2) { // for safety
	  double node1CenterX = node1.getCenterXD();
	  double node1CenterY = node1.getCenterYD();
	  double node2CenterX = node2.getCenterXD();
	  double node2CenterY = node2.getCenterYD();
  	  //if ((Math.abs(node2CenterX - node1CenterX) < _rgraph.getWidth()/2) && 
	  //(Math.abs(node2CenterY - node1CenterY) < _rgraph.getHeight()/2)) {
	    double d = Math.sqrt(Math.pow(node1CenterX - node2CenterX, 2) +
				 Math.pow(node1CenterY - node2CenterY, 2));
	    double xForce = Math.pow(xScale,2) *
	      forceMultiplier * 200 * 170 *
	      // (100 + _node.getWidthD() + otherNode.getWidthD()) *
	      (node1CenterX - node2CenterX) / (Math.pow(d, 3) + 1);
	    double yForce = Math.pow(yScale,2) * 
	      forceMultiplier * 200 * 170 *
	      //(250 + _node.getHeightD() + otherNode.getHeightD()) *
	      (node1CenterY - node2CenterY) / (Math.pow(d, 3) + 1);
	    //}
	    //  	  if (xForce > 10.0) { xForce = 10.0; }
	    //  	  if (xForce < -10.0) { xForce = -10.0; }
	    //  	  if (yForce > 10.0) { yForce = 10.0; }
//  	  if (yForce < -10.0) { yForce = -10.0; }
	    node1.addForce(xForce, yForce, this);
	    node2.addForce(-xForce, -yForce, this);
	    //}
  	}
      }
    }
  }

  /** adds forces to nodes that are proportional to the ration between the area
   * of the nodes and the area of the bounding box xontaining both nodes.
   */
  public void apply2(IsRenderableGraph _rgraph) {
    //getGraphScalings(_rgraph);

    // forces from nodes
    Set visibleNodes = _rgraph.getVisibleNodes();
    int numNodes = visibleNodes.size();
    ArrayList nodeArray = new ArrayList(visibleNodes);
//      double[] nodeXForcesArray = new double[numNodes];
//      double[] nodeYForcesArray = new double[numNodes];

    for (int i = 0; i < numNodes - 1; i++) {
      IsRenderableNode node1 = (IsRenderableNode)nodeArray.get(i);
      for (int j = i + 1; j < numNodes; j++) {
	IsRenderableNode node2 = (IsRenderableNode)nodeArray.get(j);
	if (node1 != node2) { // for safety
	  double  node1CenterX = node1.getCenterXD();
	  double  node1CenterY = node1.getCenterYD();
	  double  node2CenterX = node2.getCenterXD();
	  double  node2CenterY = node2.getCenterYD();

	  if (node1CenterX != node2CenterX &&
	      node1CenterY != node2CenterY) { // for safety
	    
	    double  node1MinX = node1.getMinXD();
	    double  node1MinY = node1.getMinYD();
	    double  node1MaxX = node1.getMaxXD();
	    double  node1MaxY = node1.getMaxYD();
	    double  node2MinX = node2.getMinXD();
	    double  node2MinY = node2.getMinYD();
	    double  node2MaxX = node2.getMaxXD();
	    double  node2MaxY = node2.getMaxYD();
	    double  boundingBoxMinX = Math.min(node1MinX, node2MinX);
	    double  boundingBoxMinY = Math.min(node1MinY, node2MinY);
	    double  boundingBoxMaxX = Math.max(node1MaxX, node2MaxX);
	    double  boundingBoxMaxY = Math.max(node1MaxY, node2MaxY);
	    
	    double forceProportion = // area node1 + area node2 / area bounding box
	      (double)(((node1MaxX - node1MinX) * (node1MaxY - node1MinY)) +
		       ((node2MaxX - node2MinX) * (node2MaxY - node2MinY))) /
	      (double)((boundingBoxMaxX - boundingBoxMinX) * (boundingBoxMaxY - boundingBoxMinY));
	  //System.out.println("forceProportion = " + forceProportion);
	    double xForce = 
	      forceMultiplier * forceMultiplierInternal * forceProportion * ((node1CenterX - node2CenterX) / (boundingBoxMaxX - boundingBoxMinX));
	    double yForce = 
	      forceMultiplier * forceMultiplierInternal * forceProportion * ((node1CenterY - node2CenterY) / (boundingBoxMaxY - boundingBoxMinY));
//  	  if (node1CenterX < node2CenterX) {
//  	    xForce = -xForce;
//  	  }
//  	  if (node1CenterY < node2CenterY) {
//  	    yForce = -yForce;
//  	  }
	    //System.out.println("xForce = " + xForce);
	  //System.out.println("yForce = " + yForce);
	    node1.addForce(xForce, yForce, this);
	    node2.addForce(-xForce, -yForce, this);
	    
//  	    nodeXForcesArray[i] += xForce;
//  	    nodeYForcesArray[i] += yForce;
//  	    nodeXForcesArray[j] += -xForce;
//  	    nodeYForcesArray[j] += -yForce;
	  }
  	}
      }
    }
//      for (int i = 0; i < numNodes; i++) {
//        IsRenderableNode node = (IsRenderableNode)nodeArray.get(i);
//        //System.out.println("node.addForce(" + nodeXForcesArray[i] + ", " + nodeYForcesArray[i] + ", " + this);
//        node.addForce(nodeXForcesArray[i], nodeYForcesArray[i], this);
//      }
  }

  /** checks the graph to set a scaling factor.  pretty hacky.
   */
  private void getGraphScalings(IsRenderableGraph _rgraph) {
    double
      xMin = Integer.MAX_VALUE, yMin = Integer.MAX_VALUE, 
      xMax = Integer.MIN_VALUE, yMax = Integer.MIN_VALUE;
    Iterator nodes = _rgraph.visibleNodesIterator();
    while (nodes.hasNext()) {
      IsRenderableNode node = (IsRenderableNode)nodes.next();
      if (node.getMinXD() < xMin) { xMin = node.getMinXD(); }
      if (node.getMaxXD() > xMax) { xMax = node.getMaxXD(); }
      if (node.getMinYD() < yMin) { yMin = node.getMinYD(); }
      if (node.getMaxYD() > yMax) { yMax = node.getMaxYD(); }
    }
    
//        System.out.println("xMax: " + xMax);
//        System.out.println("yMax: " + yMax);
//        System.out.println("xMin: " + xMin);
//        System.out.println("yMin: " + yMin);

    //xMax += PAD; xMin -= PAD;
    //yMax += PAD; yMin -= PAD;
      //    xTranslate = xTranslate * 0.9 + (_rgraph.getMinXD() - xMin) * 0.1;//xTranslate * 0.8 + (xMin) * 0.2;
      //yTranslate = yTranslate * 0.9 + (_rgraph.getMinYD() - yMin) * 0.1;//yTranslate * 0.8 + (yMin) * 0.2;
    xScale = xScale * 0.9 + 
      (_rgraph.getWidthD() / (float)(xMax - xMin)) * 0.1;
    yScale = yScale * 0.9 + 
      (_rgraph.getHeightD() / (float)(yMax - yMin)) * 0.1;
//        System.out.println("graphHeight = " + _rgraph.getHeight());
//        System.out.println("graphWidth = " + _rgraph.getWidth());
//        System.out.println("xWidth: " + (xMax - xMin));
//        System.out.println("yWidth: " + (yMax - yMin));
//        System.out.println("xScale: " + xScale);
//        System.out.println("yScale: " + yScale);
  }


}
