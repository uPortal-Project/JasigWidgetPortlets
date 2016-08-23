/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.portlet.widget.service.spel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.portlet.PortletRequest;

import org.jasig.portlet.widget.service.DefaultPropertyAccessor;
import org.jasig.portlet.widget.service.IExpressionProcessor;
import org.jasig.portlet.widget.service.PropertyResolverAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.env.PropertyResolver;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

/**
 * Processor that uses spring EL for the implementation.
 *
 * @author Josh Helmer, jhelmer@unicon.net
 */
@Service
public class SpELExpressionProcessor implements IExpressionProcessor {

    private static ParserContext PARSER_CONTEXT = new TemplateParserContext("${", "}");

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext applicationContext;

    private PropertyResolver propertyResolver;

    private BeanResolver beanResolver;

    private final Set<IContextElementsProvider> contextElementsProviders = new HashSet<>();

    @Autowired
    public void setPropertyResolver(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }

    @PostConstruct
    public void init() {
        beanResolver = new BeanFactoryResolver(applicationContext);
        contextElementsProviders.addAll(applicationContext.getBeansOfType(IContextElementsProvider.class).values());
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String process(String value, PortletRequest request) {

        // Set up the context
        final Map<String,Object> context = new HashMap<>();
        for (IContextElementsProvider cep : contextElementsProviders) {
            context.putAll(cep.provideElements(request));
        }

        StandardEvaluationContext sec = new StandardEvaluationContext(context);

        sec.setVariable("systemProperties", propertyResolver);

        sec.addPropertyAccessor(new PropertyResolverAccessor());
        sec.addPropertyAccessor(new MapAccessor());
        sec.addPropertyAccessor(new ReflectivePropertyAccessor());
        sec.addPropertyAccessor(new DefaultPropertyAccessor(
                PARSER_CONTEXT.getExpressionPrefix(),
                PARSER_CONTEXT.getExpressionSuffix()));
        sec.setBeanResolver(beanResolver);
        SpelExpressionParser parser = new SpelExpressionParser();

        String processed = parser
                .parseExpression(value, PARSER_CONTEXT)
                .getValue(sec, String.class);

        return processed;
    }

}
