/* $Id: IsRenderable.java,v 1.10 2002/05/23 22:30:15 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

/**
 * Implement to obtain the ability to be graphically rendered.  This includes
 * being associated with an Component (i.e. implementing IsComponent), being
 * associated with a renderer, having a visibility status, having a size and
 * location, and having a render() method.
 *
 * @author John Jantos
 * @date 2001/08/23
 * @version $Id: IsRenderable.java,v 1.10 2002/05/23 22:30:15 jantos Exp $
 */

public interface IsRenderable extends IsComponent {
  
  /** sets the renderer.
   * @param _component
   */
  public void setRenderer(IsRenderer _component);

  /** gets the renderer.
   * @return the renderer
   */
  public IsRenderer getRenderer();

  /** sets the visibility attribute.
   * @param _isVisible
   */
  public void setVisible(boolean _isVisible);

  /** gets the visibility attribute.
   * @return the visibility
   */
  public boolean isVisible();

  /** gets the X value (typically the left side) of the renderable.
   * @return the X value
   */
  public double getXD();

  /** gets the Y value (typically the top side) of the renderable.
   * @return the Y value
   */
  public double getYD();

  /** gets the width of the renderable.
   * @return the width
   */
  public double getWidthD();

  /** gets the height of the renderable.
   * @return the height
   */
  public double getHeightD();

  /** gets the smallest X location (typically the left side) of the node.
   * @param the smallest X value of the node.
   */
  public double getMinXD();

  /** gets the smallest Y location (typically the top side) of the node.
   * @param the smallest Y value of the node.
   */
  public double getMinYD();

  /** gets the largest X location (typically the right side) of the node.
   * @param the largest X value of the node.
   */
  public double getMaxXD();

  /** gets the largest Y location (typically the bottom side) of the node.
   * @param the largest T value of the node.
   */
  public double getMaxYD();

  /** gets the normalized X value.  ensures that (0,0) is in the upper left of
   * the graph (no node is in a negative place).
   * @return the normalized X value.
   */
  public double getNormalizedXD();

  /** gets the normalized Y value.  ensures that (0,0) is in the upper left of
   * the graph (no node is in a negative place)
   * @return the normalized Y value.
   */
  public double getNormalizedYD();

  /** gets the normalized width value.
   * @return the normalized width value.
   */
  public double getNormalizedWidthD();

  /** gets the normalized height value.
   * @return the normalized height value.
   */
  public double getNormalizedHeightD();

  /** sets the X and Y values (typically the top left corner) of the renderable.
   * @param _x
   * @param _y
   */
  public void setXYD(double _x, double _y);

  /** sets the width and height of the renderable.
   * @param _width
   * @param _height
   */
  public void setWidthHeightD(double _width, double _height);

  /**
   * requests a render.  this is analagous to AWT's repaint() method.
   * @see java.awt.Component#repaint()
   */
  public void render();

}

/*
public void render(int _ms);
public void repaint();
public void repaint(long _ms);
*/
