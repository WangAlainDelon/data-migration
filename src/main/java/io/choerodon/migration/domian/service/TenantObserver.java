package io.choerodon.migration.domian.service;


import io.choerodon.migration.domian.hzero.Tenant;

import javax.annotation.Nonnull;

/**
 * 租户观察者
 *
 * @author bojiangzhou 2020/04/27
 */
public interface TenantObserver<T> extends Comparable<TenantObserver<T>> {

    /**
     * 观察者执行顺序，数字越小，优先级越高
     *
     * @return 优先级
     */
    int order();

    /**
     * 租户创建事件
     *
     * @param tenant 新创建的租户信息
     * @return 租户
     */
    T tenantCreate(@Nonnull Tenant tenant);

    /**
     * 租户更新事件
     *
     * @param tenant 更新后的租户信息
     * @return 租户
     */
    default T tenantUpdate(@Nonnull Tenant tenant) {
        return null;
    }

    @Override
    default int compareTo(TenantObserver o) {
        return this.order() - o.order();
    }
}
