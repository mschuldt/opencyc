/* $Id: JGraphLayoutManagerThread.java,v 1.13 2002/05/23 22:30:15 jantos Exp $
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
import com.cyc.blue.*; // todo: remove
import com.cyc.blue.gui.*;

/**
 * A thread that implements a graph layout manager.
 *
 * @author John Jantos
 * @date 2001/08/06
 * @version $Id: JGraphLayoutManagerThread.java,v 1.13 2002/05/23 22:30:15 jantos Exp $
 */

public class JGraphLayoutManagerThread extends Thread implements IsGraphLayoutManager, IsPausable {
  private static final boolean DEBUG = false;
  private static double MAX_FORCE = 250;
  private static final boolean SHOW_STATS = false;
  private static final int TARGET_FPS = 60;
  private static final double MAX_CPU_PERCENT = 0.25;
  private double TARGET_CPU_PERCENT = MAX_CPU_PERCENT;
  private static final double MAX_DELAY = 1000;
  private static final double MIN_DELAY = 15;
  private static long lastTickTime = 0;
  private static long lastPrintTime = 0;
  private static long lastGraphRenderTime = 0;
  private static long framesSinceLastPrintTime = 0;
  private static long timeWorkingSinceLastPrintTime = 0;
  private static final int PRINT_INTERVAL_MS = 5000;
  private static final int GRAPH_RENDER_INTERVAL_MS = 1000;

  //private NodeForces lastForces = new NodeForces();
  private IsRenderableGraph rgraph;
  private HashSet strategies;
  
  public JGraphLayoutManagerThread(IsRenderableGraph _rgraph) {
    rgraph = _rgraph;
    strategies = new HashSet();
    setName("JGraphLayoutManagerThread");
  }

  ////////////////////////////////////////////////////////////////////////////////
  // IsGraphLayoutManager

  public void addStrategy(IsGraphLayoutStrategy _strategy) {
    if (DEBUG) System.err.println("JGraphLayoutManagerThread.addStrategy(" + _strategy + ")");
    synchronized (strategies) {
      strategies.add(_strategy);
    }
  }
  public void removeStrategy(IsGraphLayoutStrategy _strategy) {
    if (DEBUG) System.err.println("JGraphLayoutManagerThread.removeStrategy(" + _strategy + ")");
    synchronized (strategies) {
      strategies.remove(_strategy);
    }
  }

  public Set getStrategies() {
    return strategies;
  }

  public Iterator strategiesIterator() {
    if (DEBUG) System.err.println("JGraphLayoutManagerThread.strategiesIterator()");
    return strategies.iterator();
  }
  
  public void applyStrategies(IsRenderableGraph _renderableGraph) {
    Iterator strategiesIterator = strategiesIterator();
    if (strategiesIterator != null) {
      while (strategiesIterator.hasNext()) {
	IsGraphLayoutStrategy strategy = (IsGraphLayoutStrategy)strategiesIterator.next();
	if (strategy != null) {
	  strategy.apply(_renderableGraph);
	  // also applyforces and render?! (this could be done in another thread)
	  Iterator nodesIterator = rgraph.nodesIterator();
	  while (nodesIterator.hasNext()) {
	    IsRenderableNode renderableNode = (IsRenderableNode)nodesIterator.next();
	    renderableNode.applyPendingForces();
	  }
	}
      }
    }
  }
  
  // commented out since the two argument apply method in IsGraphLayoutStrategy is commented out.
//    public void applyStrategies(IsRenderableGraph _renderableGraph, IsRenderableNode _renderableNode) {
//      Iterator strategiesIterator = strategiesIterator();
//      if (strategiesIterator != null) {
//        while (strategiesIterator.hasNext()) {
//  	IsGraphLayoutStrategy strategy = (IsGraphLayoutStrategy)strategiesIterator.next();
//  	if (strategy != null) {
//  	  strategy.apply(_renderableGraph. _renderableNode);
//  	  // also applyforces and render?! (this could be done in another thread)
//  	  _renderableNode.applyPendingForces();
//  	}
//        }
//      }
//    }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsPausable

