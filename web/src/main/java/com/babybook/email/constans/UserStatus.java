package com.babybook.email.constans;

public enum UserStatus
{
    ACTIVE(1), LOCKED(2), DELETED(3), DEACTIVATED(4);

    private final int value;

    UserStatus(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
