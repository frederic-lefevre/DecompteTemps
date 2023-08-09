/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.fl.decompteTemps.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class EventAgenda {

    private List<Events> listEvents ;

    public EventAgenda() {
        super();
        listEvents = new ArrayList<Events>() ;
    }

    public void addEvents(Date dateEvents, String[] nameEvents, int typeEvents, String comment) {
        listEvents.add(new Events(dateEvents, nameEvents, typeEvents, comment)) ;
        EventsComparator evComp = new EventsComparator() ;
        Collections.sort(listEvents, evComp) ;
    }
    
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
