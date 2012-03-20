/* Copyright 2012 predic8 GmbH, www.predic8.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License. */

package com.predic8.membrane.core.http;

import com.predic8.membrane.core.Constants;
import com.predic8.membrane.core.util.HttpUtil;

public class ErrorResponse extends Response {

	public ErrorResponse(int statusCode, String statusMessage, String msg) {
		setStatusCode(statusCode);
		setStatusMessage(statusMessage);
		setBody(new Body(getErrorPage(msg)));
		setHeader(HttpUtil.createHeaders(MimeType.TEXT_HTML));
	}
	
	private String getErrorPage(String msg) {
		StringBuffer buf = new StringBuffer();
		buf.append("<html>");
		
			buf.append("<head>");
				buf.append("<title>");
					buf.append("Membrane Router: Error Report");
				buf.append("</title>");
			buf.append("</head>");
		
			buf.append("<body>");
			
				buf.append("<h1>");
					buf.append("Error Report");
				buf.append("</h1>");
					
				buf.append("<p>");
					buf.append("Message: ");
					buf.append(msg);
				buf.append("</p>");
						
				buf.append("<p>");
					buf.append("This message was generated by ");
					buf.append("<a href='http://membrane-soa.org/'>Membrane HTTP Router</a>");
					buf.append("Version ");
					buf.append(Constants.VERSION);
				buf.append("</p>");
				
			buf.append("</body>");
		buf.append("</html>");
		return buf.toString();
		
	}
}
