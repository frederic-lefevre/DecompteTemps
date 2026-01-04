/*
 * MIT License

Copyright (c) 2017, 2026 Frederic Lefevre

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

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fl.decompteTemps.gui.DecompteTempsGui;
import org.fl.decompteTemps.util.AgendaFormat;
import org.fl.util.AdvancedProperties;
import org.fl.util.RunningContext;
import org.fl.util.file.FilesUtils;

public final class Control {

    private static final Logger presenceLog = Logger.getLogger(Control.class.getName());

    private static Control instance;
    
    private Path presenceDirectoryName = null;
    
    private StorageGroup storageGroup;
    private GroupEntity completeGroup;
    private GroupEntity currentGroup;
    
    private Date endDate;
    private boolean endDateIsNow ;
    
    private static Control getInstance() {
    	if (instance == null) {
    		instance = new Control(DecompteTempsGui.getRunningContext());
    	}
    	return instance;
    }
    
    private Control() {    	
    }
    
	private Control(RunningContext runningContext) {

		AdvancedProperties props = runningContext.getProps();

		// Get the root directory
		try {
			presenceDirectoryName = FilesUtils.uriStringToAbsolutePath(props.getProperty("presence.rootDir.name"));
		} catch (URISyntaxException e) {
			String errorMessage = "Erreur de parsing sur la propriété presence.rootDir.name";
			presenceLog.log(Level.SEVERE, errorMessage, e);
			throw new IllegalArgumentException(errorMessage, e);
		}

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
	}

    public static Path getPresenceDirectoryName() {
        return getInstance().presenceDirectoryName;
    }
    
    public static GroupEntity getCurrentGroup() {
		return getInstance().getInstanceCurrentGroup();
	}
	
    private StorageGroup getInstanceStorageGroup() {
    	// Lazy get to avoid stack overflow (loop) during init
    	if (storageGroup == null) {
    		storageGroup = new StorageGroup(presenceDirectoryName);
    	}
    	return storageGroup;
    	
    }
    
    private GroupEntity getInstanceCurrentGroup() {
    	// Lazy get to avoid stack overflow (loop) during init
    	if (currentGroup == null) {
    		currentGroup = getInstanceStorageGroup().getGroupEntity();
    	}
    	return currentGroup;
    }
    
    private GroupEntity getInstancCompleteGroup() {
    	// Lazy get to avoid stack overflow (loop) during init
    	if (completeGroup == null) {
    		completeGroup = getInstanceStorageGroup().getGroupEntity();
    	}
    	return completeGroup;
    }
    
    public static void setCurrentGroup(GroupEntity gr) {
    	getInstance().currentGroup = gr;
	}
    
    public static GroupEntity getCompleteGroup() {
		return getInstance().getInstancCompleteGroup();
	}
    
    public static GroupEntity getIndividualEntityAsGroups(String entityName) {
        
        Entity e;
        Entity[] entities = getCompleteGroup().getEntities();
        for (int i=0; i < entities.length; i++) {
            e = entities[i];
            if (e.getName().equals(entityName)) {
                GroupEntity res = new GroupEntity();
                res.addEntity(e);
                return res;
            }
        }
        return null;
    }

	public static Date getEndDate() {
		if (getInstance().endDateIsNow) {
			getInstance().endDate = new Date();
		}
		return getInstance().endDate;
	}
}
