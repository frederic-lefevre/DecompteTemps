/*
 * Created on Nov 12, 2005
 *
 */
package org.fl.decompteTemps.util;

/**
 * @author Fr�d�ric Lef�vre
 *
 */
public class StringFormat {

    /**
     * @param str A string
     * @return If the string is not null, the string. If it is null, the empty string
     */
    public static String nullToEmpty(String str) {
    	if (str == null) {
    		return "";
    	} else {
    		return str;
    	}
    }
}
