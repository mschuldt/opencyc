/* $Id: IsEdgeRenderer.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
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
 * Implement to obtain the ability to render an edge to a Graphics2D.
 *
 * @author John Jantos
 * @date 2002/02/13
 * @version $Id: IsEdgeRenderer.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
 */

public interface IsEdgeRenderer extends IsRenderer {

  /** renders the renderable edge to the specified graphics object.
   * @param _renderableEdge
   * @param _g the Graphics object to render to.
   */
  public void renderEdge(IsRenderableEdge _renderableEdge, Graphics2D _g);

}
