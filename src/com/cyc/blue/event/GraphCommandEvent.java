/*{{{
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 * @author John Jantos
 * @date 2002/04/29
 *
 * @version $Id: GraphCommandEvent.java,v 1.2 2002/05/23 22:30:14 jantos Exp $
 *
 *****************************************************************
 * Describe the class functionality
 *
 *****************************************************************
 * External interface:
 *
 * list of the public methods
 *
 *****************************************************************
 *}}}
 */

package com.cyc.blue.event;

import java.util.*;
import com.cyc.event.*;
import com.cyc.blue.graph.*;
import com.cyc.blue.command.*;

public class GraphCommandEvent extends BlueApplicationEvent implements IsEvent {
  private IsGraph graph;
  private IsCommand command;

  public GraphCommandEvent(IsGraph _graph, IsCommand _command) {
    setGraph(_graph);
    setCommand(_command);
  }

  public void setGraph(IsGraph _graph) { graph = _graph; }
  public IsGraph getGraph() { return graph; }
  public void setCommand(IsCommand _command) { command = _command; }
  public IsCommand getCommand() { return command; }

}