  private boolean isPaused = false;
  
  public void setPaused(boolean _isPaused) {
    isPaused = _isPaused; //synchronized (this) { notify(); }}
  }
  public boolean isPaused() { return isPaused; }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsRunnable

  public void run() {
    if (DEBUG) { System.err.println("JGraphLayoutManagerThread.run()"); }
    while (true) {
      if (!isPaused()) {
      //System.out.println("running JGraphLayoutManagerThread: " + this);
	  if (lastTickTime == 0) { lastTickTime = new Date().getTime(); }
	  long startCalculationTime = new Date().getTime();
	  long lastCalculationTime = applyForces();
	  //yield();
	  long endCalculationTime = new Date().getTime();
	  //rgraph.render();
	  //sleep(10);
	  //System.out.println("time b/w frame: " + (endCalculationTime - startCalculationTime) + " ms");
	  //rgraph.repaint();
	  // guarantee take up less than TARGET_CPU % of cpu.
	  //long lastCalculationTime = endCalculationTime - startCalculationTime;
	  //System.out.println("lastCalculationTime = " + lastCalculationTime);
	  double delayForTargetFPS = (double)(1000.0/TARGET_FPS) - lastCalculationTime;
	  double delayForCPULimit = (lastCalculationTime / (double)TARGET_CPU_PERCENT) - lastCalculationTime;
	  double delay = Math.round(Math.min(MAX_DELAY,Math.max(delayForTargetFPS, delayForCPULimit)));
	  delay = Math.max(MIN_DELAY,delay);

  	  if (lastGraphRenderTime + GRAPH_RENDER_INTERVAL_MS < endCalculationTime) {
  	    rgraph.render();
  	    lastGraphRenderTime = endCalculationTime;
  	  }

	  if (lastPrintTime + PRINT_INTERVAL_MS < endCalculationTime) {
	    //System.out.println("delayForTargetFPS = " + delayForTargetFPS + ", delayForCPULimit = " + delayForCPULimit);
	    //System.out.println("delay = " + delay);
	    if (SHOW_STATS) {
	      System.out.println("TARGET_CPU_PERCENT = " + TARGET_CPU_PERCENT);
	      System.out.println("fps:\t" + 1000.0 * (double)framesSinceLastPrintTime/(endCalculationTime - lastPrintTime) +
				 "\t\t% cpu:\t" + (double)timeWorkingSinceLastPrintTime/(endCalculationTime - lastPrintTime) * 100.0);
	    }
	    framesSinceLastPrintTime = 0;
	    timeWorkingSinceLastPrintTime = 0;
	    //System.out.println("time b/w print: " + (endCalculationTime - lastPrintTime) + " ms");
	    lastPrintTime = endCalculationTime;
	  } else {
	    framesSinceLastPrintTime++;
	    timeWorkingSinceLastPrintTime += lastCalculationTime;
	  }
	  lastTickTime = endCalculationTime;
	  //System.out.println("JGraphLayoutManagerThread delay = " + delay + " ms");
	  try { sleep((long)delay); } catch (InterruptedException e) {}
//  	  synchronized (this) {
//  	    try { wait(delay); } catch (InterruptedException e) {}
//  	  }
	  //yield();
      } else {
	try { sleep(1000); } catch (InterruptedException e) {}
//  	synchronized (this) {
//  	  try { wait(1000); } catch (InterruptedException e) {}
//  	}
      }
    }
  }

