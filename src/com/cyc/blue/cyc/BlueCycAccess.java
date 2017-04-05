/* $Id: BlueCycAccess.java,v 1.10 2002/05/23 22:35:04 jantos Exp $
 *
 * Copyright (c) 2001 - 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.cyc;

import java.io.IOException;
import org.opencyc.api.CycAccess;
import org.opencyc.api.CycApiException;
import org.opencyc.cycobject.CycFort;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.CycSymbol;

/**
 * An extension of CycAccess that adds methods to track relevant mts and to get
 * a paraphase string for a CycFort.
 *
 * @author John Jantos
 * @date 2001/08/06
 * @version $Id: BlueCycAccess.java,v 1.10 2002/05/23 22:35:04 jantos Exp $
 */

public class BlueCycAccess extends CycAccess {
  private static final boolean DEBUG = false;
  private BlueCycConnection cycConnection;
  private static final CycSymbol GET_NODES = new CycSymbol("GET-NODES");
  private static final CycSymbol UIA_TERM_PHRASE_MEMOIZED = new CycSymbol("UIA-TERM-PHRASE-MEMOIZED");
  private static CycFort domainMt;
  private static CycFort parsingMt;
  private static CycFort generationMt;
  
  public BlueCycAccess(String _hostName, int _basePort, int _communicationMode) throws IOException, CycApiException {
    super(_hostName, _basePort, _communicationMode, 
	  // assume persistence
	  true);
    if (DEBUG) { System.out.println("TRACE: BlueCycAccess[" + this + "](" + _hostName + ", " + _basePort + ", " + _communicationMode + ")"); }
    //cfaslImport(BlueCycAccess.class, "blue-cyc-api.cfasl");
    domainMt = getConstantByName("BaseKB");
    parsingMt = getConstantByName("EnglishMt");
    generationMt = getConstantByName("EnglishMt");
    
  }  

  public BlueCycAccess(CycAccessInitializer _cycAccessInitializer) throws IOException, CycApiException {
    super(_cycAccessInitializer.getHostName(),
	  _cycAccessInitializer.getBasePort(),
	  _cycAccessInitializer.getCommunicationMode(), 
	  // assume persistence
	  true);
    if (DEBUG) { System.out.println("TRACE: BlueCycAccess[" + this + "](" + _cycAccessInitializer + ")"); }
    if (DEBUG) { System.out.println("TRACE: --> hostName = " + _cycAccessInitializer.getHostName() + ")"); }
    if (DEBUG) { System.out.println("TRACE: --> basePort = " + _cycAccessInitializer.getBasePort() + ")"); }
    if (DEBUG) { System.out.println("TRACE: --> CommunicationsMode = " + _cycAccessInitializer.getCommunicationMode() + ")"); }
    //traceOn();
    //cfaslImport(BlueCycAccess.class, "blue-cyc-api.cfasl");
    domainMt = getConstantByName("BaseKB");
    parsingMt = getConstantByName("EnglishMt");
    generationMt = getConstantByName("EnglishMt");

  }

  public void setDomainMt(CycFort _domainMt) { domainMt = _domainMt; }
  public CycFort getDomainMt() { return domainMt; }
  public void setParsingMt(CycFort _parsingMt) { parsingMt = _parsingMt; }
  public CycFort getParsingMt() { return parsingMt; }
  public void setGenerationMt(CycFort _generationMt) { generationMt = _generationMt; }
  public CycFort getGenerationMt() { return generationMt; }
  
  public static String getParaphrase(CycFort _fort) { 
    CycList command = new CycList();
    command.add(UIA_TERM_PHRASE_MEMOIZED);
    command.add(_fort);
    //System.err.println("generationMt: " + generationMt);
    command.add(generationMt);
    //System.err.println("domainMt: " + domainMt);
    command.add(domainMt);
    //System.err.println("getParaphrase: " + command);
    String result;
    try {
      result = current().converseString(command);
      return result;
    } catch (IOException e) {
    } catch (CycApiException e) {
    }
    return _fort.toString();
  }

//    public CycList getLinks(CycFort _fort, IsRelation _relation) throws IOException, CycApiException {
//  //        String command = 
//  //          "(" + GET_NODES + " " +
//  //          _fort.cyclify() + " " +
//  //          ((CycFort)_relation.getRelation()).cyclify() + " " +
//  //          _relation.getDirection().toString() + ")";
//  //        return converseList(command);
    
//  //      StringBuffer command = new StringBuffer();
//  //      command.append("(" + GET_NODES + " " + _fort.cyclify() + " ");
//  //      command.append(((CycFort)_relation.getRelation()).cyclify() + " ");
//  //      command.append(_relation.getDirection().toString() + ")");
//  //      return converseList(command.toString());

//      CycList command = new CycList();
//      command.add(GET_NODES);
//      command.add(_fort);
//      command.add(((CycFort)_relation.getRelation()));
//      command.add(_relation.getDirection().getKeyword());
//      return converseList(command);

//    }

//    public void cfaslImport(Class base, String filename)
//      throws IOException {
//      if (base==null) base = BlueCycAccess.class;
//      InputStream in = base.getResourceAsStream(filename);
//      if (in != null) {
//        cycConnection.cfaslImport(in);
//        in.close();
//      }
//      else {
//        throw new IOException("Unable to load resource: " + filename);
//      }
//    }

//   public Iterator getLinks(FORT fort, FORT pred, RelationDirection direction) {
//      Iterator i = null;
//      try {
//        i = toVector(funcall(GET_NODES, fort, pred, direction.getKeyword())).iterator();
//      } catch (CycException e) {
//        System.err.println ("Error wrt Cyc: " + e);
//      } catch (IOException e) {
//        System.err.println ("Error wrt IO: " + e);
//      }
//      //System.out.println ("BlueCycAPI.getLinks(" + fort + "," + pred + "," + direction + ") returned " + i);
//      return i;
//    }
      
}
