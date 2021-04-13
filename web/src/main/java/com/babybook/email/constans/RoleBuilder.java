package com.babybook.email.constans;

import com.babybook.email.model.Role;

public class RoleBuilder
{

    private static Role registeredUser;
    private static Role siteUser;
    private static Role admin;

    static {
        admin = Role.builder().id(1).role(Roles.ADMIN.name()).build();

        registeredUser = Role.builder().id(2).role(Roles.REGISTERED_USER.name()).build();

        siteUser = Role.builder().id(3).role(Roles.SITE_USER.name()).build();

    }

    public static Role getRoleAdmin()
    {
        return admin;
    }

    public static Role getRoleSiteUser()
    {
        return siteUser;
    }

    public static Role getRoleRegisteredUser()
    {
        return registeredUser;
    }
}