  public synchronized long applyForces() {
    if (DEBUG) System.err.println("JGraphLayoutManagerThread.applyForces()");
    long calculationTime = 0;

    synchronized (rgraph) {
      Iterator strategiesIterator = strategiesIterator();
      while (strategiesIterator.hasNext()) {
	IsGraphLayoutStrategy strategy = (IsGraphLayoutStrategy)strategiesIterator.next();
	long calculationTimeStart = new Date().getTime();
	//System.out.println("--------------------------------------------------------------------------------");
	//System.out.println("STRATEGY = " + strategy);
	strategy.apply(rgraph);
	calculationTime += new Date().getTime() - calculationTimeStart;
	//yield();
      }
      Iterator nodesIterator = rgraph.nodesIterator();
      while (nodesIterator.hasNext()) {
	IsRenderableNode renderableNode = (IsRenderableNode)nodesIterator.next();
	renderableNode.applyPendingForces();
      }
    
    }

    return calculationTime;
  }

    


}




/* 

  public synchronized long applyForces() {
    if (DEBUG) System.err.println("JGraphLayoutManagerThread.applyForces()");
    //System.out.println("applyForcesToAllNodes()");
    long calculationTime = 0;
    // new way
    //NodeForces forces = new NodeForces();

    synchronized (rgraph) {
      Iterator strategiesIterator = strategiesIterator();
      while (strategiesIterator.hasNext()) {
	IsGraphLayoutStrategy strategy = (IsGraphLayoutStrategy)strategiesIterator.next();
	long calculationTimeStart = new Date().getTime();
	//System.out.println("--------------------------------------------------------------------------------");
	//System.out.println("STRATEGY = " + strategy);
	strategy.apply(rgraph);
	calculationTime += new Date().getTime() - calculationTimeStart;
	//yield();
      }
    
      long calculationTimeStart = new Date().getTime();

//        Iterator nodestest = rgraph.visibleNodesIterator();
//        double 
//  	maxXForce = 0.0,
//  	maxYForce = 0.0;
//        int numNodes = 0;
//        while (nodestest.hasNext()) {
//  	numNodes++;
//  	IsRenderableNode node = (IsRenderableNode)nodestest.next();
//  	maxXForce = Math.max(Math.max(maxXForce, forces.getXForce(node)), -forces.getXForce(node));
//  	maxYForce = Math.max(Math.max(maxYForce, forces.getYForce(node)), -forces.getYForce(node));
//        }
//        //System.out.println("maxXForce = " + maxXForce);
//        //System.out.println("maxYForce = " + maxYForce);

//        double xForceAdjust = 1.0;
//        double yForceAdjust = 1.0;
//  ////////////////////////////////////////////////////////////////////////////////
//  //        if (maxXForce > rgraph.getWidth()) {
//  //  	xForceAdjust = rgraph.getWidth() / maxXForce; 
//  //        }
//  //        if (maxYForce > rgraph.getHeight()) {
//  //  	yForceAdjust = rgraph.getHeight() / maxYForce; 
//  //        }
//  ////////////////////////////////////////////////////////////////////////////////

      Iterator nodes = rgraph.nodesIterator();
      //lastForces = new NodeForces();
      while (nodes.hasNext()) {
	IsRenderableNode node = (IsRenderableNode)nodes.next();
	node.validate(); // @todo: possibly unnecessary?
	if (node.isVisible()) {
	  // todo: translate method??
	  
	  ////////////////////////////////////////////////////////////////////////////////
	  // double xForce = forces.getXForce(node) * 0.8 + lastForces.getXForce(node) * 0.2;
	  // double yForce = forces.getYForce(node) * 0.8 + lastForces.getYForce(node) * 0.2;
	  double xForce = forces.getXForce(node);
	  double yForce = forces.getYForce(node);
	  
	  // @todo: below done by node...  this is redundant
	  if (Math.abs(xForce) > MAX_FORCE || Math.abs(yForce) > MAX_FORCE) {
	    if (xForce > MAX_FORCE) { xForce = MAX_FORCE; } else if (xForce < -MAX_FORCE) { xForce = -MAX_FORCE; }
	    if (yForce > MAX_FORCE) { yForce = MAX_FORCE; } else if (yForce < -MAX_FORCE) { yForce = -MAX_FORCE; }
	  }
	  ////////////////////////////////////////////////////////////////////////////////

//  	if (xForce > 20) { xForce = 20; }
//  	if (xForce < -20) { xForce = -20; }
//  	if (yForce > 20) { yForce = 20; }
//  	if (yForce < -20) { yForce = -20; }
	
	double newX = node.getCenterX() + xForce * xForceAdjust;
	double newY = node.getCenterY() + yForce * yForceAdjust;

//  	if (newX > rgraph.getMaxX()) { newX = rgraph.getMaxX(); }
//  	if (newX < rgraph.getMinX()) { newX = rgraph.getMinX(); }
//  	if (newY > rgraph.getMaxY()) { newY = rgraph.getMaxY(); }
//  	if (newY < rgraph.getMinY()) { newY = rgraph.getMinY(); }
	
 	if (!node.isLocked()) {
    	  node.setCenter((int)Math.round(Math.floor(newX)),
    			 (int)Math.round(Math.floor(newY)));
  	}
	lastForces.addForce(node, xForce, yForce);
	}
      }
      calculationTime += new Date().getTime() - calculationTimeStart;
    }


//      {
//        double aFPN = lastForces.averageForcePerNode();
//        if (aFPN > 0.0 && aFPN < 1.0) {
//    	TARGET_CPU_PERCENT = aFPN * MAX_CPU_PERCENT;
//        } else if (aFPN == -1) {
//  	TARGET_CPU_PERCENT = MAX_CPU_PERCENT / 50;
//        } else {
//  	TARGET_CPU_PERCENT = MAX_CPU_PERCENT;
//        }
//      }
    return calculationTime;
    

//      // old way

//      int 
//        xMin = rgraph.getX(),
//        yMin = rgraph.getY(),
//        xMax = xMin + rgraph.getWidth(),
//        yMax = yMin + rgraph.getHeight();


//       nodes = rgraph.nodesIterator();
//      while (nodes.hasNext()) {
//        JNode node = (JNode)nodes.next();
//        applyForcesToNode(node);
//      }
    //scaleGraphToFit();
  }

//    public void applyForcesToNode(JNode _node) {
//      Point forceVector = new Point(0,0);
//      Point nodeCenter = _node.getCenter();
//      int 
//        xMin = rgraph.getX(),
//        yMin = rgraph.getY(),
//        xMax = xMin + rgraph.getWidth(),
//        yMax = yMin + rgraph.getHeight();
    
//      double xForce = 0.0;
//      double yForce = 0.0;






 

//        //System.out.println("xForce = " + xForce);
//        //System.out.println("yForce = " + yForce);
//  //          if (xForce > 30) { xForce = 30; }
//  //          if (xForce < -30) { xForce = -30; }
//  //          if (yForce > 30) { yForce = 30; }
//  //          if (yForce < -30) { yForce = -30; }
//  //          if (xForce > 0 && xForce < 1) { xForce = 0; }
//  //          if (xForce < 0 && xForce > -1) { xForce = 0; }
//  //          if (yForce > 0 && yForce < 1) { yForce = 0; }
//  //          if (yForce < 0 && yForce > -1) { yForce = 0; }
      
//  	int newX = (int)Math.round(nodeCenter.getX() + xForce);
//  	int newY = (int)Math.round(nodeCenter.getY() + yForce);
	
//  //  	if (newX < rgraph.getX()) { newX = rgraph.getX(); }
//  //  	if (newX > rgraph.getWidth()) { newX = rgraph.getWidth(); }
//  //  	if (newX < rgraph.getY()) { newX = rgraph.getY(); }
//  //  	if (newX > rgraph.getHeight()) { newX = rgraph.getHeight(); }

//  	//_node.setCenter(new Point(newX, newY));

//  	_node.setCenter(new Point((int)Math.round(newX),
//  				  (int)Math.round(newY)));
	
//      }
    
  

*/
