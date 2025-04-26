package smartcommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartcommerce.model.AppSettings;
import smartcommerce.repository.AppSettingsRepository;

import java.util.Optional;

@Service
public class SettingsService {

    @Autowired
    private AppSettingsRepository repo;

    public String getTtsMode() {
        return repo.findById("ttsMode").map(AppSettings::getValue).orElse("native"); // default to native
    }

    public void updateTtsMode(String mode) {
        AppSettings setting = new AppSettings();
        setting.setKey("ttsMode");
        setting.setValue(mode);
        repo.save(setting);
    }
}
