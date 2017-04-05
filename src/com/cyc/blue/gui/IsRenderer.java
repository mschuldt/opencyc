/* $Id: IsRenderer.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.Graphics;

/**
 * Implement to obtain the ability to be a renderer.
 *
 * @author John Jantos
 * @date 2002/02/13
 * @version $Id: IsRenderer.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
 */

public interface IsRenderer {

  /** requests a render of the specified renderable.  this is analagous to AWT's
   * repaint() method.
   * @see java.awt.Component#repaint()
   * @param _renderable
   */
  public void render(IsRenderable _renderable);

  /** renders the specified renderable to the specified graphics object.
   * @param _renderable
   * @param _g the Graphics object to render to.
   */
  public void render(IsRenderable _renderable, Graphics _g);

}
