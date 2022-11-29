//package ca.sheridancollege.fangyux.config.oauth;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import ca.sheridancollege.fangyux.Addition.StudentUserDetails;
//import ca.sheridancollege.fangyux.beans.User;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.UrlPathHelper;
//
//@Component
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//										Authentication authentication) throws IOException, ServletException {
//
//		StudentUserDetails studentUserDetails = (StudentUserDetails) authentication.getPrincipal();
//		User user = studentUserDetails.getUser();
//
//		//custom logics
//		UrlPathHelper helper = new UrlPathHelper();
//		String contextPath = helper.getContextPath(request);
//		response.sendRedirect(contextPath);
//
//	}
//
//	}