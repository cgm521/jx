package com.choice.payment.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestInterface extends HttpServlet {



	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		参数  是否必须  说明  
		appid  是  公众号的唯一标识  
		redirect_uri  是  授权后重定向的回调链接地址，请使用urlencode对链接进行处理  
		response_type  是  返回类型，请填写code  
		scope  是  应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）  
		state  否  重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值  
		#wechat_redirect  是  无论直接打开还是做页面302重定向时候，必须带此参数 
	*/
			response.setContentType("textml");
			PrintWriter out = response.getWriter();
			//授权
			String baseurl = "https://open.weixin.qq.com/connect/oauth2/authorize?";
			String appid = "wxcce44c9df1c584fd";
			String relurl = "http://m.96360.com:8082/mobileSkyecho1";
			//https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
			String finalurl=baseurl+"appid="+appid+"&redirect_uri="+relurl+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
			System.out.println(finalurl);
			request.getRequestDispatcher("finalurl").forward(request, response);
			
			//通过code换取网页授权access_token
	/*		参数  是否必须  说明  
			appid  是  公众号的唯一标识  
			secret  是  公众号的appsecret  
			code  是  填写第一步获取的code参数  
			grant_type  是  填写为authorization_code  */

			String appid2 = "wxcce44c9df1c584fd";
			String secret2= "dad2ef536c4497f9adbda499493eb663";
			String  code2 = "";
			
			//https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
			String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid2+"&secret="+secret2+"&code="+code2+"&grant_type=authorization_code";
			System.out.println(url);
			
			out.flush();
			out.close();

		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
	
	public static void main(String[] args){
		/*
		参数  是否必须  说明  
		appid  是  公众号的唯一标识  
		redirect_uri  是  授权后重定向的回调链接地址，请使用urlencode对链接进行处理  
		response_type  是  返回类型，请填写code  
		scope  是  应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）  
		state  否  重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值  
		#wechat_redirect  是  无论直接打开还是做页面302重定向时候，必须带此参数 
	*/
		String baseurl = "https://open.weixin.qq.com/connect/oauth2/authorize?";
		String appid = "wxc1437f5d60f3755e";
		String relurl = "http://m.96360.com:8082/mobileSkyecho1/pages/airServices/SJW121/sjwgruit.html";
		//https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
		String finalurl=baseurl+"appid="+appid+"&redirect_uri="+relurl+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
		System.out.println(finalurl);
	}
}
