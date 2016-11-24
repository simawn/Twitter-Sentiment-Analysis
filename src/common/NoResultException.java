package common;

public class NoResultException extends Exception{
	public NoResultException(){
		super("No results found!");
	}
}
