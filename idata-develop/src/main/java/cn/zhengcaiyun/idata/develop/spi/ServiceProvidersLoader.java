/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.zhengcaiyun.idata.develop.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-08 15:10
 **/
@Component
public class ServiceProvidersLoader implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProvidersLoader.class);

    private static final String PROVIDERS_DIR = "META-INF/providers/";
    private static final ConcurrentMap<Class<?>, Map<String, Object>> PROVIDERS_MAP = new ConcurrentHashMap<>();
    private ApplicationContext applicationContext;

    public Object load(Class<?> type, String identity) {
        return loadAll(type).get(identity);
    }

    public Object load(Class<?> type, ClassLoader classLoader, String identity) {
        return loadAll(type, classLoader).get(identity);
    }

    public Map<String, Object> loadAll(Class<?> type) {
        return loadAll(type, null);
    }

    public Map<String, Object> loadAll(Class<?> type, ClassLoader classLoader) {
        Objects.requireNonNull(type, "type is null");
        if (!type.isInterface()) {
            throw new IllegalArgumentException("type (" + type + ") is not an interface");
        }
        ClassLoader usingClassLoader = classLoader;
        if (usingClassLoader == null) {
            usingClassLoader = ServiceProvidersLoader.class.getClassLoader();
        }

        Map<String, Object> providers = PROVIDERS_MAP.get(type);
        if (providers == null) {
            synchronized (ServiceProvidersLoader.class) {
                providers = PROVIDERS_MAP.get(type);
                if (providers == null) {
                    PROVIDERS_MAP.putIfAbsent(type, loadProviders(type, usingClassLoader));
                    providers = PROVIDERS_MAP.get(type);
                }
            }
        }
        return providers;
    }

    private Map<String, Object> loadProviders(Class<?> type, ClassLoader classLoader) {
        Map<String, String> providerNames = loadProviderNames(type, classLoader);
        if (providerNames == null || providerNames.size() == 0) {
            return new HashMap<>();
        }

        Map<String, Object> providers = new HashMap<>();
        Set<Map.Entry<String, String>> entrySet = providerNames.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            Object bean = loadProviderBeans(entry.getKey(), entry.getValue(), classLoader);
            if (bean != null) {
                providers.put(entry.getKey(), bean);
            }
        }
        return providers;
    }

    private Map<String, String> loadProviderNames(Class<?> type, ClassLoader classLoader) {
        Map<String, String> providerNames = new HashMap<>();
        try {
            String providerFilePath = PROVIDERS_DIR + type.getName();
            Enumeration<URL> urls;
            if (classLoader != null) {
                urls = classLoader.getResources(providerFilePath);
            } else {
                urls = ClassLoader.getSystemResources(providerFilePath);
            }

            if (urls != null) {
                while (urls.hasMoreElements()) {
                    URL resourcesURL = urls.nextElement();
                    loadProviderNamesFromResources(providerNames, resourcesURL);
                }
            }
        } catch (IOException ex) {
            LOGGER.warn("Load provider: " + type.getName() + " exception: {}", ex);
            providerNames.clear();
            return providerNames;
        }
        return providerNames;
    }


    private void loadProviderNamesFromResources(Map<String, String> providerNames, URL resourceURL) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceURL.openStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final int commentIdx = line.indexOf('#');
                if (commentIdx >= 0) {
                    line = line.substring(0, commentIdx);
                }
                line = line.trim();
                if (line.length() > 0) {
                    String providerIdentity = null;
                    String providerClass = null;
                    int idx = line.indexOf('=');
                    if (idx > 0) {
                        providerIdentity = line.substring(0, idx).trim();
                        providerClass = line.substring(idx + 1).trim();
                    }
                    if (providerClass != null) {
                        if (providerNames.get(providerIdentity) == null) {
                            providerNames.put(providerIdentity, providerClass);
                        } else {
                            LOGGER.warn("The provider identity: " + providerIdentity + " is duplicate with class: " + providerClass);
                        }
                    } else {
                        LOGGER.warn("The provider: " + line + " is not invalid");
                    }
                }
            }
        }
    }

    private Object loadProviderBeans(String providerIdentity, String providerClass, ClassLoader classLoader) {
        try {
            Class<?> providerType = Class.forName(providerClass, true, classLoader);
            return generateBeanWithSpring(providerIdentity, providerType);
        } catch (ClassNotFoundException ex) {
            LOGGER.warn("Load class: " + providerClass + " exception: {}", ex);
        }
        return null;
    }

    private Object generateBeanWithSpring(String providerIdentity, Class<?> providerType) {
        String beanName = providerIdentity.concat(providerType.getSimpleName());

        // 通过BeanDefinitionBuilder创建bean定义
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(providerType);
        GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
        definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_NAME);

        //将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) this.applicationContext;
        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        // 注册bean
        defaultListableBeanFactory.registerBeanDefinition(beanName, definition);

        return this.applicationContext.getBean(beanName);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
