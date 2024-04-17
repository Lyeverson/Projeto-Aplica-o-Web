package com.trabalho.pw.classes_down;

import com.trabalho.pw.classes.Usuario;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void cadastrar(Usuario usuario){
        Connection connection = null;
        PreparedStatement stmt = null;

        try{
         connection = Conexao.getConnection();
         
         stmt = connection.prepareStatement("insert into Usuario (nome, senha, email) values(?,?,?)");
         stmt.setString(1, usuario.getNome());
         stmt.setString(2, usuario.getSenha());
         stmt.setString(3, usuario.getEmail());

         stmt.executeUpdate();
         connection.close();
        }catch (SQLException | URISyntaxException ex) {
            System.out.println("Connection Failed! Check output console" + ex.getMessage());
        }
    }

    public List<Usuario> listarUsuarios(){
        Connection connection = null;
        PreparedStatement stmt = null;

        ResultSet rs = null;
        List<Usuario> listaUsuarios = new ArrayList<>();
    
        try{
            connection = Conexao.getConnection();
            stmt = connection.prepareStatement("select * from Usuario");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario(rs.getString("nome"),  rs.getString("senha"), rs.getString("email"));
                usuario.setId(rs.getInt("id"));
                listaUsuarios.add(usuario);
            }
            connection.close();
        }catch (SQLException | URISyntaxException ex) {
            System.out.println("Connection Failed! Check output console" + ex.getMessage());
        }
        return listaUsuarios;

    }
    
}
