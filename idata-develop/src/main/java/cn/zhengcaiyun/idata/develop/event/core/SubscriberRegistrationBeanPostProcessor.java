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

package cn.zhengcaiyun.idata.develop.event.core;

import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.function.BiConsumer;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-05 14:37
 **/
public class SubscriberRegistrationBeanPostProcessor implements DestructionAwareBeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberRegistrationBeanPostProcessor.class);

    private final SpelExpressionParser expressionParser = new SpelExpressionParser();

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        this.process(bean, EventBus::unregister, "destruction");
    }

    @Override
    public boolean requiresDestruction(Object bean) {
        return true;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        this.process(bean, EventBus::register, "initialization");
        return bean;
    }

    private void process(final Object bean, final BiConsumer<EventBus, Object> consumer, final String action) {
        Object proxy = this.getTargetObject(bean);
        final Subscriber annotation = AnnotationUtils.getAnnotation(proxy.getClass(), Subscriber.class);
        if (annotation == null)
            return;

        LOGGER.info("{}: processing bean of type {} during {}", this.getClass().getSimpleName(), proxy.getClass().getName(), action);
        final String annotationValue = annotation.value();
        try {
            final Expression expression = this.expressionParser.parseExpression(annotationValue);
            final Object value = expression.getValue();
            if (!(value instanceof EventBus)) {
                LOGGER.error("{}: expression {} did not evaluate to an instance of EventBus for bean of type {}",
                        this.getClass().getSimpleName(), annotationValue, proxy.getClass().getSimpleName());
                return;
            }

            final EventBus eventBus = (EventBus) value;
            consumer.accept(eventBus, proxy);
        } catch (ExpressionException ex) {
            LOGGER.error("{}: unable to parse/evaluate expression {} for bean of type {}", this.getClass().getSimpleName(),
                    annotationValue, proxy.getClass().getName());
        }
    }

    private Object getTargetObject(Object proxy) throws BeansException {
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            try {
                return ((Advised) proxy).getTargetSource().getTarget();
            } catch (Exception ex) {
                throw new FatalBeanException("Error getting target of JDK proxy", ex);
            }
        }
        return proxy;
    }
}
