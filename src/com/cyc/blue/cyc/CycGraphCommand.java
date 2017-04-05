/* $Id: CycGraphCommand.java,v 1.3 2002/05/23 22:35:04 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.cyc;

import java.util.HashSet;
import java.util.Iterator;
import com.cyc.blue.command.GenericGraphCommand;
import com.cyc.blue.command.IsCommand;
import com.cyc.blue.command.IsCommandExecutor;
import com.cyc.blue.command.IsGraphCommand;
import com.cyc.blue.graph.IsGraph;

/**
 * An abstrace class extending a generic graph command to allow setting of focus
 * nodes to any cyc graph command.  This functionality may be better places in
 * the cyc graph command executor.
 *
 * @author John Jantos
 * @date 2001/09/19
 * @version $Id: CycGraphCommand.java,v 1.3 2002/05/23 22:35:04 jantos Exp $
 */

public abstract class CycGraphCommand extends GenericGraphCommand implements IsGraphCommand {

  private HashSet focusNodes;

  private HashSet getFocusNodes() {
    return focusNodes;
  }

  public Iterator getFocusNodesIterator() {
    if (focusNodes != null) {
      return focusNodes.iterator();
    } else {
      return null;
    }
  }

  public void addFocusNode(Object _cycNode) {
    if (focusNodes == null) {
      focusNodes = new HashSet();
    }
    focusNodes.add(_cycNode);
  }
  
  public Object execute(IsCommandExecutor _commandExecutor) {
      Iterator focusNodesIterator = getFocusNodesIterator();
      if (focusNodesIterator != null) {
	while (focusNodesIterator.hasNext()) {
	  Object focusNode = focusNodesIterator.next(); //todo: dangerous
	  addSubCommand(new CycGraphCommand_FocusAtNode(focusNode));
	}
      }
    return super.execute(_commandExecutor);
  }

}
