import java.io.*;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.*;
import javax.servlet.http.*;

public class AutoCompleteServlet extends HttpServlet {

    private ServletContext context;
    AJAXUtitliy ajaxStore = new AJAXUtitliy();

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.context = config.getServletContext();
    }

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String action = request.getParameter("action");
		System.out.println("action");
        String searchId = request.getParameter("id");
        StringBuffer sb ;

        if (searchId != null) {
        	searchId = searchId.trim().toLowerCase();
        } 
        
        boolean namesAdded = false;
        if (action.equals("complete")) {
        	  sb = ajaxStore.readdata(searchId);

            if (sb.length() > 2) {
                response.setContentType("text/xml");
                response.setHeader("Cache-Control", "no-cache");
                response.getWriter().write("<products>" + sb.toString() + "</products>");
            } else {
				System.out.println("No product");
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        }

    }
}
