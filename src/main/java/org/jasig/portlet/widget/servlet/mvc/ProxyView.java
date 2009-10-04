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
package org.jasig.portlet.widget.servlet.mvc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.AbstractView;

/**
 * ProxyView provides a View for proxying HTTP(s) resources.  The view requires
 * that the model provide a target url, and allows it to also specify
 * user credentials.  Future versions might include more configuration options.
 * 
 * @author Jen Bourey
 */
public class ProxyView extends AbstractView {
	
	public static final String URL = "url";
	public static final String CREDENTIALS = "credentials";
	
	private Log log = LogFactory.getLog(ProxyView.class);

	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String url = (String) model.get(URL);
		HttpClient client = new HttpClient();
		GetMethod get = null;

		// if credentials are defined, add them to the client
		Credentials credentials = (Credentials) model.get(CREDENTIALS);
		if(null != credentials) {
			client.getState().setCredentials(AuthScope.ANY, credentials);
		}

		try {

			if(log.isDebugEnabled()) {
				log.debug("Retrieving proxy url " + url);
			}
			get = new GetMethod(url);
			int rc = client.executeMethod(get);
			if(rc == HttpStatus.SC_OK) {
				
				// get the response body
				log.debug("request completed successfully");
				InputStream in = get.getResponseBodyAsStream();
				
				// copy all headers from the proxied page
				Header[] headers = get.getResponseHeaders();
				for (Header header : headers) {
					response.addHeader(header.getName(), header.getValue());
				}
				
				// proxy the data itself
				ServletOutputStream out = response.getOutputStream();
				IOUtils.copyLarge(in, out);
				out.flush();
				out.close();
			}
			else {
				log.warn("HttpStatus for " + url + ":" + rc);
			}
		} catch (HttpException e) {
			log.warn("Error proxying url", e);
		} catch (IOException e) {
			log.warn("Error proxying url", e);
		} finally {
			if (get != null) {
				get.releaseConnection();
			}
		}

		
	}

}
