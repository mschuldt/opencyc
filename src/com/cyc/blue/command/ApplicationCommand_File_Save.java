/* $Id: ApplicationCommand_File_Save.java,v 1.5 2002/05/23 22:30:14 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.command;

import java.util.*;
import java.io.*;
import javax.swing.*;
import org.opencyc.api.*;
import org.opencyc.cycobject.*;
import com.cyc.blue.gui.*;

/**
 * <description of this class or interface>
 *
 * @author John Jantos
 * @date 2001/08/19
 * @version $Id: ApplicationCommand_File_Save.java,v 1.5 2002/05/23 22:30:14 jantos Exp $
 */

public class ApplicationCommand_File_Save extends GenericCommand implements IsCommand {
  private static final boolean DEBUG = false;
  private IsApplicationInternalFrame internalFrame; 

  public ApplicationCommand_File_Save(IsApplicationFrame _frame) {
    internalFrame = _frame.getActiveInternalFrame();
  }

  public ApplicationCommand_File_Save(IsApplicationInternalFrame _internalFrame) {
    internalFrame = _internalFrame;
  }

  public Object execute(IsCommandExecutor _commandExecutor) {
    if (_commandExecutor instanceof IsApplicationCommandExecutor) {
      if (internalFrame.isSaved()) {
	JOptionPane.showMessageDialog(internalFrame.getComponent(),
				      "This graph is already saved!",
				      "Error!",
				      JOptionPane.ERROR_MESSAGE);
      } else {
	Object[] options = {"Save Anyway", "Cancel", "Save then Save"};
	int n = JOptionPane.showOptionDialog(internalFrame.getComponent(),
					       "Your work is not closed, Save anyway?",
					     "Warning!",
					     JOptionPane.YES_NO_CANCEL_OPTION,
					     JOptionPane.WARNING_MESSAGE,
					     null,     //don't use a custom Icon
					     options,  //the titles of buttons
					     options[1]); //default button title
	if (n == 0) {
	} else if (n == 1) {
	} else if (n == 2) {
	}
      }
    }
    return null;
  }
  //public CommandExecutionState getExecutionState() { return executionState; }

  public boolean isPossiblyDestructive() { return false; }
  public boolean wasDestructive() { return false; }

  ////////////////////////////////////////////////////////////////////////////////


}



