/* $Id: IsNodeRenderer.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.Graphics2D;

/**
 * Implement to obtain the ability to render a node to a Graphics2D.
 * @see java.awt.Graphics2D
 *
 * @author John Jantos
 * @date 2002/02/13
 * @version $Id: IsNodeRenderer.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
 */

public interface IsNodeRenderer extends IsRenderer {

  /** renders the renderable node to the specified graphics object.
   * @param _renderableNode
   * @param _g the Graphics object to render to.
   */
  public void renderNode(IsRenderableNode _renderableNode, Graphics2D _g);

  /** renders the renderable node's forces to the specified graphics object.
   * @param _renderableNode
   * @param _g the Graphics object to render to.
   */
  public void renderForces(IsRenderableNode _renderableNode, Graphics2D _g);

}
