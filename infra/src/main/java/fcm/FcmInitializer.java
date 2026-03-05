package fcm;

import java.io.IOException;

@Component
@Slf4j
public class FcmInitializer {
    @Value("${fcm.firebase_config_path}")
    private String firebaseConfigPath;

    @PostConstruct
    public void init() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream());
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(googleCredentials)
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
