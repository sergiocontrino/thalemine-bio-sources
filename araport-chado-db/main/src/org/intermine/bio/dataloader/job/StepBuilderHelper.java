/*
 * Copyright 2006-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.intermine.bio.dataloader.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 *
 */
public abstract class StepBuilderHelper<B extends StepBuilderHelper<B>> {

    protected final Log logger = LogFactory.getLog(getClass());

    protected final CommonStepProperties properties;

    public StepBuilderHelper(String name) {
        this.properties = new CommonStepProperties();
        properties.name = name;
    }

    /**
     * Create a new builder initialized with any properties in the parent. The parent is copied, so it can be re-used.
     *
     * @param parent a parent helper containing common step properties
     */
    protected StepBuilderHelper(StepBuilderHelper<?> parent) {
        this.properties = new CommonStepProperties(parent.properties);
    }


    public B startLimit(int startLimit) {
        properties.startLimit = startLimit;
        @SuppressWarnings("unchecked")
        B result = (B) this;
        return result;
    }

    /**
     * Registers objects using the annotation based listener configuration.
     *
     * @param listener the object that has a method configured with listener annotation
     * @return this for fluent chaining
     */
    public B listener(Object listener) {
        Set<Method> stepExecutionListenerMethods = new HashSet<Method>();


        @SuppressWarnings("unchecked")
        B result = (B) this;
        return result;
    }


    public B allowStartIfComplete(boolean allowStartIfComplete) {
        properties.allowStartIfComplete = allowStartIfComplete;
        @SuppressWarnings("unchecked")
        B result = (B) this;
        return result;
    }

    protected String getName() {
        return properties.name;
    }


    protected boolean isAllowStartIfComplete() {
        return properties.allowStartIfComplete != null ? properties.allowStartIfComplete : false;
    }


    public static class CommonStepProperties {

        private int startLimit = Integer.MAX_VALUE;

        private Boolean allowStartIfComplete;

        public CommonStepProperties() {
        }

        public CommonStepProperties(CommonStepProperties properties) {
            this.name = properties.name;
            this.startLimit = properties.startLimit;
            this.allowStartIfComplete = properties.allowStartIfComplete;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getStartLimit() {
            return startLimit;
        }

        public void setStartLimit(Integer startLimit) {
            this.startLimit = startLimit;
        }

        public Boolean getAllowStartIfComplete() {
            return allowStartIfComplete;
        }

        public void setAllowStartIfComplete(Boolean allowStartIfComplete) {
            this.allowStartIfComplete = allowStartIfComplete;
        }

        private String name;

    }

}
