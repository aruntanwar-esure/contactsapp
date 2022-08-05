package uk.co.jpmorgan.contactmanager.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "address")
public class Address {
	public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;

	@Column(name = "house_number", length = 5)
	private String houseNumber;

	@Column(name = "flat_number", length = 5)
	private String flatNumber;

	@Column(name = "building_number", length = 5)
	private String buildingNumber;

	@Column(name = "building_name", length = 50)
	private String buildingName;

	@Column(name = "street", nullable = false, length = 50)
	private String street;

	@Column(name = "county", length = 20)
	private String county;

	@Column(name = "postcode", nullable = false)
	private String postCode;

	@Column(name = "city", nullable = false, length = 20)
	private String city;

	@Column(name = "country_code", nullable = false, length = 2)
	private String countryCode;

	@Column(updatable = false)
	private LocalDate createdAt;
	private LocalDate updatedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDate.parse(LocalDate.now().toString(), dateFormatter);
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDate.parse(LocalDate.now().toString(), dateFormatter);
	}
}
