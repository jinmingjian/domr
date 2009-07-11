package com.db4o.omplus.connection;

import java.io.*;

import com.db4o.*;
import com.db4o.ext.*;
import com.db4o.omplus.*;
import com.db4o.omplus.datalayer.*;

public class DbConnect {
	
	public final static String OLD_FORMAT = "Database is of old Format";

	public boolean connectToServer(
			RemoteConnectionParams params) throws Exception {
		try {
			String path = params.getPath();
			ObjectContainer db = Db4o.openClient(params.configure(), params.getHost(), params.getPort(), params.getUser(), params.getPassword());
			if(db != null){
				IDbInterface dbHandle = Activator.getDefault().getDatabaseInterface();
				dbHandle.setDB(db, path);
			}
			return true;
		} catch (OldFormatException e) {
			throw new Exception(OLD_FORMAT);
		} catch (InvalidPasswordException e) {
			throw new Exception("Invalid User Credentials ");
		} catch (Db4oException e) {
			throw new Exception("Could not connect to the remote database");
		}
	}

	public boolean connectToFile(ConnectionParams params) throws Exception {
		try {
			String path = params.getPath();
			assertFileExists(path);
			ObjectContainer db = Db4o.openFile(params.configure(), path);
			if(db != null) {
				IDbInterface dbHandle = Activator.getDefault().getDatabaseInterface();
				dbHandle.setDB(db, path);
			}
			return true;
		} catch (com.db4o.ext.DatabaseFileLockedException e) {
			throw new DatabaseFileLockedException("Database is locked by another thread" + params.getPath());
		} catch (OldFormatException e) {
			throw new Exception(OLD_FORMAT);
		} catch (IncompatibleFileFormatException e) {
			throw new Exception("Connection closed. Incompatible file format");
		} catch (DatabaseReadOnlyException e) {
			throw new Exception("Database is opened in readonly mode" + params.getPath());
		} catch (Db4oException e) {
			throw new Exception("Could not open database! Db4o exception raised");
		}/*catch (Gener e) {
			throw new Exception("Could not open database! Db4o exception raised");
		}*/
	}
	
	private void assertFileExists(String fullPath) throws FileNotFoundException {
		File f = new File(fullPath);
		if (!f.exists() || f.isDirectory()) {
			throw new FileNotFoundException("File not found: " + f.getAbsolutePath());
			}
	}
	
/*	public boolean dbOpened(){
		DbInterface db = DbInterface.getInstance();
		if(db.getDB() == null)
			return false;
		return true;
	}*/

}
