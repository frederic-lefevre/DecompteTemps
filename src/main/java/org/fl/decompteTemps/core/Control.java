/*
 * MIT License

Copyright (c) 2017, 2025 Frederic Lefevre

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

import java.net.URI;
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
    private static final Logger presenceLog = Logger.getLogger(Control.class.getName());
    
    private static final String DEFAULT_PROP_FILE = "presence.properties";

    private static Path presenceDirectoryName = null;

    private static boolean initialized = false; 
    
    private static StorageGroup storageGroup;
    private static GroupEntity completeGroup;
    private static GroupEntity currentGroup;
    
    private static Date    endDate = null;
    private static boolean endDateIsNow ;
    
    public static void init() {

        if (! initialized) {
        	forceInit() ;
        }
    }

	private static void forceInit() {

		// access to properties and logger
		RunningContext tempsRunningContext = new RunningContext("org.fl.decompteTemps", URI.create(DEFAULT_PROP_FILE));

		AdvancedProperties props = tempsRunningContext.getProps();

		// Get the root directory
		presenceDirectoryName = props.getPathFromURI("presence.rootDir.name");

		// Get the end date
		String ed = props.getProperty("presence.endDate");
		if ((ed == null) || ed.isEmpty()) {
			// if end date is not defined, end date is now
			endDate = new Date();
			endDateIsNow = true;
		} else {
			try {
				endDateIsNow = false;
				endDate = AgendaFormat.getDate(ed, "00", "00");
			} catch (ParseException e) {
				presenceLog.log(Level.SEVERE, "Erreur de parsing sur la propriété presence.endDate: ", e);
				endDate = new Date();
				endDateIsNow = true;
			}
		}

		storageGroup = new StorageGroup(presenceDirectoryName);
		completeGroup = storageGroup.getGroupEntity();
		currentGroup = storageGroup.getGroupEntity();

		initialized = true;
	}
    
    /**
     * @return Returns the presenceDirectoryName.
     */
    public static Path getPresenceDirectoryName() {
        if (presenceDirectoryName == null) {
        	forceInit() ;
        }
        return presenceDirectoryName;
    }
    
	
    public static GroupEntity getCurrentGroup() {
        if (! initialized) {
        	forceInit() ;
        }
		return currentGroup;
	}
	
    public static void setCurrentGroup(GroupEntity gr) {
        if (! initialized) {
        	forceInit() ;
        }
		currentGroup = gr;
	}
    
    public static GroupEntity getCompleteGroup() {
        if (! initialized) {
        	forceInit() ;
        }
		return completeGroup;
	}
    
    /**
     * @param entityName
     * @return a GroupEntity composed of a single entity
     */
    public static GroupEntity getIndividualEntityAsGroups(String entityName) {
        
        Entity e ;
        Entity[] entities = getCompleteGroup().getEntities();
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
        if (endDate == null) {
        	forceInit() ;
        }
		if (endDateIsNow) {
			endDate = new Date() ;
		}
		return endDate;
	}
}
