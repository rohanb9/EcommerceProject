import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/AuthenticationFilterMyAccount")
public class AuthenticationFilterMyAccount implements Filter {

	private ServletContext context;
	
	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
		this.context.log("Initializion of AuthenticationFilter ");
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		String uri = req.getRequestURI();
		this.context.log("Requested Resource::"+uri);
		
		HttpSession session = req.getSession(false);
		
		HttpSession reqsession = req.getSession();
		boolean authenicated = false;
		if(session != null){

			String uname = (String)session.getAttribute("username");
				//res.sendRedirect("login");
			//response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/home"));
			if(uname != null) {
				authenicated = true;
			}		

		} 
		if(!authenicated) {
			res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/home?ACCESS_DENIED"));		
		}
		
		chain.doFilter(request, response);
		
	}

	public void destroy() {
		//destroy any resources here
	}

}