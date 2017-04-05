/* $Id: ApplicationInternalFrame.java,v 1.6 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JInternalFrame;
import com.cyc.blue.command.IsCommandExecutor;
import com.cyc.blue.command.IsCommandManager;

/**
 * A basic implementation of IsApplicationInternalFrame.
 *
 * @author John Jantos
 * @date 2001/10/18
 * @version $Id: ApplicationInternalFrame.java,v 1.6 2002/05/23 22:30:14 jantos Exp $
 */

public abstract class ApplicationInternalFrame extends JInternalFrame implements IsApplicationInternalFrame {
  private IsCommandExecutor commandExecutor;
  private IsCommandManager commandManager;
  private boolean isSaved = false;

  public ApplicationInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
    super(title, resizable, closable, maximizable, iconifiable);
  }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsApplicationInternalFrame

  public void setJInternalFrame(JInternalFrame _frame) { }
  public JInternalFrame getJInternalFrame() { return this; }

  public void setSaved(boolean _isSaved) { isSaved = _isSaved; }
  public boolean isSaved() { return isSaved; }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsContainer

  public void setContainer(Container _container) { }
  public Container getContainer() { return this; }

  ////////////////////////////////////////////////////////////////////////////////
  // Qua IsComponent

  public void setComponent(Component _component) { }
  public Component getComponent() { return this; }

}

/*
public void setCommandExecutor(IsCommandExecutor _commandExecutor) {
  commandExecutor = _commandExecutor;
}
public IsCommandExecutor getCommandExecutor() {
  return commandExecutor;
}
*/
