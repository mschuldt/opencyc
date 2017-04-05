/* $Id: IsMenuBar.java,v 1.5 2002/05/23 22:30:15 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

/**
 * Implement to obtain the ability to be used as a menu bar.  Currently this
 * interface is empty, but it should be expanded to be more dynamic.  The
 * current BlueMenuBar is unchanging but it should change to reflect information
 * found in the commandManager, the graph itself, etc.  Thus this interface
 * should eventually have method for creating the menubar and the menus on
 * demand.
 *
 * @author John Jantos
 * @date 2001/10/18
 * @version $Id: IsMenuBar.java,v 1.5 2002/05/23 22:30:15 jantos Exp $
 */

public interface IsMenuBar {

}
