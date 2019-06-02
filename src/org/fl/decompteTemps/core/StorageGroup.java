/*
 * Created on Nov 5, 2005
 *
 */
package org.fl.decompteTemps.core;

import java.io.File;

/**
 * @author Fr�d�ric Lef�vre
 *
 */
public class StorageGroup {

    private GroupEntity group ;
    private static final String ENTITY_EXTENTION = ".entity"; ;
    
    /**
     * Create a storage group
     * @param dir directory
     * 
     */
    public StorageGroup(File dir) {
        super();
        
        if ((dir == null) || (! dir.isDirectory())) {
            Control.presenceLog.severe("Argument is not a directory") ;
            throw new IllegalArgumentException("Argument is not a directory") ;
        }
        group = new GroupEntity() ;
        
        // scan directory
        File[] entityFiles = dir.listFiles() ;
        File currFile ;
        for (int i=0; i < entityFiles.length; i++) {
            
            currFile = entityFiles[i] ;
            // check that file is an entity
            if ((currFile.isFile()) && (currFile.getName().endsWith(ENTITY_EXTENTION))) {
                Control.presenceLog.fine("Found an entity " + currFile.getAbsolutePath()) ;
                group.addEntity(new Entity(new StorageEntity(currFile))) ;
            }
        }
        
    }

    /**
     * Get the group entity corresponding to this storage 
     * @return the group entity corresponding to this storage
     */
    public GroupEntity getGroupEntity() {
        return group ;
    }
}
