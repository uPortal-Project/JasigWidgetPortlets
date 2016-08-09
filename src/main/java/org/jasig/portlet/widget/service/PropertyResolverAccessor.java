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
package org.jasig.portlet.widget.service;

import org.springframework.core.env.PropertyResolver;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

/**
 * Created by andrew on 7/25/16.
 */
public class PropertyResolverAccessor implements PropertyAccessor {
    @Override
    public Class[] getSpecificTargetClasses() {
        return new Class[] { PropertyResolver.class };
    }

    @Override
    public boolean canRead(EvaluationContext evaluationContext, Object o, String s) throws AccessException {
        return o != null;
    }

    @Override
    public TypedValue read(EvaluationContext evaluationContext, Object o, String s) throws AccessException {
        PropertyResolver pr = (PropertyResolver) o;
        return new TypedValue(pr.getProperty(s)); // Returns TypedValue(null), allowing defaults to be processed
    }

    @Override
    public boolean canWrite(EvaluationContext evaluationContext, Object o, String s) throws AccessException {
        return false;
    }

    public void write(EvaluationContext evaluationContext, Object o, String s, Object o1) throws AccessException {
        //Unreachable
    }
}
