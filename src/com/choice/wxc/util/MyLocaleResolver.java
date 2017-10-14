package com.choice.wxc.util;

import org.springframework.web.servlet.i18n.SessionLocaleResolver;

public class MyLocaleResolver extends SessionLocaleResolver{
	private String myLocal=SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME;
	
	public String getMyLocal() {
		return myLocal;
	}
	public void setMyLocal(String myLocal) {
		this.myLocal = myLocal;
	}

}
