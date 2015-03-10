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
package org.jasig.portlet.widget.mvc.app;

import static org.junit.Assert.*;
import org.junit.Test;

public class AppDefinitionTest {

    @Test
    public void testAppUrlSettingValidate() {
        String[] validUrls = new String[] {
                "https://www.example.com/path",
                "http://www.example.com/path",
                "http://example.com/path",
                "//example.com/path",
                "/path",
                "${elToken}/path"
        };
        for (String url : validUrls) {
            assertTrue("The following URL should validate, but didn't:  " + url, AppDefinition.Setting.APP_URL.validate(url));
        }
        String[] invalidUrls = new String[] {
                "garbage://protocol.com",
                "http:missing.slash.com",
                "${unterninated_el_expression"
        };
        for (String url : invalidUrls) {
            assertFalse("The following URL SHOULD NOT validate, but did:  " + url, AppDefinition.Setting.APP_URL.validate(url));
        }
    }

}
