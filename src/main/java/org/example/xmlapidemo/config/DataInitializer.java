package org.example.xmlapidemo.config;

import org.example.xmlapidemo.entity.User;
import org.example.xmlapidemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Create sample users
            User user1 = new User("John Doe", "john.doe@example.com", "+1234567890", "123 Main St, City, Country");
            User user2 = new User("Jane Smith", "jane.smith@example.com", "+1234567891", "456 Oak Ave, City, Country");
            User user3 = new User("Bob Johnson", "bob.johnson@example.com", "+1234567892", "789 Pine Rd, City, Country");
            User user4 = new User("Alice Brown", "alice.brown@example.com", "+1234567893", "321 Elm St, City, Country");
            User user5 = new User("Charlie Wilson", "charlie.wilson@example.com", "+1234567894", "654 Maple Dr, City, Country");

            // Make one user inactive for testing
            user5.setActive(false);

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
            userRepository.save(user5);

            System.out.println("Sample data initialized!");
        }
    }
}
