/*
 * Created on Nov 13, 2005
 *
 */
package org.fl.decompteTemps.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * @author Fr�d�ric Lef�vre
 *
 */
public class EventAgenda {

    private List<Events> listEvents ;
    /**
     * 
     */
    public EventAgenda() {
        super();
        listEvents = new ArrayList<Events>() ;
    }

    /**
     * Add an event
     * @param dateEvents event date
     * @param nameEvents event names
     * @param typeEvents event type
     * @param comment
     */
    public void addEvents(Date dateEvents, String[] nameEvents, int typeEvents, String comment) {
        listEvents.add(new Events(dateEvents, nameEvents, typeEvents, comment)) ;
        EventsComparator evComp = new EventsComparator() ;
        Collections.sort(listEvents, evComp) ;
    }
    
    /**
     * Merge 2 event agendas
     * @param agd the event agenda to merge
     */
    public void mergeEventAgenda(EventAgenda agd) {
        listEvents.addAll(agd.listEvents) ;
        EventsComparator evComp = new EventsComparator() ;
        Collections.sort(listEvents, evComp) ;
        condenseAgenda() ;
    }
    
    private void condenseAgenda() {
        List<Events> newList = new ArrayList<Events>(listEvents.size()) ;
        Events currEvent, nextEvent ;
        currEvent = (Events)(listEvents.get(0)) ;
        for (int i=1; i < listEvents.size(); i++) {    
            nextEvent = (Events)(listEvents.get(i)) ;
            if (! currEvent.mergeEvent(nextEvent)) {
                newList.add(currEvent) ;
                currEvent = nextEvent ;
            }
        }
        newList.add(currEvent) ;
        listEvents = newList ;
    }
    
    /**
     * Get the events
     * @return the events of the event agenda
     */
    public Events[] getEvents() {
        Events[] evt = new Events[listEvents.size()] ;
        
        Events[] resEvt = (Events[])(listEvents.toArray(evt)) ;
        
		// sort by date, last date first
		Arrays.sort(resEvt, new Comparator<Events>() {
			public int compare(Events o1, Events o2) {
				return ((Events) o2).getDateEvents().compareTo(((Events) o1).getDateEvents());
			}
		});
		
        return resEvt ;
    }
}
