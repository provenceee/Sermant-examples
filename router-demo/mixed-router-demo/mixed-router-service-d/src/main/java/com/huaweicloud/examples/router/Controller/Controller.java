/*
 * Copyright (C) 2022-2022 Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huaweicloud.examples.router.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller
 *
 * @author provenceee
 * @since 2023-02-02
 */
@RestController
public class Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    @Value("${spring.application.name}")
    private String name;

    @Value("${service_meta_version:${SERVICE_META_VERSION:${service.meta.version:1.0.0}}}")
    private String version;

    @Value("${service_meta_parameters:${SERVICE_META_PARAMETERS:${service.meta.parameters:}}}")
    private String metadata;

    /**
     * 测试方法
     *
     * @return msg
     */
    @GetMapping("hello")
    public Map<String, Object> hello(@RequestHeader Map<String, String> header) {
        LOGGER.info("header is :{}", header);
        Map<String, Object> meta = new HashMap<>();
        meta.put("SERVICE_META_PARAMETERS", metadata);
        meta.put("SERVICE_META_VERSION", version);
        Map<String, Object> map = new HashMap<>();
        map.put("header", header);
        map.put("meta", meta);
        Map<String, Object> result = new HashMap<>();
        result.put(name, map);
        return result;
    }
}