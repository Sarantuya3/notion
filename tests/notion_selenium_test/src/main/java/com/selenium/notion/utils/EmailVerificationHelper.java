package com.selenium.notion.utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class for automated email verification code retrieval.
 * Supports Gmail IMAP for reading verification emails.
 */
public class EmailVerificationHelper {
    
    private final String email;
    private final String password;
    private final String imapHost;
    private final int imapPort;
    
    public EmailVerificationHelper(String email, String password) {
        this.email = email;
        this.password = password;
        this.imapHost = "imap.gmail.com"; // For Gmail
        this.imapPort = 993;
    }
    
    /**
     * Retrieves the latest verification code from Notion emails.
     * 
     * @param timeoutSeconds Maximum time to wait for email
     * @return The verification code, or null if not found
     */
    public String getNotionVerificationCode(int timeoutSeconds) {
        try {
            System.out.println("Connecting to email to retrieve verification code...");
            
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");
            props.put("mail.imaps.host", imapHost);
            props.put("mail.imaps.port", imapPort);
            props.put("mail.imaps.ssl.enable", "true");
            
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore("imaps");
            store.connect(imapHost, email, password);
            
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            
            // Wait for new email with timeout
            long startTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - startTime) < (timeoutSeconds * 1000)) {
                
                // Get unread messages
                Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
                
                for (Message message : messages) {
                    if (message.getSubject().toLowerCase().contains("notion") && 
                        message.getSubject().toLowerCase().contains("code")) {
                        
                        String content = getTextContent(message);
                        String code = extractVerificationCode(content);
                        
                        if (code != null) {
                            System.out.println("Found verification code: " + code);
                            inbox.close(false);
                            store.close();
                            return code;
                        }
                    }
                }
                
                Thread.sleep(2000); // Check every 2 seconds
            }
            
            inbox.close(false);
            store.close();
            System.out.println("No verification code found within timeout");
            return null;
            
        } catch (Exception e) {
            System.out.println("Error retrieving verification code: " + e.getMessage());
            return null;
        }
    }
    
    private String getTextContent(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return (String) message.getContent();
        } else if (message.isMimeType("text/html")) {
            return (String) message.getContent();
        } else if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();
            return getTextFromMultipart(multipart);
        }
        return "";
    }
    
    private String getTextFromMultipart(Multipart multipart) throws Exception {
        StringBuilder result = new StringBuilder();
        int count = multipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent().toString());
            } else if (bodyPart.isMimeType("text/html")) {
                result.append(bodyPart.getContent().toString());
            }
        }
        return result.toString();
    }
    
    /**
     * Extracts verification code from email content using regex patterns.
     */
    private String extractVerificationCode(String content) {
        // Common patterns for verification codes
        String[] patterns = {
            "\\b\\d{6}\\b",           // 6-digit code
            "\\b\\d{4}\\b",           // 4-digit code  
            "code:\\s*(\\d+)",        // "code: 123456"
            "verification.*?(\\d{4,6})", // "verification code 123456"
            "\\b[A-Z0-9]{6}\\b"       // 6-character alphanumeric
        };
        
        for (String patternStr : patterns) {
            Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                return matcher.group(matcher.groupCount() > 0 ? 1 : 0);
            }
        }
        
        return null;
    }
}