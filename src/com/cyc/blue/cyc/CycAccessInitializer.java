/* $Id: CycAccessInitializer.java,v 1.7 2002/05/23 22:35:04 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.cyc;

/**
 * A class to hold the parameter necessary to open a connection to a runnig cyc
 * image including host name, base TCP port, and communications mode.  Ideally
 * This class should be moved into org.opencyc.api.
 * @see org.opencyc.api
 *
 * @author John Jantos
 * @date 2001/09/10
 * @version $Id: CycAccessInitializer.java,v 1.7 2002/05/23 22:35:04 jantos Exp $
 */

public class CycAccessInitializer {
  private String hostName;
  private int basePort;
  public String hiddenHostName;
  public String hiddenBasePort;
  public String login;
  public String password;
  private int communicationMode;

  public CycAccessInitializer() {
    // use default hostName and basePort
    hostName = "localhost";
    basePort = 3600;
    communicationMode = org.opencyc.api.CycConnection.BINARY_MODE;
  }

  public CycAccessInitializer(String _hostName, int _basePort) {
    hostName = _hostName;
    basePort = _basePort;
    communicationMode = org.opencyc.api.CycConnection.BINARY_MODE;
  }

  public CycAccessInitializer(String _hostName, int _basePort, int _communicationMode) {
    hostName = _hostName;
    basePort = _basePort;
    communicationMode = _communicationMode;
  }

  public CycAccessInitializer(String _hostName, 
			      int _basePort, 
			      String _hiddenHostName, 
			      String _hiddenBasePort, 
			      String _login,
			      String _password,
			      int _communicationMode) {
    hostName = _hostName;
    basePort = _basePort;
    hiddenHostName = _hiddenHostName;
    hiddenBasePort = _hiddenBasePort;
    login = _login;
    password = _password;
    communicationMode = _communicationMode;
  }

  public String getHostName() {
    return hostName;
  }
  
  public int getBasePort() {
    return basePort;
  }
  
  public int getCommunicationMode() {
    return communicationMode;
  }
}
