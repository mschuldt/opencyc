/* $Id: IsComponent.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.Component;

/**
 * Implement to obtain the ability to be associated with one java.awt.Component.
 * @see java.awt.Component
 *
 * @author John Jantos
 * @date 2002/02/18
 * @version $Id: IsComponent.java,v 1.3 2002/05/22 01:33:22 jantos Exp $
 */

public interface IsComponent {

  /** sets the assiciated Component.
   * @param _component
   */
  public void setComponent(Component _component);

  /** gets the assiciated Component.
   * @return the component.
   */
  public Component getComponent();
  
}
