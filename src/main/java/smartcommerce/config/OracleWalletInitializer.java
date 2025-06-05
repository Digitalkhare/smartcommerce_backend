package smartcommerce.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class OracleWalletInitializer {

    @PostConstruct
    public void init() throws IOException {
        List<String> walletFiles = List.of(
        		"cwallet.sso", "ewallet.p12", "ewallet.pem", "keystore.jks",
            "truststore.jks", "sqlnet.ora", "tnsnames.ora", "ojdbc.properties"
        );

        Path walletTempDir = Files.createTempDirectory("oracle_wallet");

        for (String file : walletFiles) {
            String path = "wallet/" + file;
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
                if (is == null) {
                    System.err.println("❌ Wallet file missing in JAR: " + path);
                    continue;
                }
                Path target = walletTempDir.resolve(file);
                Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("✅ Copied wallet file to: " + target);
            }
        }

        String walletPath = walletTempDir.toAbsolutePath().toString();

        // Oracle JDBC wallet + TNS setup
        System.setProperty("oracle.net.tns_admin", walletPath);
        System.setProperty("oracle.net.wallet_override", "true");
        System.setProperty("oracle.net.ssl_server_dn_match", "true");

        // SSL configuration
        //System.setProperty("javax.net.ssl.keyStore", walletPath + "/ewallet.p12");
        //System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
        //System.setProperty("javax.net.ssl.keyStorePassword", "**********"); // Replace with your actual wallet password

        //System.setProperty("javax.net.ssl.trustStore", walletPath + "/truststore.jks");
        //System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        //System.setProperty("javax.net.ssl.trustStorePassword", "**********"); // Replace with your actual wallet password

        // Log details
        System.out.println("✅ Set TNS_ADMIN to: " + walletPath);
        System.out.println("🔍 Contents of extracted wallet:");
        Files.list(walletTempDir).forEach(System.out::println);
    }
}
