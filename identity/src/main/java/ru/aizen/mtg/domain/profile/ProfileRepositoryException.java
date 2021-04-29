package ru.aizen.mtg.domain.profile;

public class ProfileRepositoryException extends Exception {

	public ProfileRepositoryException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

}
