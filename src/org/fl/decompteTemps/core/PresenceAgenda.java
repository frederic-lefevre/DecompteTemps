/*
 * Created on Nov 4, 2005
 *
 */
package org.fl.decompteTemps.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Fr�d�ric Lef�vre
 *
 */
public class PresenceAgenda {

    private List<Period> periods ;
    private long duration ;
    /**
     * 
     */
    public PresenceAgenda() {
        super();
        duration = 0 ;
        periods = new ArrayList<Period>() ;
    }

    /**
     * Add a presence
     * @param p presence
     */
    public void addPresence(Period p) {
        periods.add(p) ;
        duration = duration + p.getDuration() ;
    }
    
    /**
     * Get the duration of the presence
     * @return the duration of the presence
     */
    public long getDuration () {
        return duration ;
    }
    
    /**
     * Get the presence duration between 2 dates
     * @param a begin date
     * @param b end date
     * @return duration in milliseconds
     */
    public long getDuration(Date a, Date b) {
        
        if (a.after(b)) {
            Control.presenceLog.severe("Begin is after end") ;
            throw new IllegalArgumentException("Begin is after end") ;
        }
        long res = 0 ;
        for (int i = 0 ; i < periods.size(); i++) {
            res = res + ((Period)(periods.get(i))).getDuration(a, b) ;
        }
        return res;
    }
    
    /**
     * Get the periods of the presence agenda
     * @return the periods of the presence agenda
     */
    public Period[] getPeriods() {
        
        Period[] result = new Period[periods.size()] ;
        
        return (Period[])periods.toArray(result) ;
    }
}
