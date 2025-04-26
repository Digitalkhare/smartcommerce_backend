package smartcommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smartcommerce.model.AppSettings;

public interface AppSettingsRepository extends JpaRepository<AppSettings, String> {
	
}
