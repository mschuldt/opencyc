/* $Id: IsCommandable.java,v 1.3 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.command;

/**
 * Implement to obtain the ability to be commandable, in other words to be used
 * as the commanded object of a command manager.
 * @see IsCommandManager
 *
 * @author jantos
 * @date 2002/02/23
 * @version $Id: IsCommandable.java,v 1.3 2002/05/23 22:30:14 jantos Exp $
 */

public interface IsCommandable extends IsCommandProcessor {

  /**
   * sets the command manager for this commandable.
   * @param _commandManager
   */
  public void setCommandManager(IsCommandManager _commandManager);

  /**
   * gets the command manager for this commandable.
   * @return the command manager.
   */
  public IsCommandManager getCommandManager();

}
