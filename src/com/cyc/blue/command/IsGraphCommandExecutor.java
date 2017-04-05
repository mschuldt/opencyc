/* $Id: IsGraphCommandExecutor.java,v 1.9 2002/05/23 22:41:22 jantos Exp $
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
 * Implement to obtain the ability to execute graph commands.  This interface
 * adds some general graph manipulating methods that graph commands can use
 * instead of modifying the graph directly to allow extra work to be done, such
 * as keeping track of why nodes exist in a graph.
 * @see GraphCommand
 *
 * @author John Jantos
 * @date 2001/09/24
 * @version $Id: IsGraphCommandExecutor.java,v 1.9 2002/05/23 22:41:22 jantos Exp $
 */

public interface IsGraphCommandExecutor extends IsCommandExecutor {
  
  /** sets the graph this command executor is executing commands for
   * @param _graph
   */
  public void setGraph(IsGraph _graph);

  /** gets the graph this command executor is executing commands for
   * @return the graph
   */
  public IsGraph getGraph();

  /** adds the specified node to the graph for the specified command.
   * @param _command
   * @param _node
   */
  public void addNode(IsCommand _command, IsNode _node);

  /** removes the specified node from the graph for the specified command.
   * @param _command
   * @param _node
   * @return <code>true</code> iff the node was in the graph (and removed).
   */
  public boolean removeNode(IsCommand _command, IsNode _node);

  //public IsEdge addEdge(IsCommand _command, IsNode _tailNode, IsNode _HeadNode, Object _relation);

  /** adds the specified edge to the graph for the specified command.
   * @param _command
   * @param _edge
   */
  public void addEdge(IsCommand _command, IsEdge _edge);

  /** removes the specified edge from the graph for the specified command.
   * @param _command
   * @param _edge
   * @return <code>true</code> iff the edge was in the graph (and removed).
   */
  public boolean removeEdge(IsCommand _command, IsEdge _edge);

  //public boolean isNodeFocus(IsNode _node);

  /** gets whether a node in the graph is directly justified by a command that added it
   * @param _node
   * @return <code>true</code> iff the node has a justifying command.
   */
  public boolean isNodeJustified(IsNode _node);

  /** gets an iterator over the commands that justify a node in the graph.
   * @param _node
   * return and iterator over commands
   */
  public Iterator getNodeJustificationsIterator(IsNode _node);

  /** gets whether an edge in the graph is directly justified by a command that added it
   * @param _edge
   * @return <code>true</code> iff the node has a justifying command.
   */
  public boolean isEdgeJustified(IsEdge _edge);

  /** gets an iterator over the commands that justify an edge in the graph.
   * @param _edge
   * return and iterator over commands
   */
  public Iterator getEdgeJustificationsIterator(IsEdge _edge);

}
