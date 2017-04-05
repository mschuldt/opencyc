/* $Id: IsPublisherGraph.java,v 1.3 2002/05/22 17:11:10 jantos Exp $
 *
 * Copyright (c) 2002 Cycorp, Inc.  All rights reserved.
 * 
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 *
 */

package com.cyc.blue.graph;

import com.cyc.event.IsEventPublisher;

/**
 * Implement to obtain the ability to be a graph and publish events.
 *
 * @author John Jantos
 * @date 2002/01/17
 * @version $Id: IsPublisherGraph.java,v 1.3 2002/05/22 17:11:10 jantos Exp $
 */

public interface IsPublisherGraph extends IsGraph, IsEventPublisher {

}
