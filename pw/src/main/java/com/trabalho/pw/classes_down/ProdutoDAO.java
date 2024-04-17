package com.trabalho.pw.classes_down;

import com.trabalho.pw.classes.Produto;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void cadastrar(Produto produto){
        Connection connection = null;
        PreparedStatement stmt = null;

        try{
            connection = Conexao.getConnection();
            stmt = connection.prepareStatement("insert into Produto (id, preco, nome, descricao, estoque) values(?,?,?,?,?)");
            stmt.setInt(1, produto.getId());
            stmt.setInt(2, produto.getPreco());
            stmt.setString(3, produto.getNome());
            stmt.setString(4, produto.getDescricao());
            stmt.setInt(5, produto.getEstoque());

            stmt.executeUpdate();
            connection.close();
        }catch (SQLException | URISyntaxException ex) {
            System.out.println("Connection Failed! Check output console" + ex.getMessage());
        }
    }

    public List<Produto> ListarProdutos(){
        Connection connection = null;
        PreparedStatement stmt = null;

        ResultSet rs = null;
        List<Produto> listaProdutos = new ArrayList<>();
    
        try{
            connection = Conexao.getConnection();
            stmt = connection.prepareStatement("select * from Produto");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Produto produto = new Produto(rs.getInt("id"),  rs.getInt("preco"), 
                rs.getString("nome"), rs.getString("descricao"), rs.getInt("estoque"));
                listaProdutos.add(produto);
            }
            connection.close();
        }catch (SQLException | URISyntaxException ex) {
            System.out.println("Connection Failed! Check output console" + ex.getMessage());
        }
        return listaProdutos;
    }


    public void diminuirEstoque(int idProduto, int quantidade) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = Conexao.getConnection();
            // Primeiro, obtemos a quantidade atual do estoque do produto
            PreparedStatement selectStmt = connection.prepareStatement("SELECT estoque FROM Produto WHERE id = ?");
            selectStmt.setInt(1, idProduto);
            ResultSet rs = selectStmt.executeQuery();
            
            int estoqueAtual = 0;
            if (rs.next()) {
                estoqueAtual = rs.getInt("estoque");
            }
            
            // Agora, subtraímos a quantidade desejada do estoque atual
            int novoEstoque = estoqueAtual - quantidade;
            if (novoEstoque < 0) {
                throw new IllegalArgumentException("Quantidade desejada é maior do que o estoque atual");
            }

            // Atualizamos o estoque no banco de dados com o novo valor calculado
            stmt = connection.prepareStatement("UPDATE Produto SET estoque = ? WHERE id = ?");
            stmt.setInt(1, novoEstoque);
            stmt.setInt(2, idProduto);

            stmt.executeUpdate();
            connection.close();
        } catch (SQLException | URISyntaxException ex) {
            System.out.println("Connection Failed! Check output console" + ex.getMessage());
        }
    }


    public void removerProduto(String nome){
        Connection connection = null;
        PreparedStatement stmt = null;
        try{
            connection = Conexao.getConnection();
            stmt = connection.prepareStatement("delete from Produto where nome=?");
            stmt.setString(1, nome);
            stmt.executeUpdate();
            connection.close();
    }catch (SQLException | URISyntaxException ex) {
        System.out.println("Connection Failed! Check output console" + ex.getMessage());
    }
    
}

}
