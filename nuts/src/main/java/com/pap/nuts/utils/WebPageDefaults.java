package com.pap.nuts.utils;

public enum WebPageDefaults {
	
	LOGIN {
		@Override
		public String getPage(String jsInit, String context) {
			return "<!DOCTYPE html>\n"
					+ "<html>\n"
					+ "  <head>\n"
					+ "    <meta charset=\"UTF-8\">\n"
					+ String.format("	   <link rel=\"shortcut icon\" type=\"image/png\" href=\"%s/img/nuts.png\">\n", context)
					+ "    <title>Nuts project</title>\n"
					+ "  </head>\n"
					+ "<body>\n"
					+ "		<p>Hello Rest <b>API Login<b></p>\n"
					+ "</body>\n"
					+ "<script type=\"text/javascript\">\n"
					+ jsInit
					+ "</script>\n"
					+ "</html>\n";
		}
	};
	
	public abstract String getPage(String jsInit, String context);
}
