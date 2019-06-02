/*
 * Created on Nov 4, 2005
 *
 */
package org.fl.decompteTemps.core;

import java.io.IOException;
import java.util.Date;

/**
 * @author Fr�d�ric Lef�vre
 *
 */
public class Entity {

    private String name;
    
    private PresenceAgenda presence ;
    private StorageEntity storage;
    
    /**
     * @param st The storage of the entity
     */
    public Entity(StorageEntity st) {
        super();
       
        storage = st ;
        name = storage.getName() ;
        presence = storage.getPresenceAgenda() ; 
    }

    /**
     * Get the presence duration for the entity
     * @return the presence duration for the entity
     */
    public long getPresenceDuration() {
        return presence.getDuration() ;
    }
    
    /**
     * Get the presence duration for the entity between the dates a and b
     * @param a begin date
     * @param b end date
     * @return the presence duration for the entity between the dates a and b
     */
    public long getPresenceDuration(Date a, Date b) {
        if (a.after(b)) {
            Control.presenceLog.warning("Begin is after end") ;
            throw new IllegalArgumentException("Begin is after end") ;
        }
        return presence.getDuration(a, b) ;
    }
    
    /**
     * Get the presence percentage
     * @param a begin date
     * @param b end date
     * @return the presence percentage
     */
    public float getPresencePercentage(Date a, Date b) {
        if (a.after(b)) {
            Control.presenceLog.warning("Begin is after end") ;
            throw new IllegalArgumentException("Begin is after end") ;
        }
        float duration = b.getTime() - a.getTime() ;
        float presenceDuration = getPresenceDuration(a, b) ;
        return ((presenceDuration * 100) / duration)  ;
    }
    
    /**
     * Add a presence between a and b
     * @param a begin date
     * @param b end date
     * @param ci comment in
     * @param co comment out
     */
    public void addPresence(Date a, Date b, String ci, String co) {
        if (a.after(b)) {
            Control.presenceLog.warning("Begin is after end") ;
            throw new IllegalArgumentException("Begin is after end") ;
        }
        Period p = new Period(a, b, ci, co) ; 
        presence.addPresence(p) ;
        storage.addPresence(p) ;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the first date of presence
     * @return the first date of presence
     */
    public Date getBeginPresence() {
        return (presence.getPeriods())[0].getBegin() ;
    }
    
    /**
     * Add a date to the entity
     * @param d date
     * @param c
     */
    public void addBeginDate(Date d, String c) {
        storage.addBeginDate(d, c) ;
        presence = storage.getPresenceAgenda() ; 
    }
    
    /**
     * Add a date to the entity
     * @param d date
     * @param c comment
     */
    public void addEndDate(Date d, String c) {
        storage.addEndDate(d, c) ;
        presence = storage.getPresenceAgenda() ; 
    }
    
    /**
     * Get the last period of presence
     * @return the last period of presence
     * @throws IOException
     */
    public Period getLastPeriod() throws IOException {
        return storage.getLastPeriod() ;
    }

    /**
     * @return The end of presence
     * @throws IOException
     */
    public Date getEndPresence() throws IOException {
        Period lastPeriod = getLastPeriod() ;
        if (lastPeriod.getEnd() == null) {
            return lastPeriod.getBegin() ;
        } else {
            return lastPeriod.getEnd() ;
        }
    }
    
    /**
     * <code>IN</code> : Place
     */
    public static int IN = 1 ;
    /**
     * <code>OUT</code> : Place
     */
    public static int OUT = 2 ;
    
    /**
     * Get the presence place (IN or OUT)
     * @return the presence place (IN or OUT)
     * @throws IOException
     */
    public int getPresencePlace() throws IOException {
        Period lastPeriod = getLastPeriod() ;
        if (lastPeriod.getEnd() == null) {
            return IN ;
        } else {
            return OUT ;
        }
    }
    
    /**
     * Get the event agenda
     * @return the Event agenda
     */
    public EventAgenda getEventAgenda() {
        
        EventAgenda res = new EventAgenda() ;
        
        Period[] periods = presence.getPeriods() ;
        String[] eventName = {getName()} ; 
        for (int i=0; i < periods.length; i++) {
            res.addEvents(periods[i].getBegin(), eventName, Events.IN, periods[i].getCommentIn()) ;
            if (periods[i].getEnd() != null) {
                res.addEvents(periods[i].getEnd(), eventName, Events.OUT, periods[i].getCommentOut()) ;
            }
        }
        return res ;
    }
    
    /**
     * Store the entity
     */
    public void storeEntity() {
        storage.storeEntity(presence) ;
    }
}
