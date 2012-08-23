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

package org.jasig.portlet.widget.service.rome;

import org.jasig.portlet.widget.service.BasicAlert;

public class TimestampAlert extends BasicAlert implements Comparable<TimestampAlert> {
    
    // Instance Members.
    private final long timestamp;

    public TimestampAlert(String header, String body, String url, long timestamp) {
        super(header, body, url);
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(TimestampAlert a) {
        final long diff = this.timestamp - a.timestamp;
        if (diff > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else if (diff < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        } else {
            return (int) diff;
        }
    }

}
