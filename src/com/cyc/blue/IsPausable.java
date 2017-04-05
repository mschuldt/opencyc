/* $Id: IsPausable.java,v 1.3 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue;

/**
 * Implement to obtain the ability to be paused and resumed.
 *
 * @author John Jantos
 * @date 2002/03/11
 * @version $Id: IsPausable.java,v 1.3 2002/05/23 22:30:14 jantos Exp $
 */

public interface IsPausable {
  
  /** sets the paused attribute.
   * @param _isPaused
   */
  public void setPaused(boolean _isPaused);

  /** gets the paused attribute.
   * @param _isPaused
   */
  public boolean isPaused();

}
