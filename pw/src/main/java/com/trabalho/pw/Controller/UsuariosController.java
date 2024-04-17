package com.trabalho.pw.Controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trabalho.pw.classes_down.UsuarioDAO;
import com.trabalho.pw.classes.Usuario;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;




@Controller
public class UsuariosController {
    

@RequestMapping(value = "/cadastrar", method=RequestMethod.POST)
public void cadastro(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String nome =  request.getParameter("nome");
    String email = request.getParameter("email");
    String senha = request.getParameter("senha");
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    boolean verificador = true;
    List<Usuario> usuarios = usuarioDAO.listarUsuarios();
   Usuario usuario = new Usuario(nome, senha, email);
    for(int i=0;i<usuarios.size();i++){
        if(usuario.getEmail().equals(usuarios.get(i).getEmail())){
             verificador = false;
        }
    }

    if(verificador == true){
        int indexUsuario = 0;
         usuarioDAO.cadastrar(usuario);
         usuarios = usuarioDAO.listarUsuarios();
         for(int i=0;i<usuarios.size();i++){
            if(usuario.getEmail().equals(usuarios.get(i).getEmail())){
                indexUsuario = i;
           }
         }
         HttpSession session = request.getSession(false); 
          if (session != null) {
           session.invalidate();
          }
          String id = String.valueOf(usuarios.get(indexUsuario).getId());
        HttpSession sessions = request.getSession(true);
        sessions.setAttribute("pessoa", id);
        response.sendRedirect("/cliente/listarProdutos"); //filter
            
    }else{
        response.sendRedirect("/cadastrar.html");
    }

}


@RequestMapping(value = "/login", method=RequestMethod.POST)
public void logar(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String email = request.getParameter("email");
    String senha = request.getParameter("senha");
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    boolean verificador = false;
    List<Usuario> usuarios = usuarioDAO.listarUsuarios();
   int index = 0;
    for(int i=0;i<usuarios.size();i++){
        if(email.equals(usuarios.get(i).getEmail()) && senha.equals(usuarios.get(i).getSenha())){
             verificador = true;
             index = i;

        }
    }
    if(verificador == true){
        if(usuarios.get(index).getEmail().equals("tanirocr@gmail.com") || usuarios.get(index).getEmail().equals("lore_sil@yahoo.com.br")){
            HttpSession session = request.getSession(false); 
            if (session != null) {
             session.invalidate();
            }
          HttpSession sessions = request.getSession(true);
           String id = String.valueOf(usuarios.get(index).getId());
          sessions.setAttribute("lojista", id);
          response.sendRedirect("/lojista/listarProdutos"); // filter
        }else {
            HttpSession session = request.getSession(false); 
            if (session != null) {
             session.invalidate();
            }
          HttpSession sessions = request.getSession(true);
           String id = String.valueOf(usuarios.get(index).getId());
          sessions.setAttribute("pessoa", id);
          response.sendRedirect("/cliente/listarProdutos");  //filter 
        }
    }else{
        response.sendRedirect("/login.html");
    }
}



}
