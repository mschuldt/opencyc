/* $Id: GenericRenderableNode.java,v 1.7 2002/05/22 01:33:22 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.Component;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.cyc.blue.graph.GenericNode;
import com.cyc.blue.graph.IsGraph;
import com.cyc.blue.graph.IsNode;
import com.cyc.blue.layout.IsGraphLayoutStrategy;

/**
 * A node implementation that extends a generic node to also be a renderable node.
 *
 * @author John Jantos
 * @date 2002/02/07
 * @version $Id: GenericRenderableNode.java,v 1.7 2002/05/22 01:33:22 jantos Exp $
 */

public class GenericRenderableNode extends GenericNode implements IsRenderableNode {
  private static final boolean DEBUG = false;

  private Component nodeComponent;
  private IsRenderer nodeRenderer;

  private double inhibition = 0.0;
  private boolean isVisible = false;
  private boolean isLocked = false;
  private boolean isSelected = false;
  private HashMap lastAttachmentPointMap = new HashMap();

  private Map xForcesByStrategy = Collections.synchronizedMap(new HashMap());
  private Map yForcesByStrategy = Collections.synchronizedMap(new HashMap());
  private Map xForcesByStrategyLast = Collections.synchronizedMap(new HashMap());
  private Map yForcesByStrategyLast = Collections.synchronizedMap(new HashMap());
  
  private double x = 1, y = 1;
  private double width = 1, height = 1;

  private boolean isInitializing = true;
  
  public GenericRenderableNode(Object _core) {
    super(_core);
    setComponent(new JNodePanel(this));
  }

