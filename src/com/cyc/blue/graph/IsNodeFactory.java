/* $Id: IsNodeFactory.java,v 1.3 2002/05/22 00:52:52 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.graph;

/**
 * Implement to obtain the ability to create nodes.
 *
 * @author John Jantos
 * @date 2002/02/07
 * @version $Id: IsNodeFactory.java,v 1.3 2002/05/22 00:52:52 jantos Exp $
 */

public interface IsNodeFactory {
  
  /** sets the class that nodes will be instance of. 
   * @param _nodeClass the node core class. 
   */
  public void setNodeClass(Class _nodeClass);

  /** gets the class that nodes are instances of.
   * @return the node core class. 
   */
  public Class getNodeClass();

  /** creates a new instance of the node class and set the core object.
   * @param _core
   * @return the instantiated IsNode.
   */
  public IsNode instantiate(Object _core);

  /** finds an instance of the node class with specified core object.
   * @param _core
   * @return an IsNode if found, o/w null.
   */
  public IsNode find(Object _core);
  
}
