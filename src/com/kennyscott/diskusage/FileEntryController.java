
package com.kennyscott.diskusage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.icefaces.component.fileentry.FileEntry;
import org.icefaces.component.fileentry.FileEntryEvent;
import org.icefaces.component.fileentry.FileEntryResults;

@ManagedBean
@SessionScoped
public class FileEntryController {

	private DiskUsageBean root = null;
	private DiskUsageBean displayedBean = null;
	private String newChildDir = null;

	/*
	 * TODO: This totally needs refactoring
	 */
	public void listener( FileEntryEvent event ) {
		FileEntry fileEntry = (FileEntry) event.getSource();
		FileEntryResults results = fileEntry.getResults();
		for ( FileEntryResults.FileInfo fileInfo : results.getFiles() ) {
			if ( fileInfo.isSaved() ) {
				File inputFile = fileInfo.getFile();
				FileReader fr = null;
				try {
					fr = new FileReader( inputFile );
				}
				catch ( FileNotFoundException e ) {
					e.printStackTrace();
					System.exit( 1 );
				}
				BufferedReader reader = new BufferedReader( fr );
				String st = null;
				try {
					root = new DiskUsageBean( "__ROOT__" ); // reset it
					displayedBean = null;
					while ( (st = reader.readLine()) != null ) {
						String[] parts = st.split( "\t" );
						String[] directories = Utility.getDirectories( parts[1] );
						// System.out.println( ArrayUtils.toString( directories ) );
						DiskUsageBean currentBean = null;
						for ( int i = 0 ; i < directories.length ; i++ ) {
							String directory = directories[i];
							if ( currentBean == null ) {
								// must be the first directory in the stack
								currentBean = root;
							}

							DiskUsageBean nextBean = currentBean.getDiskUsageBean( directory );
							if ( nextBean == null ) {
								nextBean = new DiskUsageBean( directory );
								currentBean.addDiskUsageBean( directory, nextBean );
							}
							currentBean = nextBean;
							if ( i == directories.length - 1 ) {
								// the last in the path, so set the size
								nextBean.setSize( Integer.parseInt( parts[0] ) );
							}
						}
					}
				}
				catch ( IOException e ) {
					e.printStackTrace();
					System.exit( 1 );
				}
			}
		}
	}

	public void resetDisplayedBean() {
		displayedBean = null;
	}

	public DiskUsageBean getDiskUsageBean() {
		return root;
	}

	public String getDisplayedBeanInfo() {
		if ( root == null ) {
			return "No du uploaded yet";
		}

		if ( displayedBean == null ) {
			displayedBean = root;
		}
		return displayedBean.getName() + ", " + displayedBean.getSizeInGb() + ", " + displayedBean.getSizeOfFileContents() + " of files";
	}

	public List<DiskUsageBean> getDisplayedBeanChildren() {
		if ( displayedBean == null ) {
			return null;
		}

		return new ArrayList<DiskUsageBean>( displayedBean.getDirs().values() );
	}

	public void setChildDir() {
		displayedBean = displayedBean.getDiskUsageBean( this.getNewChildDir() );
	}

	public String getNewChildDir() {
		return newChildDir;
	}

	public void setNewChildDir( String newChildDir ) {
		this.newChildDir = newChildDir;
	}

}
