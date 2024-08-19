package com.huaweicloud.examples.router.controller;

import com.huaweicloud.common.configration.dynamic.GovernanceProperties;
import com.huaweicloud.servicecomb.discovery.registry.ServiceCombRegistration;
import com.huaweicloud.servicecomb.discovery.registry.ServiceCombServiceRegistry;

import org.apache.commons.lang.StringUtils;
import org.apache.servicecomb.service.center.client.model.MicroserviceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

/**
 * @author provenceee
 * @since 2024-08-16
 */
@RestController
public class GracefulController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GracefulController.class);

    @Autowired
    private ServiceCombServiceRegistry serviceCombServiceRegistry;

    @Autowired
    private ServiceCombRegistration serviceCombRegistration;

    @Value("${spring.cloud.servicecomb.instance.initial-status:UP}")
    private String initStatus;

    private final AtomicBoolean init = new AtomicBoolean();

    @PostConstruct
    public void init() {
        if (GovernanceProperties.GRASEFUL_STATUS_UPPER.equalsIgnoreCase(initStatus)) {
            init.set(true);
        }
    }

    @PostMapping("/gracefulUpperAndDown")
    public boolean gracefulUpperAndDown(@RequestBody StatusObj statusObj) {
        if (statusObj == null) {
            LOGGER.warn("status input is null.");
            return false;
        }
        String status = statusObj.getStatus();
        if (StringUtils.isEmpty(status) || (!GovernanceProperties.GRASEFUL_STATUS_UPPER.equalsIgnoreCase(status)
                && !GovernanceProperties.GRASEFUL_STATUS_DOWN.equalsIgnoreCase(status))) {
            LOGGER.warn("status input {} is not a valid value.", status);
            return false;
        }

        // 如果serviceId或instanceId为空，则说明调用这个接口更新状态时，实例还未注册
        MicroserviceInstance instance = serviceCombRegistration.getMicroserviceInstance();
        if (StringUtils.isEmpty(instance.getServiceId()) || StringUtils.isEmpty(instance.getInstanceId())) {
            LOGGER.warn("The instance is not registered.");
            return false;
        }
        LOGGER.info("Current status is {}, want to change to {}.", init.get(), status);
        boolean isUp = GovernanceProperties.GRASEFUL_STATUS_UPPER.equalsIgnoreCase(status);
        if (init.compareAndSet(!isUp, isUp)) {
            LOGGER.info("instance status is changed.");
            serviceCombServiceRegistry.setStatus(serviceCombRegistration, status.toUpperCase(Locale.ROOT));
            return true;
        }
        LOGGER.info("instance status is not changed.");
        return false;
    }

    public static class StatusObj {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}