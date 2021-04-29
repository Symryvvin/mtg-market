package ru.aizen.mtg.domain.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
import ru.aizen.mtg.domain.profile.Profile;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

	Optional<Profile> findByProfileOwnerId(long ownerId);

}
