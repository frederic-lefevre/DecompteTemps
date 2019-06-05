package org.fl.decompteTemps.core;

import java.util.Comparator;
import java.util.Date;

public class EventsComparator implements Comparator<Events> {

    public EventsComparator() {
        super();      
    }

    public int compare(Events o1, Events o2) {
		Date d0 = o1.getDateEvents() ;
		Date d1 = o2.getDateEvents() ;
		return d0.compareTo(d1) ;
    }
}
