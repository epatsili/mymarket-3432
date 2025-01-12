package api;

import java.io.Serializable;

/**
 * Abstract base class representing a user in the system.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L; // For serialization compatibility
    private final String userId;
    private final String username;
    private String password;

    /**
     * Constructor for creating a user.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    public User(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        this.userId = java.util.UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Authenticates the user by password.
     *
     * @param password the password to check
     * @return true if the password matches, false otherwise
     */
    public boolean authenticate(String password) {
        if (password == null) {
            return false;
        }
        return this.password.equals(password);
    }

    /**
     * Sets a new password for the user.
     *
     * @param newPassword the new password
     */
    public void setPassword(String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        this.password = newPassword;
    }

    /**
     * Abstract method to get the role of the user.
     *
     * @return the role of the user
     */
    public abstract String getRole();

    /**
     * Provides a string representation of the user.
     *
     * @return a string containing user details
     */
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", role='" + getRole() + '\'' +
                '}';
    }

    /**
     * Validates the user credentials (username and password).
     *
     * @param username the username to check
     * @param password the password to check
     * @return true if credentials match, false otherwise
     */
    public boolean validateCredentials(String username, String password) {
        return this.username.equals(username) && authenticate(password);
    }
}
