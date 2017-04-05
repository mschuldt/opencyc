/* $Id: IsApplicationFrame.java,v 1.8 2002/05/23 22:30:15 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import javax.swing.RootPaneContainer;
import com.cyc.blue.command.IsCommandable;
import com.cyc.blue.command.IsCommandManager;
import com.cyc.blue.command.IsCommandProcessor;

/**
 * Implement to obtain the ability to be used as an application frame.
 * Application frames contain application internal frames.
 * @see IsApplicationInternalFrame
 *
 * @author John Jantos
 * @date 2001/10/18
 * @version $Id: IsApplicationFrame.java,v 1.8 2002/05/23 22:30:15 jantos Exp $
 */

public interface IsApplicationFrame extends IsContainer, IsCommandable, IsCommandProcessor {

  /** sets the root pane container.  note that the class RootPaneContainer is
   * hardcoded for swing compatibility and is not ideal.
   * @param _rootPaneContainer
   * @deprecated 20020520: seems to no longer be useful.
   */
  public void setRootPaneContainer(RootPaneContainer _rootPaneContainer);
  /** gets the root pane container.
   * @return the root pane container
   * @deprecated 20020520: seems to no longer be useful.
   */
  public RootPaneContainer getRootPaneContainer();

  /** adds an internal frame to this application frame.
   * @param _applicationInternalFrame
   */
  public void addInternalFrame(IsApplicationInternalFrame _applicationInternalFrame);

  /** gets the active internal frame.  active means that the user is currently
   * working in this internal frame.
   * @return the active internal frame.
   */
  public IsApplicationInternalFrame getActiveInternalFrame();

  /** gets the command manager for this application frame.
   * @return the command manager for this application frame.
   */
  public IsCommandManager getCommandManager();

}
   
/*
public IsApplicationInternalFrame newInternalFrame(boolean _resizable, boolean _closable, boolean _maximizable, boolean _iconifiable);
*/  