  public String toString() {
    return "GenericNode[" + getCore() + "]";
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsNode (inherited from GenericNode)

//    public static IsNode instantiate(Object _core) {
//      IsNode found = findNode(_core);
//      if (found != null) {
//        return found;
//      } else {
//        return new GenericRenderableNode(_core);
//      }
//    }

//    public static IsNode findNode(Object _object) {
//      GenericRenderableNode foundNode;
//      synchronized (nodesByCores) {
//        foundNode = (GenericRenderableNode)nodesByCores.get(_object);
//      }
//      return foundNode;
//    }

  public boolean isFocus() { 
    if (getCore() instanceof IsNode) { 
      return ((IsNode)getCore()).isFocus();
    } else {
      return false;
    }
  }
  public void setFocus(boolean _isFocus) { }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsRenderable
 
  public void setComponent(Component _nodeComponent) { nodeComponent = _nodeComponent; }
  public Component getComponent() { return nodeComponent; }

  public void setRenderer(IsRenderer _renderer) { nodeRenderer = _renderer; }
  public IsRenderer getRenderer() { return nodeRenderer; }
//    public void setRenderer(IsRenderer _renderer) {
//      if (DEBUG) { System.out.println(this + ".setRenderer(" + _renderer + ")"); }
//      if (_renderer instanceof IsNodeRenderer) {
//        nodeRenderer = (IsNodeRenderer)_renderer;
//      } else {
//        System.err.println("ERROR " + this + ".setRenderer(" + _renderer + "): instanceof IsNodeRenderer required.");
//      }
//    }
//    public IsNodeRenderer getNodeRenderer() { return nodeRenderer; }
 
  public void setVisible(boolean _isVisible) { isVisible = _isVisible; }
  public boolean isVisible() { return isVisible; }
  
  public double getXD() { 
    if (DEBUG) { System.out.println("--> " + this + ".getXD()"); }
    if (DEBUG) { System.out.println("--- " + this + ".getXD() returning " + x); }
    return x;
  }
  public double getYD() {     
    return y; 
  }
  
  public double getWidthD() { return width; }
  public double getHeightD() { return height; }

  public double getNormalizedXD() {
    double normalizedXD;
    if (getRenderableGraph() != null) {
      normalizedXD = getXD() + getRenderableGraph().getNormalizedXD();
    } else {
      normalizedXD = getXD();
    }
    //System.out.println("GenericRenderableNode[" + this + "].getNormalizedXD() = " + normalizedXD + " = " + getXD() + " - " + getRenderableGraph().getXD());
    return normalizedXD;
  }
  public double getNormalizedYD() {
    if (getRenderableGraph() != null) {
      return getYD() + getRenderableGraph().getNormalizedYD();
    } else {
      return getYD();
    }
  }
  public double getNormalizedWidthD() {
    // account for zoom?
    return getWidthD();
  }
  public double getNormalizedHeightD() {
    // account for zoom?
    return getHeightD();
  }

  public void setXYD(double _x, double _y) { // todo: overloads swing method so should go away
    //System.err.println("--> " + this + ".setXYD(" + _x + ", " + _y + ")");
    x = _x; y = _y;
    render();
  }
  public void setWidthHeightD(double _width, double _height) {
    //if (DEBUG) { System.err.println("--> " + this + ".setWidthHeightD(" + _width + ", " + _height + ")"); }
    width = _width; height = _height;
    // do this instead of render since can't fully subclass AWT components and override getWidth(),.
    getComponent().setSize((int)Math.round(getNormalizedWidthD()),	
			   (int)Math.round(getNormalizedHeightD()));
  }

  // AAAAAAAAAAA
//  //    private void validateIncidentEdges() {
//  //      if (getGraph() != null) {
//  //        Iterator edges = getGraph().incidentEdgesIterator(this);
//  //        if (edges != null) {
//  //  	while (edges.hasNext()) {
//  //  	  IsRenderableEdge redge = (IsRenderableEdge)edges.next();
//  //  	  if (isVisible() == true) { 
//  //  	    if (redge.isVisible() == false) {
//  //  	      IsRenderableNode otherNode = (IsRenderableNode)redge.getOtherNode(this);
//  //  	      if (otherNode.isVisible() == true) {
//  //  		redge.setVisible(true); 
//  //  	      }
//  //  	    }
//  //  	  } else { // isVisible == false
//  //  	    if (redge.isVisible() == true) { redge.setVisible(false); }
//  //  	  }
//  //  	}
//  //        }
//  //      } else {
//  //  //        if (DEBUG) {
//  //  //  	Thread.currentThread().dumpStack();
//  //  //  	System.err.println(this + ".repaintIncidentEdges(): getGraph() returned null");
//  //  //        }
//  //      }
//  //    }

//    public void validate() { 
//      getComponent().invalidate();
//      getComponent().validate();
//      validateIncidentEdges();
//    }


//    public void repaint() {
//      validate();
//      getComponent().repaint();
//    }

//    public void repaint(int _ms) {
//      validate();
//      getComponent().repaint(100);
//    }

  public void render() { 
    //if (DEBUG) { System.out.println("--> " + this + ".render()"); }
    //if (DEBUG) { System.out.println("--- getComponent() = " + getComponent()); }
//      getComponent().invalidate();
//      getComponent().validate();
    getRenderer().render(this);
//      repaint();
//      renderIncidentEdges();
  }

//    public void render(int _ms) { 
//  //      getComponent().invalidate();
//  //      getComponent().validate();
//      getRenderer().render(this,_ms);
//  //  //      applyPendingForces(); 
//  //  //      repaint(100);
//  //  //      renderIncidentEdges();
  //  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsRenderableNode

  public void setRenderableGraph(IsRenderableGraph _renderableGraph) {
    setGraph(_renderableGraph);
  }

  public IsRenderableGraph getRenderableGraph() {
    if (getGraph() instanceof IsRenderableGraph) {
      return (IsRenderableGraph)getGraph();
    } else {
      return null;
    }
  }

  private void setXD(double _x) {
    setXYD(_x, getYD());
  }
  private void setYD(double _y) {
    setXYD(getXD(), _y);
  }
  
  public void setCenterD(double _centerX, double _centerY) {
    setCenterXD(_centerX);
    setCenterYD(_centerY);
  }
  
  public void setCenterXD(double _centerX) {
    setXD(_centerX - getWidthD()/2);
  }
  public void setCenterYD(double _centerY) {
    setYD(_centerY - getHeightD()/2);
  }

  public double getCenterXD() { return getXD() + getWidthD()/2; }
  public double getCenterYD() { return getYD() + getHeightD()/2; }

  public double getMinXD() { return getXD(); }
  public double getMinYD() { return getYD(); }

  public double getMaxXD() { return getXD() + getWidthD(); }
  public double getMaxYD() { return getYD() + getHeightD(); }

  private void setLastAttachmentPoint(IsRenderableNode _node, Point2D.Double _point) {
    lastAttachmentPointMap.put(_node, _point);
  }

  public Point2D.Double getLastAttachmentPoint(IsRenderableNode _node) {
    Point2D.Double lastPoint = (Point2D.Double)lastAttachmentPointMap.get(_node);
    if (lastPoint == null) {
      lastPoint = new Point2D.Double(getCenterXD(), getCenterYD());
    }
    return lastPoint;
  }

  public Point2D.Double getAttachmentPoint(IsRenderableNode _rnode) {
    // see JNode.getAttachmentPoint!
    //find angle
    double thisWidth = getNormalizedWidthD();
    double thisHeight = getNormalizedHeightD();
    double otherWidth = _rnode.getNormalizedWidthD();
    double otherHeight = _rnode.getNormalizedHeightD();
    double thisX, thisY, otherX, otherY;
    Point2D.Double thisLastAttachmentPoint = getLastAttachmentPoint(_rnode);
    Point2D.Double otherLastAttachmentPoint = _rnode.getLastAttachmentPoint(this);
    double centerX = getNormalizedXD() + getNormalizedWidthD() / 2;
    double centerY = getNormalizedYD() + getNormalizedHeightD() / 2;
    thisX = centerX;
    thisY = centerY;
    if (thisLastAttachmentPoint != null) {
      thisX = thisLastAttachmentPoint.getX();
      thisY = thisLastAttachmentPoint.getY();
    } else {
      thisX = centerX;
      thisY = centerY;
    }
    double otherCenterX = _rnode.getNormalizedXD() + _rnode.getNormalizedWidthD() / 2;
    double otherCenterY = _rnode.getNormalizedYD() + _rnode.getNormalizedHeightD() / 2;
    if (otherLastAttachmentPoint != null) {
      otherX = otherLastAttachmentPoint.getX();
      otherY = otherLastAttachmentPoint.getY();
    } else {
      otherX = otherCenterX;
      otherY = otherCenterY;
    }

    double angle;
    angle = Math.atan2((centerY - otherCenterY),
		       (centerX - otherCenterX));
    double diagonal = Math.atan2(otherHeight,otherWidth);
    int yToggle = (centerY < otherCenterY) ? 1 : -1;
    int xToggle = (centerX < otherCenterX) ? 1 : -1;
    double absAngle = Math.abs(angle);
    double x,y;
    double adjustedHeight = Math.min(thisHeight, yToggle * 2 * (otherCenterY - centerY));
    double adjustedWidth = Math.min(thisWidth, xToggle * 2 * (otherCenterX - centerX));
    // adjust w/ edgelength in consideration.
//      double twoD = 2 * Math.sqrt(Math.pow(thisX - otherX, 2) +
//  			      Math.pow(thisY - otherY, 2));
    //    adjustedHeight = Math.min(adjustedHeight, twoD);
    //adjustedWidth = Math.min(adjustedWidth, twoD);
    if (absAngle < diagonal || absAngle > Math.PI - diagonal) {
      double adjustedAngle = angle;
      if (adjustedAngle > diagonal) {
	adjustedAngle = Math.PI - adjustedAngle;
      } else if (adjustedAngle < -diagonal) {
	adjustedAngle = -Math.PI - adjustedAngle;
      }
      x = centerX + xToggle * thisWidth / 2;
      y = centerY - (adjustedHeight / 2) * adjustedAngle / diagonal;
    } else {
      x = centerX + (adjustedWidth / 2) * (2 * absAngle - Math.PI) / (Math.PI - 2 * diagonal);
      y = centerY + yToggle * thisHeight / 2;
    }
    Point2D.Double result = new Point2D.Double(x, y); 
    setLastAttachmentPoint(_rnode, result);
    return result;
  }

  // layout

  private static final double MAX_FORCE = 25;

  public Iterator getLastForceStrategiesIterator() {
    return xForcesByStrategyLast.keySet().iterator();
  }
  public double getLastXForceByStrategy(IsGraphLayoutStrategy _strategy) {
    Double xForce = (Double)xForcesByStrategyLast.get(_strategy);
    if (xForce != null) {
      return xForce.doubleValue();
    } else {
      return 0;
    }
  }
  public double getLastYForceByStrategy(IsGraphLayoutStrategy _strategy) {
    Double yForce = (Double)yForcesByStrategyLast.get(_strategy);
    if (yForce != null) {
      return yForce.doubleValue();
    } else {
      return 0;
    }
  }
  
  public void addForce(double _dx, double _dy, IsGraphLayoutStrategy _strategy) {
    if (DEBUG) { System.err.println("--> " + this + ".addForce(" + _dx + ", " + _dy + ", " + _strategy + ")"); }
    if (_dx == 0 && _dy == 0) { 
      return; 
    }
    if (Math.abs(_dx) > MAX_FORCE || Math.abs(_dy) > MAX_FORCE) {
      //System.err.println("--> " + this + ".addForce(" + _dx + ", " + _dy + ", " + _strategy + ") UNREASONABLE AND BEING CLIPPED.");
      if (_dx > MAX_FORCE) { _dx = MAX_FORCE; } else if (_dx < -MAX_FORCE) { _dx = -MAX_FORCE; }
      if (_dy > MAX_FORCE) { _dy = MAX_FORCE; } else if (_dy < -MAX_FORCE) { _dy = -MAX_FORCE; }
    }
    if (_dx == Double.NaN || _dy == Double.NaN) {
      //System.err.println("--> " + this + ".addForce(" + _dx + ", " + _dy + ", " + _strategy + ") NAN AND BEING THROWN OUT.");
      return;
    }
    
    Double currentX = (Double)xForcesByStrategy.get(_strategy);
    if (currentX != null) {
      xForcesByStrategy.put(_strategy, new Double(currentX.doubleValue() + _dx));
    } else {
      xForcesByStrategy.put(_strategy, new Double(_dx));
    }
    Double currentY = (Double)yForcesByStrategy.get(_strategy);
    if (currentY != null) {
      yForcesByStrategy.put(_strategy, new Double(currentY.doubleValue() + _dy));
    } else {
      yForcesByStrategy.put(_strategy, new Double(_dy));
    }
  }

  public void applyPendingForces() {
    //System.out.println("--> " + this + ".applyPendingForces()");
    double totalDx = 0.0, totalDy = 0.0;
    if (!isLocked()) {
      Set strategies = xForcesByStrategy.keySet();
      if (strategies != null) {
	try {
	  Iterator strategiesIterator = strategies.iterator();
	  if (strategiesIterator != null) {
	    while (strategiesIterator.hasNext()) {
	      IsGraphLayoutStrategy strategy = (IsGraphLayoutStrategy)strategiesIterator.next();
	      Double dx = (Double)xForcesByStrategy.get(strategy);
	      Double dy = (Double)yForcesByStrategy.get(strategy);
	      //System.out.println("--- applyPendingForces(): moving " + this + " (" + dx + "," + dy + ") " + strategy);
	      if (dx != null && !dx.isNaN()) { totalDx += dx.doubleValue(); }
	      if (dy != null && !dx.isNaN()) { totalDy += dy.doubleValue(); }
	    }
	    {
	      // set inhibition
	      double totalDxFactor = Math.abs(totalDx);
	      double totalDyFactor = Math.abs(totalDy);
		if (isInitializing == true) {
		  isInitializing = false;
		  getComponent().setVisible(true);
		  setVisible(true);
		}
	      if (totalDxFactor > 0 && totalDxFactor < 2 &&
		  totalDyFactor > 0 && totalDyFactor < 2) {
		inhibition += 4 - (totalDxFactor + totalDyFactor);
	      } else {
		inhibition = 0;
	      }
	      if (inhibition > 500) {
		setLocked(true);
	      }
	    }
	    totalDx /= (inhibition + 1.0);
	    totalDy /= (inhibition + 1.0);
	    //  	  System.out.println(this + " totalDx=" + totalDx);
	    //  	  System.out.println(this + " totalDy=" + totalDy);
	    //      dx = dx += totalDx;
	    //      dy = dy += totalDy;
  	    setXYD(getXD() + totalDx, getYD() + totalDy);
	  }
	} catch (ConcurrentModificationException e) {
	}
      }
    }
    xForcesByStrategyLast = xForcesByStrategy;
    yForcesByStrategyLast = yForcesByStrategy;
    xForcesByStrategy = Collections.synchronizedMap(new HashMap());
    yForcesByStrategy = Collections.synchronizedMap(new HashMap());
  }

  public void setForceInhibition(double _inhibition) { 
    inhibition = Math.abs(_inhibition); // insurance 
  }

  public double getForceInhibition() { return inhibition; }

  // status

  public void setLocked(boolean _isLocked) { isLocked = _isLocked; }
  public boolean isLocked() { return isLocked; }

  public boolean isSelected() { return isSelected; }
  public void setSelected(boolean _isSelected) { isSelected = _isSelected; }

  public void toggleSelected() {
    if (isSelected) { 
      isSelected = false; 
    } else { 
      isSelected = true; 
    }
  }

  ////////////////////////////////////////////////////////////////////////////////

}
  
