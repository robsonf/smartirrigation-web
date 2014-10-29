package br.ifce.crato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SetorDAO {

    public List<Setor> findAll() {
        List<Setor> list = new ArrayList<Setor>();
        Connection c = null;
    	String sql = "SELECT * FROM setor ORDER BY descricao";
        try {
            c = ConnectionHelper.getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                list.add(processRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
        return list;
    }

    protected Setor processRow(ResultSet rs) throws SQLException {
        Setor s = new Setor();
        s.setId(rs.getInt("id"));
        s.setDescricao(rs.getString("descricao"));
        return s;
    }
    
}
