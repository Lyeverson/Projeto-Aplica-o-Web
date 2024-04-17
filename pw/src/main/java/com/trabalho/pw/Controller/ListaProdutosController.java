package com.trabalho.pw.Controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;


import com.trabalho.pw.classes_down.ProdutoDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trabalho.pw.classes.Carrinho;
import com.trabalho.pw.classes.Produto;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;




@Controller
public class ListaProdutosController {
    
@RequestMapping(value = "/cliente/listarProdutos", method=RequestMethod.GET) //filter
public void clienteProdutos(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
writer.println("<th>Ação</th>");
writer.println("</tr>");
writer.println("</thead>");
writer.println("<tbody>");
String estoque;
for(Produto p : produtos){
    writer.println("<tr>");
    writer.println("<td>"+ p.getNome() +"</td>");
    writer.println("<td>"+p.getDescricao()+"</td>");
    writer.println("<td>"+ p.getPreco() +"</td>");
    writer.println("<td>"+ p.getEstoque() +"</td>");
    if(p.getEstoque() > 0){
        String link = "http://localhost:8080/cliente/listarProdutos/CarrinhoServlet?id=" + p.getId() + "&comando=add";
        estoque = "<a href=\"" + "/cliente/adicionar?id=" + p.getId() + "&comando=add" + "\" title=\"" + link + "\">adicionar</a>";
    } else {
        estoque = "Sem estoque";
    }
    writer.println("<td>" + estoque + "</td>");
    writer.println("</tr>");
}
writer.println("</tbody>");
writer.println("<tfoot>");
writer.println("<tr>");
writer.println("<td colspan=\"5\"><a href=\"/cliente/carrinho\">ver carrinho</a></td>");
writer.println("</tr>");
writer.println("<tr>");
writer.println("<td colspan=\"5\"><a href=\"/cliente/deslogar\">deslogar</a></td>");
writer.println("</tr>");
writer.println("</tfoot>");
writer.println("</table>");
writer.println("</body> </html>");

}


@RequestMapping(value = "/cliente/carrinho", method=RequestMethod.GET) //filter
public void carrinho(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Carrinho carrinho = new Carrinho();
       Cookie[] cookie = request.getCookies();
      
       HttpSession session = request.getSession(false);
       String nomeSession = (String) session.getAttribute("pessoa");
      
      boolean mostrar = true;

      var writer = response.getWriter();

       for(int i=0;i<cookie.length;i++){
        
         if(cookie[i].getName().equals(nomeSession)){
          mostrar = false;
          String carrinhoJson = URLDecoder.decode(cookie[i].getValue(), "UTF-8");
          ObjectMapper objectMapper = new ObjectMapper();
          carrinho = objectMapper.readValue(carrinhoJson, Carrinho.class);

          

        writer.println("<html> <style> table, th, td {border:1px solid black;}</style> <head> <title> carrinho de cliente </title> </head> <body>");
        writer.println("<table>");
writer.println("<thead>");
writer.println("<tr>");
writer.println("<th>Nome</th>");
writer.println("<th>Descrição</th>");
writer.println("<th>Preço</th>");
writer.println("<th>Estoque</th>");
writer.println("<th>Ação</th>");
writer.println("</tr>");
writer.println("</thead>");
writer.println("<tbody>");
        String estoque;
        for(Produto p : carrinho.getProdutos()){
             String link = "http://localhost:8080/cliente/listarProdutos/CarrinhoServlet?id=" + p.getId() + "&comando=remover";
             estoque = "<a href=\"" + "/cliente/adicionar?id=" + p.getId() + "&comando=remover" + "\" title=\"" + link + "\">remover</a>";
            writer.println("<tr>");
            writer.println("<td>"+ p.getNome() +"</td>");
            writer.println("<td>"+p.getDescricao()+"</td>");
            writer.println("<td>"+ p.getPreco() +"</td>");
            writer.println("<td>"+ p.getEstoque() +"</td>");
            writer.println("<td>" + estoque + "</td>");
            writer.println("</tr>");
        }
        writer.println("</table>");
        ObjectMapper objectMMapper = new ObjectMapper();
        String carrinhoJjson = objectMMapper.writeValueAsString(carrinho);
        String carrinhoEncoded = URLEncoder.encode(carrinhoJjson, "UTF-8");
       writer.println("<a href=\"/cliente/adicionar?carrinho=" + carrinhoEncoded + "&comando=pagar" + "\">pagar</a>");
         }
       }
       writer.println("<a href=\"" + "/cliente/listarProdutos" + "\">ver produtos</a>");
      
       writer.println("</body> </html>");
       if(mostrar){
        response.sendRedirect("/cliente/listarProdutos");
       }   
}





@RequestMapping(value = "/cliente/deslogar", method=RequestMethod.GET)
public void deslogar(HttpServletRequest request, HttpServletResponse response) throws IOException {
  
  HttpSession session = request.getSession(false); 
          if (session != null) {
           session.invalidate();
          }
  
  response.sendRedirect("/login.html");
}

@RequestMapping(value = "/cliente/adicionar", method=RequestMethod.GET)
public void ControllerDecarrinhos(HttpServletRequest request, HttpServletResponse response) throws IOException {
  String command = request.getParameter("comando");


if (command.equals("add")) {
  int id = Integer.parseInt(request.getParameter("id"));
  ProdutoDAO produtoDAO = new ProdutoDAO();
  List<Produto> produtos = produtoDAO.ListarProdutos();

  Carrinho carrinho = null;
  Produto produto = null;
  Cookie[] cookies = request.getCookies();
  int estoque = 0;
  for (Produto p : produtos) {
      if (p.getId() == id) {
          produto = p;
          estoque = p.getEstoque();
          p.setEstoque(1);
          break; // interrompe o loop depois de encontrar o produto
      }
  }

  

  ArrayList<Produto> produtoList = new ArrayList<>();
  produtoList.add(produto);

  HttpSession session = request.getSession(false);
  String nomeSession = (String) session.getAttribute("pessoa");

  boolean carrinhoExistente = false;
  for (Cookie cookie : cookies) {
      if (cookie.getName().equals(nomeSession)) {
          carrinhoExistente = true;
          String carrinhoJson = URLDecoder.decode(cookie.getValue(), "UTF-8");
          ObjectMapper objectMapper = new ObjectMapper();
          carrinho = objectMapper.readValue(carrinhoJson, Carrinho.class);

          boolean produtoAtualizado = false;
          for (Produto p : carrinho.getProdutos()) {
              if (produto.getId() == p.getId()) {
                  if (p.getEstoque() < estoque) {
                      p.incrementaEstoque();
                      produtoAtualizado = true;
                      break; // interrompe o loop depois de atualizar o estoque do produto
                  }
              }
          }

          if (!produtoAtualizado) {
              carrinho.addProduto(produto);
          }

          // Atualiza o cookie do carrinho excluindo o antigo e adicionando o novo
          Cookie novoCookie = new Cookie(nomeSession, null);
          novoCookie.setMaxAge(0);
          novoCookie.setPath("/");
          response.addCookie(novoCookie);

          String novoCarrinhoJson = objectMapper.writeValueAsString(carrinho);
          String novoCarrinhoCookieValue = URLEncoder.encode(novoCarrinhoJson, "UTF-8");
          Cookie novoCookieCarrinho = new Cookie(nomeSession, novoCarrinhoCookieValue);
          novoCookieCarrinho.setMaxAge(48 * 60 * 60); // 48 horas
          novoCookieCarrinho.setPath("/");
          response.addCookie(novoCookieCarrinho);

          break; // interrompe o loop depois de atualizar o carrinho existente
      }
  }

  if (!carrinhoExistente) {
      carrinho = new Carrinho(produtoList);

      for (Produto p : carrinho.getProdutos()) {
          p.setEstoque(1);
      }

      ObjectMapper objectMapper = new ObjectMapper();
      String json = objectMapper.writeValueAsString(carrinho);
      String encodedJson = URLEncoder.encode(json, "UTF-8");

      Cookie novoCookie = new Cookie(nomeSession, encodedJson);
      novoCookie.setPath("/");
      novoCookie.setMaxAge(48 * 60 * 60); // 48 horas
      response.addCookie(novoCookie);
  }

  response.sendRedirect("/cliente/listarProdutos");

  //adicionar ao carrinho
  //remover do estoque
  }else if (command.equals("remover")){

    int id = Integer.parseInt(request.getParameter("id"));

    Carrinho carrinho = null;
    Cookie[] cookie = request.getCookies();
   
    HttpSession session = request.getSession(false);
    String nomeSession = (String) session.getAttribute("pessoa");
   
  // boolean mostrar = true;
    for(int i=0;i<cookie.length;i++){
      if(cookie[i].getName().equals(nomeSession)){
        String carrinhoJson = URLDecoder.decode(cookie[i].getValue(), "UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        carrinho = objectMapper.readValue(carrinhoJson, Carrinho.class);
        carrinho.removeProduto(id);
        Cookie carrinhoNovo = cookie[i];
        String novoCarrinhoJson = objectMapper.writeValueAsString(carrinho);
        String novoCarrinhoCookieValue = URLEncoder.encode(novoCarrinhoJson, "UTF-8");

        carrinhoNovo.setMaxAge(0);
       carrinhoNovo.setPath("/");

      cookie[i].setValue(novoCarrinhoCookieValue);
     cookie[i].setMaxAge(48 * 60 * 60); //48 horas
      response.addCookie(carrinhoNovo);
      response.addCookie(cookie[i]);
      
      }
    }
    response.sendRedirect("/cliente/carrinho");
  //remover do carrinho
  //adicionar ao estoque
  } else if (command.equals("pagar")){

    HttpSession session = request.getSession(false);
    String nomeSession = (String) session.getAttribute("pessoa");

    Cookie[] cookie = request.getCookies();
    
    for(int i=0;i<cookie.length;i++){
      if(cookie[i].getName().equals(nomeSession)){
        cookie[i].setMaxAge(0);
        cookie[i].setPath("/");
        response.addCookie(cookie[i]);
      }
  }

  String carrinhoEncoded = request.getParameter("carrinho");
    String carrinhoJson = URLDecoder.decode(carrinhoEncoded, "UTF-8");
    ObjectMapper objectMapper = new ObjectMapper();
    Carrinho carrinho = objectMapper.readValue(carrinhoJson, Carrinho.class);
  
    ProdutoDAO produtoDAO = new ProdutoDAO();

    int SomaPreco = 0;
    int SomaEstoque = 0;
    int Total = 0;
    for(int i=0;i<carrinho.getProdutos().size();i++){
      produtoDAO.diminuirEstoque(carrinho.getProdutos().get(i).getId(), carrinho.getProdutos().get(i).getEstoque());
      SomaPreco+= carrinho.getProdutos().get(i).getPreco();
      SomaEstoque+= carrinho.getProdutos().get(i).getEstoque();
    }

    

    Total = SomaPreco * SomaEstoque;

    var writer = response.getWriter();

    writer.println("<html> <head> <title> carrinho de cliente </title> </head> <body> <table>");
    writer.println("<p>total pago:" + Total +"</p>");
    writer.println("<a href=\"" + "/cliente/listarProdutos" + "\">ver produtos</a>");
    writer.println("</body> </html>");
}

}

}
