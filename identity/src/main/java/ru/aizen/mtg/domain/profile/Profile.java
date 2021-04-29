package ru.aizen.mtg.domain.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.aizen.mtg.domain.account.Account;
import ru.aizen.mtg.insfrastructure.persistence.EmailConverter;
import ru.aizen.mtg.insfrastructure.persistence.PhoneConverter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity(name = "profile")
public class Profile {

	@Id
	@Column(name = "account_id")
	private Long id;
	@OneToOne
	@MapsId
	@JoinColumn(name = "account_id")
	private Account account;

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

	private Profile(Account account) {
		this.account = account;
		this.id = account.getId();
	}

	public static Profile createFor(Account account) {
		return new Profile(account);
	}

	public void update(String fullName, String email, String phone) {
		this.fullName = fullName;
		this.email = Email.from(email);
		this.phone = Phone.from(phone);
	}

	public void updateAddress(String settlement, String street, String building, String apartment, Integer postIndex) {
		if (this.address == null) {
			address = Address.create(this);
		}
		this.address.setSettlement(settlement);
		this.address.setStreet(street);
		this.address.setBuilding(building);
		this.address.setApartment(apartment);
		this.address.setPostIndex(postIndex);
	}

	public void deleteAddress() {
		this.address = null;
	}

}
