package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfile {
    private String fullName;
    private String email;
    private String joinedDate;
    private int totalBooksOwned;
    private int readHours;
    private String favoriteGenre;

    public UserProfile() {}

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getJoinedDate() { return joinedDate; }
    public void setJoinedDate(String joinedDate) { this.joinedDate = joinedDate; }

    public int getTotalBooksOwned() { return totalBooksOwned; }
    public void setTotalBooksOwned(int totalBooksOwned) { this.totalBooksOwned = totalBooksOwned; }

    public int getReadHours() { return readHours; }
    public void setReadHours(int readHours) { this.readHours = readHours; }

    public String getFavoriteGenre() { return favoriteGenre; }
    public void setFavoriteGenre(String favoriteGenre) { this.favoriteGenre = favoriteGenre; }
}
