package uk.co.jpmorgan.contactmanager.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "international_dial_code")
    private String internationalDialCode;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "extension_number")
    private String extensionNumber;

    @Column(updatable = false)
    private Date created_At;
    private Date updated_At;

    @PrePersist
    protected void onCreate() {
        this.created_At = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated_At = new Date();
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Phone) {
            Phone that = (Phone) other;
            result = (this.getPhoneNumber().equals(that.getPhoneNumber())
                    && this.getInternationalDialCode().equals(that.getInternationalDialCode())
                    && this.getExtensionNumber().equals(that.getExtensionNumber()));
        }
        return result;
    }

    @Override
    public String toString() {
        return "Phone: Inter Dial Code- " + this.internationalDialCode + " Main: "
                + this.phoneNumber + " Ext: " + this.extensionNumber;
    }
}
