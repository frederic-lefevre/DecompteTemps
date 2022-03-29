package org.fl.decompteTemps.core;

import java.nio.file.Path;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.decompteTemps.util.AgendaFormat;
import org.fl.util.AdvancedProperties;
import org.fl.util.RunningContext;

public final class Control {

	// logger Manager
    public static Logger presenceLog ;
    
    private static final String DEFAULT_PROP_FILE = "presence.properties";

    private static Path presenceDirectoryName;

    private static boolean initialized = false ; 
    
    private static GroupEntity completeGroup ;
    private static GroupEntity currentGroup ;
    
    private static Date    endDate ;
    private static boolean endDateIsNow ;
    
    public static void init() {

        if (! initialized) {
        	forceInit() ;
        }
    }

    public static void forceInit() {
    	
    	//access to properties and logger
		RunningContext tempsRunningContext = new RunningContext("DecompteTemps", null, DEFAULT_PROP_FILE);
	
		AdvancedProperties props = tempsRunningContext.getProps() ;

        // Initialize logger
        presenceLog = tempsRunningContext.getpLog() ;

        // Get the root directory
        presenceDirectoryName = props.getPathFromURI("presence.rootDir.name") ;
    
        // Get the end date
        String ed = props.getProperty("presence.endDate") ;
        if ((ed == null) || ed.isEmpty()) {
        	// if end date is not defined, end date is now
        	endDate = new Date() ;
        	endDateIsNow = true ;
        } else {
	        try {
	        	endDateIsNow = false ;
				endDate = AgendaFormat.getDate(ed, "00", "00") ;
			} catch (ParseException e) {
				presenceLog.log(Level.SEVERE, "Erreur de parsing sur la propriété presence.endDate: ", e) ;
				endDate = new Date() ;
				endDateIsNow = true ;
			}
        }
        
        StorageGroup st = new StorageGroup(Control.getPresenceDirectoryName());
        completeGroup = st.getGroupEntity() ;
        currentGroup = st.getGroupEntity() ;
        
        initialized = true ;
    }
    

    /**
     * @return Returns the presenceDirectoryName.
     */
    public static Path getPresenceDirectoryName() {
        return presenceDirectoryName;
    }
    
	
    public static GroupEntity getCurrentGroup() {
		return currentGroup;
	}
	
    public static void setCurrentGroup(GroupEntity gr) {
		currentGroup = gr;
	}
    
    public static GroupEntity getCompleteGroup() {
		return completeGroup;
	}
    
    /**
     * @param entityName
     * @return a GroupEntity composed of a single entity
     */
    public static GroupEntity getIndividualEntityAsGroups(String entityName) {
        
        Entity e ;
        Entity[] entities = completeGroup.getEntities();
        for (int i=0; i < entities.length; i++) {
            e = (Entity)entities[i] ;
            if (e.getName().equals(entityName)) {
                GroupEntity res = new GroupEntity() ;
                res.addEntity(e) ;
                return res ;
            }
        }
        return null ;
    }

	public static Date getEndDate() {
		if (endDateIsNow) {
			endDate = new Date() ;
		}
		return endDate;
	}
}
