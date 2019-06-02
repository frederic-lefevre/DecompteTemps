package org.fl.decompteTemps.core;

import java.util.Date;

public class Events {

    private Date dateEvents ;
    private String[] nameEvents ;
    private int typeEvents ;
    private String comment;
    
    public final static int IN = 1 ;
    public final static int OUT = 2 ;

    public Events(Date dateEvents, String[] nameEvents, int typeEvents, String c) {
        super();
        this.dateEvents = dateEvents;
        this.nameEvents = nameEvents;
        this.typeEvents = typeEvents;
        comment = c;
    }
    
    private void addEventsName(String[] names) {
        String[] newNames = new String[names.length + nameEvents.length] ;
        for (int i=0; i < names.length; i++) {
            newNames[i] = names[i] ;
        }
        for (int j=0; j < nameEvents.length; j++) {
            newNames[j+names.length] = nameEvents[j] ;
        }
        nameEvents = newNames ;
    }
    
    /**
     * Merge 2 events (if date and type are the same, names and comment are added)
     * @param e the event
     * @return true if the event are merged, false otherwise
     */
    public boolean mergeEvent(Events e) {
        
        if ((e.typeEvents == typeEvents) && (e.dateEvents.equals(dateEvents))) {
            addEventsName(e.nameEvents) ;
            String eComment = e.getComment() ;
            if ((eComment != null) && (eComment.length() > 0)) {
                comment = comment + ", " + e.getComment() ;
            }
            return true ;
        } else {
            return false ;
        }
    }

    public Date getDateEvents() {
        return dateEvents;
    }

    public String[] getNameEvents() {
        return nameEvents;
    }

    public int getTypeEvents() {
        return typeEvents;
    }

    public String getComment() {
        return comment;
    }
}
