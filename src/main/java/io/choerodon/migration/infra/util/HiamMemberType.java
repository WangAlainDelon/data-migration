package io.choerodon.migration.infra.util;


public enum HiamMemberType {
    /**
     * 用户类型
     */
    USER("user"),

    /**
     * 客户端
     */
    CLIENT("client");

    private String value;

    HiamMemberType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
