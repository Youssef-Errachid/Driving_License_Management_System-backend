package com.drivinglicense.service;

public interface EmailService {
    void sendUserCredentials(String toEmail, String fullName, String rawPassword, String role);
}