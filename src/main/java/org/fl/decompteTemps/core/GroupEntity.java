package org.fl.decompteTemps.core;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GroupEntity {
    
    private final static String dateFrancePattern 		   = "EEEE dd MMMM yyyy à HH:mm:ss.SSS" ;
    private final static SimpleDateFormat dateFranceFormat = new SimpleDateFormat(dateFrancePattern, Locale.FRANCE);
    
    private List<Entity> entities ;
    
    public GroupEntity() {
        super();
        entities = new ArrayList<Entity>() ;      
    }

    public void addEntity(Entity e) {
        entities.add(e) ;
    }
    
    public long getPresenceDurationTotal(Date a, Date b) {
        if (a.after(b)) {
            Control.presenceLog.warning("Begin is after end") ;
            throw new IllegalArgumentException("Begin is after end") ;
        }
        Control.presenceLog.finer("Total presence between " + dateFranceFormat.format(a) + " and "  + dateFranceFormat.format(b)) ;
        long duration = 0 ;
        for (int i=0; i < entities.size(); i++) {
            duration = duration + ((Entity)entities.get(i)).getPresenceDuration(a, b) ;
            Control.presenceLog.finer("Presence duration for " + ((Entity)entities.get(i)).getName() + " " + duration) ;
        }
        return duration ;
    }
    
    public long getPresenceDurationPerEntity(Date a, Date b) {
        if (a.after(b))  {
            Control.presenceLog.warning("Begin is after end") ;
            throw new IllegalArgumentException("Begin is after end") ;
        }
        long duration = 0 ;
        for (int i=0; i < entities.size(); i++) {
            duration = duration + ((Entity)entities.get(i)).getPresenceDuration(a, b) ;
        }
        Control.presenceLog.finer("Presence duration between " + dateFranceFormat.format(a) + " and "  + dateFranceFormat.format(b) + "= " + duration) ;
        return (duration / entities.size()) ;
    }
    
    public float getPresencePercentage(Date a, Date b) {
        if (a.after(b)) {
            Control.presenceLog.warning("Begin is after end") ;
            throw new IllegalArgumentException("Begin is after end") ;
        }
        float duration = b.getTime() - a.getTime() ;
        float presenceDuration = getPresenceDurationPerEntity(a, b) ;
        return ((presenceDuration * 100) / duration)  ;
    }
    
    public Entity[] getEntities() {
        
        Entity[] entityTab = new Entity[entities.size()];
        return (Entity[])entities.toArray(entityTab);
    }
    

    public Date getBeginPresence() {
        
        Date b = new Date() ;
        Date bEntity ;
        for (int i=0; i < entities.size(); i++) {
            bEntity = ((Entity) entities.get(i)).getBeginPresence() ;
            if (bEntity.before(b)) b = bEntity ;
        }
        return b;
    }
    
    public Date getEndPresence() throws IOException {
        
        Date e = new Date() ;
        Date eEntity ;
        for (int i=0; i < entities.size(); i++) {
            eEntity = ((Entity) entities.get(i)).getEndPresence() ;
            if (eEntity.after(e)) e = eEntity ;
        }
        return e;
    }
    

    public static int IN  = Entity.IN ;
    public static int OUT = Entity.OUT ;

    // the calculus is a way to generate a number different from IN and OUT
    public static int MIXED = 2*(Entity.IN+Entity.OUT) ;
    
    public int getPresencePlace() throws IOException {
        
        if (entities.size() == 0) {
            return MIXED ;
        } else {
            int res = ((Entity) entities.get(0)).getPresencePlace() ;
           
            for (int i=1; i < entities.size(); i++) {
                if (((Entity) entities.get(i)).getPresencePlace() != res) {
                    return MIXED ;
                }
        	}
            return res ;
        }
    }

    public void addBeginDate(Date d, String c) {
        for (int i=0; i < entities.size(); i++) {
            ((Entity)(entities.get(i))).addBeginDate(d, c) ;
            // comment only on one entity
            c = "" ;
        }
    }
    

    public void addEndDate(Date d, String c) {
        for (int i=0; i < entities.size(); i++) {
            ((Entity)(entities.get(i))).addEndDate(d, c) ;
            // comment only on one entity
            c = "" ;
        }
    }
    
    /**
     * Get the event agenda of the group
     * @return the event agenda of the group
     */
    public EventAgenda getEventsAgenda() {
        
        EventAgenda agd = ((Entity) entities.get(0)).getEventAgenda() ;
        EventAgenda agdNext ;
        for (int i=1; i < entities.size(); i++) {
            agdNext = ((Entity) entities.get(i)).getEventAgenda() ;
            agd.mergeEventAgenda(agdNext) ;
        }
        return agd ;
    }
    
    public Entity getEntityByName(String name) {
        
    	Entity res ;
        for (int i=0; i < entities.size(); i++) {
        	res = (Entity)entities.get(i) ;
            if (name.equals(res.getName())) {
                return res;
            }
        }
        return null ;
    }

}
