package ru.aizen.mtg.domain.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.aizen.mtg.insfrastructure.persistence.EmailConverter;
import ru.aizen.mtg.insfrastructure.persistence.PhoneConverter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity(name = "profile")
public class Profile {

	private long profileOwnerId;

	@Id
	private long id;
	@Column(name = "full_name")
	private String fullName;
	@Column(name = "email")
	@Convert(converter = EmailConverter.class)
	private Email email;
	@Column(name = "phone")
	@Convert(converter = PhoneConverter.class)
	private Phone phone;
	@OneToOne(mappedBy = "profile", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Address address;

	private Profile(long profileOwnerId) {
		this.profileOwnerId = profileOwnerId;
	}

	public static Profile newProfile(long accountId) {
		return new Profile(accountId);
	}

	public void updateProfile(String fullName, String email, String phone) {
		if (fullName != null && !fullName.isEmpty()) {
			this.fullName = fullName;
		}
		if (email != null && !email.isEmpty()) {
			this.email = Email.from(email);
		}
		if (phone != null && !phone.isEmpty()) {
			this.phone = Phone.from(phone);
		}
	}

	public void updateAddress(String settlement, String street, String building, String apartment, Integer postIndex) {
		if (settlement != null && !settlement.isEmpty()) {
			this.address.setSettlement(settlement);
		}
		if (street != null && !street.isEmpty()) {
			this.address.setStreet(street);
		}
		if (building != null && !building.isEmpty()) {
			this.address.setBuilding(building);
		}
		if (apartment != null && !apartment.isEmpty()) {
			this.address.setApartment(apartment);
		}
		if (postIndex != null) {
			this.address.setPostIndex(postIndex);
		}
	}

	public void deleteAddress() {
		this.address = null;
	}

}
