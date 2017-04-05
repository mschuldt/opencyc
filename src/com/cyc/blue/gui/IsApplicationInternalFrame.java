/* $Id: IsApplicationInternalFrame.java,v 1.5 2002/05/22 01:33:22 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.gui;

import javax.swing.JInternalFrame;

/**
 * Implement to obtain the ability to be an internal frame of an application.
 * Note that this class depends on the swing JInternalFrame.
 * @see javax.swing.JInternalFrame
 *
 * @author John Jantos
 * @date 2002/02/07
 * @version $Id: IsApplicationInternalFrame.java,v 1.5 2002/05/22 01:33:22 jantos Exp $
 */

public interface IsApplicationInternalFrame extends IsContainer {

  /** sets the application frame that this application internal frame is subframe of.
   * @param _applicationFrame
   */
  public void setApplicationFrame(IsApplicationFrame _applicationFrame);

  /** gets the application frame that this application internal frame is subframe of.
   * @return the application frame
   */
  public IsApplicationFrame getApplicationFrame();

  /** sets the actual JInternalFrame of this application internal frame.
   * @param _jInternalFrame
   */
  public void setJInternalFrame(JInternalFrame _jInternalFrame);

  /** gets the actual JInternalFrame of this application internal frame.
   */
  public JInternalFrame getJInternalFrame();
  
  /** sets the "saved" attribute of this application internal frame.  if
   * modifications have been made that are not saved in a file somewhere, this
   * should be <code>true</code>
   * @param _isSaved
   */
  public void setSaved(boolean _isSaved);

  /** gets the "saved" attribute of this application internal frame.
   */
  public boolean isSaved();

}

/*
// These methods are pertinent to application internal frames but are duplicates
// of JInternalFrame methods.  Use getInternalFrame().method(...) instead.  If
// it is necessary to add these back to this interface, give them different
// names than their JInternalFrame counterparts to avoid confusion.
public void setClosed(boolean _isClosed) throws java.beans.PropertyVetoException;
public boolean isClosable();
public void setMaximum(boolean _maximum) throws java.beans.PropertyVetoException;;
public void setSelected(boolean _selected) throws java.beans.PropertyVetoException;;
*/
