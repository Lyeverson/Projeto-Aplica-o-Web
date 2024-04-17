package com.trabalho.pw.Controller;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter({"/lojista/excluir", "/lojista/listarProdutos","/produtoCadastrar.html","/produtoExcluir.html", "/lojista/deslogar", "/lojista/adicionar"})
public class LojistaFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }


 
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = ((HttpServletResponse) servletResponse);
        HttpServletRequest request = ((HttpServletRequest) servletRequest);

        HttpSession session = request.getSession(false);

        if (session == null){
            response.sendRedirect("/login.html?msg=Você precisa logar com uma conta de lojista");
        }else{
       String nomeSession = (String) session.getAttribute("lojista");
            if (nomeSession == null){
                response.sendRedirect("/login.html?msg=Você precisa logar com uma conta de lojista");
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
