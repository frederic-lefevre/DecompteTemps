package org.fl.decompteTemps.core;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class StorageGroup {

    private final GroupEntity group ;
    private static final String ENTITY_EXTENTION = ".entity"; ;
    
    public StorageGroup(Path dir) {
        super();
        
        if ((dir == null) || (! Files.isDirectory(dir))) {
            Control.presenceLog.severe("Argument is not a directory") ;
            throw new IllegalArgumentException("Argument is not a directory") ;
        }
        group = new GroupEntity() ;
        
        // scan directory
        try (DirectoryStream<Path> dirFileStream = Files.newDirectoryStream(dir)) {

        	for (Path currFile : dirFileStream) {
        		if ((Files.isRegularFile(currFile)) && (currFile.toString().endsWith(ENTITY_EXTENTION))) {
        			Control.presenceLog.fine("Found an entity " + currFile) ;
        			group.addEntity(new Entity(new StorageEntity(currFile))) ;
        		}
        	}
        } catch (Exception e) {
        	Control.presenceLog.log(Level.SEVERE, "Exception when scanning directory " + dir, e);
        }
        
    }

    public GroupEntity getGroupEntity() {
        return group ;
    }
}
