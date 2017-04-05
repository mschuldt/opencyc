/* $Id: IsGraphCommand.java,v 1.7 2002/05/23 22:41:22 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.command;

import com.cyc.blue.graph.*;
import com.cyc.blue.gui.*;

/**
 * Implement to obtain the ability to be used a command that acts on graphs.
 *
 * @author John Jantos
 * @date 2001/09/19
 * @version $Id: IsGraphCommand.java,v 1.7 2002/05/23 22:41:22 jantos Exp $
 */

public interface IsGraphCommand extends IsCommand {

  /** sets the graph this command will be working on.
   * @param _graph
   */
  public void setGraph(IsGraph _graph);

  /** gets the grap this command will be working on.
   * @return the graph
   */
  public IsGraph getGraph();

  /** sets the renderable graph this command will be working on.
   */
  public void setRenderableGraph(IsRenderableGraph _renderableGraph);

  /** gets the renderable graph this command will be working on.
   */
  public IsRenderableGraph getRenderableGraph();
}
