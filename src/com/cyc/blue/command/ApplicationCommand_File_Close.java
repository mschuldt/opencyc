/* $Id: ApplicationCommand_File_Close.java,v 1.6 2002/05/23 22:30:14 jantos Exp $
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
 * @date 2001/08/15
 * @version $Id: ApplicationCommand_File_Close.java,v 1.6 2002/05/23 22:30:14 jantos Exp $
 */

public class ApplicationCommand_File_Close extends GenericCommand implements IsCommand {
  private static final boolean DEBUG = false;
  private IsApplicationInternalFrame applicationInternalFrame; 

  public ApplicationCommand_File_Close(IsApplicationFrame _frame) {
    applicationInternalFrame = _frame.getActiveInternalFrame();
  }

  public ApplicationCommand_File_Close(IsApplicationInternalFrame _applicationInternalFrame) {
    applicationInternalFrame = _applicationInternalFrame;
  }

  public Object execute(IsCommandExecutor _commandExecutor) {
    if (_commandExecutor instanceof IsApplicationCommandExecutor) {
      JInternalFrame jInternalFrame = applicationInternalFrame.getJInternalFrame();
      if (!jInternalFrame.isClosable()) {
	JOptionPane.showMessageDialog(applicationInternalFrame.getComponent(),
				      "This graph cannot currently be closed.",
				      "Error!",
				      JOptionPane.ERROR_MESSAGE);
      } else {
	if (applicationInternalFrame.isSaved()) {
	  try {
	    jInternalFrame.setClosed(true);
	  } catch (java.beans.PropertyVetoException e) {
	  }
	} else {
	  Object[] options = {"Close Anyway", "Cancel", "Save then Close"};
	  int n = JOptionPane.showOptionDialog(applicationInternalFrame.getComponent(),
					       "Your work is not saved, close anyway?",
					       "Warning!",
					       JOptionPane.YES_NO_CANCEL_OPTION,
					       JOptionPane.WARNING_MESSAGE,
					       null,     //don't use a custom Icon
					       options,  //the titles of buttons
					       options[1]); //default button title
	  if (n == 0) {
	    try {
	      jInternalFrame.setClosed(true);
	    } catch (java.beans.PropertyVetoException e) {
	    }
	  } else if (n == 1) {
	  } else if (n == 2) {
	    addSubCommand(new ApplicationCommand_File_Save(applicationInternalFrame));
	    addSubCommand(new ApplicationCommand_File_Close(applicationInternalFrame));
	    super.execute(_commandExecutor);
	  }
	}
      }
      return null;
    } else {
      return null;
    }
  }
  //public CommandExecutionState getExecutionState() { return executionState; }

  public boolean isPossiblyDestructive() { return false; }
  public boolean wasDestructive() { return false; }

  ////////////////////////////////////////////////////////////////////////////////


}



