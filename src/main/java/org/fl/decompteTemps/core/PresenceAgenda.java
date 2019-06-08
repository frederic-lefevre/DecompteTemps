package org.fl.decompteTemps.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PresenceAgenda {

    private List<Period> periods ;
    private long 		 duration ;

    public PresenceAgenda() {
        super();
        duration = 0 ;
        periods = new ArrayList<Period>() ;
    }

    public void addPresence(Period p) {
        periods.add(p) ;
        duration = duration + p.getDuration() ;
    }
    
    public long getDuration () {
        return duration ;
    }
    
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
