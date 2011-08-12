
package com.kennyscott.diskusage;

import java.util.*;

public class Utility {

	public static String[] getDirectories( String path ) {
		path = path.replaceAll( "//", "/" ).replaceFirst( "/", "" );
		String[] directories = path.split( "/" );
		return directories;
	}

	public static <T> List<T> getListAsSet( Set<T> set ) {
		return new ArrayList<T>( set );
	}

}
