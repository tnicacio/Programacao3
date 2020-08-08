package av3.dao;

import java.io.IOException;

@SuppressWarnings("serial")
public class DAOException extends Exception{
	
	public DAOException(IOException ex) {
		super("I/O Error: + ex.getMessage()");
		ex.printStackTrace();
	}

}
