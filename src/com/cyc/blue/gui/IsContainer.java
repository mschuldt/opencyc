/* $Id: IsContainer.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.Container;

/**
 * Implement to obtain the ability to be associated with one java.awt.Container.
 * @see java.awt.Container
 *
 * @author John Jantos
 * @date 2002/02/18
 * @version $Id: IsContainer.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
 */

public interface IsContainer extends IsComponent {

  /** sets the associated Container.
   * @param _container
   */
  public void setContainer(Container _container);

  /** gets the associated Container.
   * @return the container.
   */
  public Container getContainer();
  
}
