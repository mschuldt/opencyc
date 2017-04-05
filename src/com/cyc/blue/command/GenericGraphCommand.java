/* $Id: GenericGraphCommand.java,v 1.3 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.command;

import java.util.*;
import com.cyc.blue.graph.*;
import com.cyc.blue.gui.*;

/**
 * <description of this class or interface>
 *
 * @author John Jantos
 * @date 2001/09/19
 * @version $Id: GenericGraphCommand.java,v 1.3 2002/05/23 22:30:14 jantos Exp $
 */

public class GenericGraphCommand extends GenericCommand implements IsGraphCommand {

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsCommand

  // all from GenericCommand

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsGraphCommand
  private IsGraph graph;
  private IsRenderableGraph renderableGraph;

  public void setGraph(IsGraph _graph) { graph = _graph; }
  public IsGraph getGraph() { return graph; }

  public void setRenderableGraph(IsRenderableGraph _renderableGraph) { renderableGraph = _renderableGraph; }
  public IsRenderableGraph getRenderableGraph() { return renderableGraph; }

  ////////////////////////////////////////////////////////////////////////////////

  // promote to IsGraphCommand?
  private IsGraphFrame graphFrame;
  private IsApplicationFrame applicationFrame;

  public void setGraphFrame(IsGraphFrame _graphFrame) { graphFrame = _graphFrame; }
  public IsGraphFrame getGraphFrame() { return graphFrame; }
  public void setApplicationFrame(IsApplicationFrame _applicationFrame) { applicationFrame = _applicationFrame; }
  public IsApplicationFrame getApplicationFrame() { return applicationFrame; }
}
