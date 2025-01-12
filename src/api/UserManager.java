package api;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages user accounts, including administrators and customers.
 */
public class UserManager implements Serializable {
    private static final long serialVersionUID = 1L; // For serialization compatibility
    private static final String USERS_FILE = "users.dat"; // File for storing user data
    private List<User> users;

    /**
     * Constructor for creating a UserManager.
     * Loads existing users from the file or initializes a fresh list if the file is not found.
     */
    public UserManager() {
        this.users = new ArrayList<>();
        loadUsers();
        if (users.isEmpty()) {
            initializeDefaultUsers();
        }
    }

    /**
     * Adds a new user to the list.
     *
     * @param user the user to add
     * @throws UserAlreadyExistsException if the username is already taken
     */
    public synchronized void addUser(User user) throws UserAlreadyExistsException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (isUsernameTaken(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + user.getUsername());
        }
        users.add(user);
        saveUsers();
    }

    /**
     * Checks if a username is already taken.
     *
     * @param username the username to check
     * @return true if the username is taken, false otherwise
     */
    public synchronized boolean isUsernameTaken(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        return users.stream().anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }

    /**
     * Gets an immutable list of users.
     *
     * @return an unmodifiable list of users
     */
    public synchronized List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    /**
     * Authenticates a user by username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the authenticated User object, or null if authentication fails
     */
    public synchronized User authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be null or empty.");
        }
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username) && user.authenticate(password))
                .findFirst()
                .orElse(null);
    }

    /**
     * Removes a user from the list.
     *
     * @param username the username of the user to remove
     * @throws UserNotFoundException if the user does not exist
     */
    public synchronized void removeUser(String username) throws UserNotFoundException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        User user = users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        users.remove(user);
        saveUsers();
    }

    /**
     * Updates a user's password.
     *
     * @param username    the username of the user
     * @param newPassword the new password to set
     * @throws UserNotFoundException if the user does not exist
     */
    public synchronized void updatePassword(String username, String newPassword) throws UserNotFoundException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be null or empty.");
        }
        User user = users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        user.setPassword(newPassword);
        saveUsers();
    }

    /**
     * Loads users from the file.
     */
    private synchronized void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            users = (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No user data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    /**
     * Saves users to the file.
     */
    private synchronized void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    /**
     * Initializes default administrators and customers.
     */
    private void initializeDefaultUsers() {
        try {
            addUser(new Administrator("admin1", "password1"));
            addUser(new Administrator("admin2", "password2"));
            addUser(new Customer("user1", "password1"));
            addUser(new Customer("user2", "password2"));
        } catch (UserAlreadyExistsException e) {
            System.err.println("Error initializing default users: " + e.getMessage());
        }
    }

    /**
     * Provides a summary of the current users for debugging.
     *
     * @return a string containing the list of users
     */
    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder("Current Users:\n");
        for (User user : users) {
            sb.append("- ").append(user.getUsername()).append(" (").append(user.getRole()).append(")\n");
        }
        return sb.toString();
    }
}

/**
 * Custom exception for a user not found.
 */
class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}