package com.trabalho.pw.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.trabalho.pw.classes.Produto;
import com.trabalho.pw.classes_down.ProdutoDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LojistaController {
    

    @RequestMapping(value ="/lojista/listarProdutos", method=RequestMethod.GET) //filter
    public void lojistaListaProduto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ProdutoDAO produtoDao = new ProdutoDAO();
        List<Produto> produtos = produtoDao.ListarProdutos();

        var writer = response.getWriter();

       
writer.println("<html> <style> table, th, td {border:1px solid black;}</style> <head> <title> produtos clientes </title> </head> <body>");
writer.println("<table>");
writer.println("<thead>");
writer.println("<tr>");
writer.println("<th>Nome</th>");
writer.println("<th>Descrição</th>");
writer.println("<th>Preço</th>");
writer.println("<th>Estoque</th>");
writer.println("</tr>");
writer.println("</thead>");
writer.println("<tbody>");

        for(Produto p : produtos){
            writer.println("<tr>");
            writer.println("<td>"+ p.getNome() +"</td>");
            writer.println("<td>"+p.getDescricao()+"</td>");
            writer.println("<td>"+ p.getPreco() +"</td>");
            writer.println("<td>"+ p.getEstoque() +"</td>");
            writer.println("</tr>");
        }
        writer.println("</table>");
        writer.println("<a href=\"" + "/produtoCadastrar.html" + "\">adicionar produto</a>");
        writer.println("<a href=\"" + "/produtoExcluir.html" + "\">excluir</a>");
        writer.println("<a href=\"" + "/lojista/deslogar" + "\">deslogar</a>");
        
        writer.println("</body> </html>");
    }

    @RequestMapping(value ="/lojista/excluir", method=RequestMethod.POST) //filter
public void excluir(HttpServletRequest request, HttpServletResponse response) throws IOException {
       String nome = request.getParameter("nome");

       ProdutoDAO produtoDAO = new ProdutoDAO();
       produtoDAO.removerProduto(nome);
       response.sendRedirect("/lojista/listarProdutos");

   }

    @RequestMapping(value ="/lojista/deslogar", method=RequestMethod.GET) //filter
public void deslogar(HttpServletRequest request, HttpServletResponse response) throws IOException {
     
    HttpSession session = request.getSession(false); 
          if (session != null) {
           session.invalidate();
          }
  
  response.sendRedirect("/login.html");

   } 

@RequestMapping(value ="/lojista/adicionar", method=RequestMethod.POST) //filter
public void adicionar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nome = request.getParameter("nome");
        int preco = Integer.parseInt(request.getParameter("preco"));
        String descricao = request.getParameter("descricao");
        int estoque =  Integer.parseInt(request.getParameter("estoque"));
        Produto produto = new Produto(preco, nome, descricao, estoque);
        ProdutoDAO produtoDAO = new ProdutoDAO();
        produtoDAO.cadastrar(produto);
        response.sendRedirect("/lojista/listarProdutos");
    }

}