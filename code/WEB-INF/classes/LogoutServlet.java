import java.io.IOException;  

import java.io.PrintWriter;  
  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.http.HttpSession;  

public class LogoutServlet extends HttpServlet {  
        protected void doGet(HttpServletRequest request, HttpServletResponse response)  
                                throws ServletException, IOException {  
            response.setContentType("text/html");  
            PrintWriter out=response.getWriter();   
              
            HttpSession session=request.getSession();  
            session.invalidate();  

         
            out.print("<b>You are successfully logged out!<b>");  
            
            out.print("<br><br><u><a href=\"/csj/home\">Go To Home Page</a></u>");   
              
            out.close(); 
            
    }  
}  