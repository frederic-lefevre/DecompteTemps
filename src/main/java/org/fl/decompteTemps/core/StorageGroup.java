/*
 * MIT License

Copyright (c) 2017, 2023 Frederic Lefevre

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

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageGroup {

	private static final Logger presenceLog = Control.getLogger();
	
    private final GroupEntity group ;
    private static final String ENTITY_EXTENTION = ".entity"; ;
    
    public StorageGroup(Path dir) {
        super();
        
        if ((dir == null) || (! Files.isDirectory(dir))) {
            presenceLog.severe("Argument is not a directory") ;
            throw new IllegalArgumentException("Argument is not a directory") ;
        }
        group = new GroupEntity() ;
        
        // scan directory
        try (DirectoryStream<Path> dirFileStream = Files.newDirectoryStream(dir)) {

        	for (Path currFile : dirFileStream) {
        		if ((Files.isRegularFile(currFile)) && (currFile.toString().endsWith(ENTITY_EXTENTION))) {
        			presenceLog.fine("Found an entity " + currFile) ;
        			group.addEntity(new Entity(new StorageEntity(currFile))) ;
        		}
        	}
        } catch (Exception e) {
        	presenceLog.log(Level.SEVERE, "Exception when scanning directory " + dir, e);
        }
        
    }

    public GroupEntity getGroupEntity() {
        return group ;
    }
}
