/*
 * Created on Nov 13, 2005
 *
 */
package org.fl.decompteTemps.core;

import java.util.Comparator;
import java.util.Date;

/**
 * @author Fr�d�ric Lef�vre
 *
 */
public class EventsComparator implements Comparator<Events> {

    /**
     * 
     */
    public EventsComparator() {
        super();
       
    }

    /** (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Events o1, Events o2) {
		Date d0 = new Date() ;
		Date d1 = new Date() ;
		d0 = o1.getDateEvents() ;
		d1 = o2.getDateEvents() ;
		return d0.compareTo(d1) ;
    }

}
