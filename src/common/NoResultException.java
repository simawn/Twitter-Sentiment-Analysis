package common;

public class NoResultException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoResultException(){
		super("No results found!");
	}
}
