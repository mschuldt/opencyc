/* $Id: IsGraphStatusBar.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import com.cyc.blue.graph.IsGraph;

/**
 * Implement to obtain the ability to be used a status bar for a graph.
 *
 * @author John Jantos
 * @date 2002/04/26
 * @version $Id: IsGraphStatusBar.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
 */

public interface IsGraphStatusBar extends IsComponent {
  
  /** sets the graph this status bar is reporting on.
   * @param _graph
   */
  public void setGraph(IsGraph _graph);
  
  /** gets the graph this status bar is reporting on. 
   * @return the graph.
   */
  public IsGraph getGraph();

  // todo: flesh these out better.  (what are the different types of messages and why does this matter?)
  public void addObjectMessage(Object _object, String _message); // IsMessage?? & IsAlert below??
  public void addObjectAlert(Object _object, String _alert);
  public void addObject(Object _object);

}
