package com.openlib.market.infrastructure.adapter.in.web.dto;

public class SupportTicketDto {
    private String id;
    private String userEmail;
    private String subject;
    private String description;
    private String priority;
    private String status;
    private String createdAt;

    public SupportTicketDto() {}

    public SupportTicketDto(String id, String userEmail, String subject, String description, String priority, String status, String createdAt) {
        this.id = id;
        this.userEmail = userEmail;
        this.subject = subject;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
