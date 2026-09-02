package com.sms.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "org_info", uniqueConstraints = {
        @UniqueConstraint(columnNames = "orgname"),
        @UniqueConstraint(columnNames = "email")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "org_name", length = 50)
    private String orgname;

    @Column(name ="org_email", length = 100)
    private String email;

    @Column(name="org_description", length = 50)
    private String description;

    @Column(name = "org_legal_name", length = 50)
    private String legalName;

    @Column(name="org_phone", length = 50)
    private String phone;

    @Column(name="org_website", length = 200)
    private String website;

    @Column(name="org_country", length = 50)
    private String country;

    @Column(name = "org_state", length = 50)
    private String state;

    @Column(name = "org_city", length = 50)
    private String city;

    @Column(name="address1", length = 200)
    private String address1;

    @Column(name = "address2", length = 200)
    private String address2;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
