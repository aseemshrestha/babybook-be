package com.babybook.email.seed;

import com.babybook.email.constans.Gender;
import com.babybook.email.constans.RoleBuilder;
import com.babybook.email.model.Baby;
import com.babybook.email.model.Role;
import com.babybook.email.model.User;
import com.babybook.service.BabyService;
import com.babybook.service.RoleService;
import com.babybook.service.UserService;
import com.babybook.email.utils.ConfigUtility;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class DbInit implements CommandLineRunner
{

    private final ConfigUtility configUtility;
    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final BabyService babyService;

    public DbInit(ConfigUtility configUtility, RoleService roleService, UserService userService,
        PasswordEncoder passwordEncoder, BabyService babyService)
    {
        this.configUtility = configUtility;
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.babyService = babyService;
    }

    @Override
    public void run(String... args) throws Exception
    {

        String property = configUtility.getProperty("spring.jpa.hibernate.ddl-auto");
        String[] profiles = configUtility.getActiveProfiles();
        if (profiles.length == 1) {
            if ((profiles[0].equalsIgnoreCase("local") && !property.equalsIgnoreCase("create"))) {
                return;
            }
        }
        List<Role> roles = new ArrayList<>();

        roles.add(RoleBuilder.getRoleAdmin());
        roles.add(RoleBuilder.getRoleRegisteredUser());
        roles.add(RoleBuilder.getRoleSiteUser());

        roles.forEach(r -> roleService.saveRole(r));
        createUser();
        createBaby();

    }

    private List<User> listOfUsers()
    {
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setFirstName("Teena");
        user1.setLastName("Smith1");
        user1.setPassword(this.passwordEncoder.encode("password"));
        user1.setEmail("shrestha1.asm@gmail.com");
        user1.setIsActive(1);
        user1.setRole(RoleBuilder.getRoleRegisteredUser());
        user1.setCreated(new Date());
        user1.setUsername("shrestha1.asm@gmail.com");
        user1.setIp("127.0.0.1");
        user1.setLastUpdated(new Date());
        user1.setGender("Female");

        User user2 = new User();
        user2.setFirstName("Reecha");
        user2.setLastName("Smith2");
        user2.setPassword(this.passwordEncoder.encode("password"));
        user2.setEmail("shrestha2.asm@gmail.com");
        user2.setIsActive(1);
        user2.setRole(RoleBuilder.getRoleRegisteredUser());
        user2.setCreated(new Date());
        user2.setUsername("shrestha2.asm@gmail.com");
        user2.setIp("127.0.0.1");
        user2.setLastUpdated(new Date());
        user2.setGender(Gender.Female.name());

        User user = new User();
        user.setFirstName("Brian");
        user.setLastName("Smith");
        user.setPassword(this.passwordEncoder.encode("password"));
        user.setEmail("shrestha.asm@gmail.com");
        user.setIsActive(1);
        user.setRole(RoleBuilder.getRoleAdmin());
        user.setCreated(new Date());
        user.setUsername("shrestha.asm@gmail.com");
        user.setIp("127.0.0.1");
        user.setLastUpdated(new Date());
        user.setGender(Gender.Female.name());

        userList.add(user);
        userList.add(user1);
        userList.add(user2);

        return userList;
    }

    private void createUser()
    {

        this.userService.saveAll(listOfUsers());

    }

    private void createBaby() {
        User user = this.userService.getUser("shrestha.asm@gmail.com").get();
        Baby baby = new Baby();
        baby.setFirstName("Brian_baby");
        baby.setLastName("Smith");
        baby.setDob(new Date());
        baby.setGender("Male");
        baby.setCreated(new Date());
        baby.setLastUpdated(new Date());
        baby.setUser(user);

        this.babyService.saveBaby(baby);


    }

}

