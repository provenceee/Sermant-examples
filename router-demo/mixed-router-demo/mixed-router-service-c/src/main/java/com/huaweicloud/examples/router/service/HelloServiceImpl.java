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

package com.huaweicloud.examples.router.service;

import com.huaweicloud.examples.router.api.HelloService;
import com.huaweicloud.examples.router.client.ProviderClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * 测试接口
 *
 * @author provenceee
 * @since 2022-08-29
 */
public class HelloServiceImpl implements HelloService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProviderClient providerClient;

    @Value("${downstream.url:http://127.0.0.1:8203/hello}")
    private String url;

    @Value("${spring.application.name}")
    private String name;

    @Value("${service_meta_version:${SERVICE_META_VERSION:${service.meta.version:1.0.0}}}")
    private String version;

    @Value("${service_meta_parameters:${SERVICE_META_PARAMETERS:${service.meta.parameters:}}}")
    private String metadata;

    private String msg;

    @PostConstruct
    public void init() {
        msg = name + "[version: " + version + ", metadata: {" + metadata + "}] -> ";
    }

    /**
     * 测试方法
     *
     * @return msg
     */
    @Override
    public String hello() {
        return msg + providerClient.hello();
    }
}