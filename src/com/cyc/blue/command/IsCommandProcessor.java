/* $Id: IsCommandProcessor.java,v 1.4 2002/05/23 22:41:22 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.command;

/**
 * Implement to obtain the ability to process commands before they are queued to
 * a command manager.  This allows commands poorly specified at creation to be
 * "processed" by multiple objects before being complete enough to be queued for
 * execution.  This interface is typically used as the visitable object in a
 * visitor pattern.  The commands themselves are the visitors and visit with the
 * "process" method.
 *
 * @author John Jantos
 * @date <creation-date>
 * @version $Id: IsCommandProcessor.java,v 1.4 2002/05/23 22:41:22 jantos Exp $
 */

public interface IsCommandProcessor { // like Visitable

  /** processes a command.  typically this should call a command's process
   * method with this command processor as an argument.
   * @param _command
   */
  public void processCommand(IsCommand _command);

}
