/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.portlet.widget.mvc.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.portlet.ActionRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;

public class AppDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final AppDefinition BLANK_INSTANCE = new AppDefinition(new HashMap<Setting,String>());

    public enum DisplayStrategies {

        IFRAME("iframe"),
        NEW_WINDOW("newWindow");

        private final String code;

        DisplayStrategies(String name) {
            this.code = name;
        }

        public String getCode() {
            return code;
        }

    }

    public enum Setting {

        APP_URL("appUrl", "http://www.apereo.org") {
            @Override
            public boolean validate(String value) {
                return value != null && VALID_URL_PATTERN.matcher(value).matches();
            }
        },
        DISPLAY_STRATEGY("displayStrategy", DisplayStrategies.IFRAME.getCode()) {
            @Override
            public boolean validate(String value) {
                boolean rslt = false;  // default
                for (DisplayStrategies s : DisplayStrategies.values()) {
                    if (s.getCode().equals(value)) {
                        rslt = true;
                    }
                }
                return rslt;
            }
        },
        ICON_URL("iconUrl", null) {
            @Override
            public boolean validate(String value) {
                return value != null 
                        ? VALID_URL_PATTERN.matcher(value).matches()
                        : true;
            }
        },
        LINK_TITLE("linkTitle", "Launch in a new window") {
            @Override
            public boolean validate(String value) {
                return true;  // Anything is allowed
            }
        },
        TITLE("title", "Apereo Foundation") {
            @Override
            public boolean validate(String value) {
                return StringUtils.isNotBlank(value);
            }
        },
        SUBTITLE("subtitle", "Serving the academic mission") {
            @Override
            public boolean validate(String value) {
                return true;  // Not required
            }
        };

        private static final String PREFERENCE_PREFIX = "AppDefinition.";
        private static final String URL_REGEX = "^(https?:)?/.*";
        private static final Pattern VALID_URL_PATTERN = Pattern.compile(URL_REGEX);

        private final String fieldName;
        private final String defaultValue;

        Setting(String fieldName, String defaultValue) {
            this.fieldName = fieldName;
            this.defaultValue = defaultValue;
        }

        public String getCode() {
            return PREFERENCE_PREFIX + fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public abstract boolean validate(String value);

    }

    private final Map<Setting,String> settings;

    public static AppDefinition fromPortletPreferences(PortletRequest req) {
        PortletPreferences prefs = req.getPreferences();
        final Map<Setting,String> settings = new HashMap<Setting,String>();
        for (Setting p : Setting.values()) {
            String value = prefs.getValue(p.getCode(), p.getDefaultValue());
            settings.put(p, value);
        }
        return new AppDefinition(settings);
    }

    public static AppDefinition fromFormFields(ActionRequest req) {
        final Map<Setting,String> settings = new HashMap<Setting,String>();
        for (Setting p : Setting.values()) {
            String value = null;  // default
            final String fieldName = p.getFieldName();
            final String fieldValue = req.getParameter(fieldName);
            if (StringUtils.isNotBlank(fieldValue)) {
                value = fieldValue.trim();
            }
            settings.put(p, value);
        }
        return new AppDefinition(settings);
    }

    public static void update(ActionRequest req, AppDefinition appDefinition) {

        // Re-validate to make certain these settings are allowed...
        final List<Setting> invalid = appDefinition.getInvalidSettings();
        if (invalid.size() != 0) {
            throw new RuntimeException("Unable to update;  invalid settings:  " + invalid);
        }

        try {
            PortletPreferences prefs = req.getPreferences();
            for (Setting p : Setting.values()) {
                final String value = appDefinition.settings.get(p);
                prefs.setValue(p.getCode(), value);
            }
            prefs.store();
        } catch (Exception e) {
            throw new RuntimeException("Unable to save settings:  " + appDefinition.settings);
        }

    }

    public String getAppUrl() {
        return settings.get(Setting.APP_URL);
    }

    public String getDisplayStrategy() {
        return settings.get(Setting.DISPLAY_STRATEGY);
    }

    public String getIconUrl() {
        return settings.get(Setting.ICON_URL);
    }

    public String getLinkTarget() {
        final String displayStrategy = settings.get(Setting.DISPLAY_STRATEGY);
        if (DisplayStrategies.NEW_WINDOW.getCode().equalsIgnoreCase(displayStrategy)) {
            return "_blank";
        }
        return null;  // default
    }

    public String getLinkTitle() {
        return settings.get(Setting.LINK_TITLE);
    }

    public String getTitle() {
        return settings.get(Setting.TITLE);
    }

    public String getSubtitle() {
        return settings.get(Setting.SUBTITLE);
    }

    /**
     * When true, indicates the definition contains only the default info.
     */
    public boolean isAllDefaults() {
        boolean rslt = true;  // default
        for (Setting p : Setting.values()) {
            if (!StringUtils.equals(p.getDefaultValue(), settings.get(p))) {
                rslt = false;
                break;
            }
        }
        return rslt;
    }

    public List<Setting> getInvalidSettings() {
        List<Setting> rslt = new ArrayList<Setting>();
        for (Setting p : Setting.values()) {
            final String value = settings.get(p);
            if (!p.validate(value)) {
                rslt.add(p);
            }
        }
        return rslt;
    }

    /*
     * Implementation
     */

    @Override
    public String toString() {
        return "AppDefinition [settings=" + settings + "]";
    }

    private AppDefinition(Map<Setting,String> settings) {
        this.settings = settings;
    }

}
