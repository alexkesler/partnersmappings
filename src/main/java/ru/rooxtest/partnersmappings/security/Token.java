package ru.rooxtest.partnersmappings.security;

/**
 * Класс, содержащий информацию о пользователе.
 * может частично или полностью содержаться в зашифрованном виде
 * в токене авторизации
 */
public class Token {
    private String userName;
    private String password;

    public Token(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

}
