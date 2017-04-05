/* $Id: IsRenderableNode.java,v 1.11 2002/05/23 22:30:15 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.geom.Point2D;
import java.util.Iterator;
import com.cyc.blue.graph.IsNode;
import com.cyc.blue.layout.IsGraphLayoutStrategy;

/**
 * Implement to obtain the ability to be used as a graph node and be renderable.
 * In addition the the methods inherited from IsNode and IsRenderable
 * interfaces, IsRenderableNode adds: renderable graph getter/setter (for
 * convenience), some properties (locked, selected, focus), more getter/setters
 * of size and location, a method to termine where edges should attack based on
 * the node on the other side of the edge, and methods for adding, applying, and
 * looking at forces on the node.
 *
 * @author John Jantos
 * @date 2001/08/23
 * @version $Id: IsRenderableNode.java,v 1.11 2002/05/23 22:30:15 jantos Exp $
 */

public interface IsRenderableNode extends IsNode, IsRenderable {
  
  /** sets the renderable graph this node is a part of.
   * @param _renderableGraph
   */
  public void setRenderableGraph(IsRenderableGraph _renderableGraph);

  /** sets the renderable graph this node is a part of.
   * @return the renderable graph.
   */
  public IsRenderableGraph getRenderableGraph();

  // status

  /** sets the locked status of the node.
   * @param _isLocked
   */
  public void setLocked(boolean _isLocked);

  /** gets the locked status of the node.
   * @return the locked status
   */
  public boolean isLocked();

  /** gets the selected status of the node.
   * @return the selected status
   */
  public boolean isSelected();

  /** sets the selected status of the node.
   * @param _isSelected
   */
  public void setSelected(boolean _isSelected);

  /** toggles the selected status of the node.
   */
  public void toggleSelected();

  // setters/getters

  /** sets the location of the center of the node.
   * @param _centerX
   * @param _centerY
   */
  public void setCenterD(double _centerX, double _centerY);

  /** sets the X location of the center of the node.
   * @param _centerX
   */
  public void setCenterXD(double _centerX);

  /** sets the Y location of the center of the node.
   * @param _centerY
   */
  public void setCenterYD(double _centerY);

  /** gets the X location of the center of the node.
   * @param the X value of the center of the node.
   */
  public double getCenterXD();

  /** gets the Y location of the center of the node.
   * @param the Y value of the center of the node.
   */
  public double getCenterYD();

  /** determines the point in the node that an edge from another renderable node
   * should attach to.
   * @param _renderableNode
   * @return the point
   */
  public Point2D.Double getAttachmentPoint(IsRenderableNode _renderableNode);

  /** gets the last value returned by getAttachmentPoint
   * @param _renderableNode
   * @return the point
   */
  public Point2D.Double getLastAttachmentPoint(IsRenderableNode _renderableNode);

  // layout

  /** adds a force vector to the node.
   * @param _dx
   * @param _dy
   * @param _strategy the IsGraphLayoutStrategy that calculated this force
   */
  public void addForce(double _dx, double _dy, IsGraphLayoutStrategy _strategy);

  /** applies the pending force vectors to the node.
   */
  public void applyPendingForces();

  /** sets the "force inhibition" -- a value indicating a value to divide all
   * force vectors by to slow node movement.
   * @param the inhibition value
   */
  public void setForceInhibition(double _inhibition);
  
  /** gets the "force inhibition"
   * @return the inhibition value
   */
  public double getForceInhibition();

  // forces jjj@todo: check for movement to own interface??

  /** gets the list of strategies used on this node during the last application of forces.
   * @return iterator through IsGraphLayoutStrategy's
   */
  public Iterator getLastForceStrategiesIterator();

  /** gets the X value of the last force created by the specified layout strategy.
   * @param _strategy
   * @return the X value
   */
  public double getLastXForceByStrategy(IsGraphLayoutStrategy _strategy);

  /** gets the Y value of the last force created by the specified layout strategy.
   * @param _strategy
   * @return the Y value
   */
  public double getLastYForceByStrategy(IsGraphLayoutStrategy _strategy);
  
}

/*
// incident edges
public void addEdgeOut(IsEdge _e);
public void addEdgeIn(IsEdge _e);
public void removeEdgeOut(IsEdge _e);
public void removeEdgeIn(IsEdge _e);
public Iterator getEdgesOut();
public Iterator getEdgesIn();
public Iterator getEdgesUndirected();
*/
