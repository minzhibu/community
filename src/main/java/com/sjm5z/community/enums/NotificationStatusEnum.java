package com.sjm5z.community.enums;

public enum NotificationStatusEnum {
    UNRAED(0L),
    READ(1L);
    ;
    private Long status;

    public Long getStatus() {
        return status;
    }

    NotificationStatusEnum(Long status) {
        this.status = status;
    }
}
