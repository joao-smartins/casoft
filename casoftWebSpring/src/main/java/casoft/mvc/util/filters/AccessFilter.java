package casoft.mvc.util.filters;

import casoft.mvc.util.JWTTokenProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;

public class AccessFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader("Authorization");
        if(token!=null && JWTTokenProvider.verifyToken(token)) {
            String nivel= JWTTokenProvider.getAllClaimsFromToken(token).get("nivel").toString();
            String rotaDestino=(((HttpServletRequest) request).getRequestURI());
            if((rotaDestino.contains("adm") && nivel.equals("ADMIN")))
                chain.doFilter(request, response);
        }
        else {
            ((HttpServletResponse) response).setStatus(500);
            response.getOutputStream().write("Não autorizado ".getBytes());
        }
    }
}

