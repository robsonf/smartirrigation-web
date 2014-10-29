package br.ifce.crato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AgendaDAO {

	public List<Agenda> findAll() {
		List<Agenda> list = new ArrayList<Agenda>();
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			Statement stm = c.createStatement();
			ResultSet rs, rs2;
			rs = stm.executeQuery("SELECT a.id AS id, a.horaInicial, a.horaFinal, a.estado AS estado, a.seg AS seg, a.ter AS ter, a.qua AS qua, a.qui AS qui, a.sex AS sex, a.sab AS sab, a.dom AS dom  FROM agenda a order by a.id");
			while (rs.next()) {
				Agenda a = new Agenda();
				a.setId(rs.getInt("id"));
				a.setHoraInicial(rs.getString("horaInicial"));
				a.setHoraFinal(rs.getString("horaFinal"));
				a.setEstado(rs.getBoolean("estado"));
				a.setSeg(rs.getBoolean("seg"));
				a.setTer(rs.getBoolean("ter"));
				a.setQua(rs.getBoolean("qua"));
				a.setQui(rs.getBoolean("qui"));
				a.setSex(rs.getBoolean("sex"));
				a.setSab(rs.getBoolean("sab"));
				a.setDom(rs.getBoolean("dom"));

				Statement stm2 = c.createStatement();
				rs2 = stm2
						.executeQuery(""
								+ "SELECT s.id AS setor_id, s.descricao AS descricao"
								+ " FROM agenda a, agenda_setores ase, setor s"
								+ " WHERE a.id = ase.agenda_id"
								+ " AND s.id = ase.setor_id" + " AND a.id="
								+ a.getId());

				List<Setor> setores = new ArrayList<Setor>();
				while (rs2.next()) {
					setores.add(new Setor(rs2.getString("descricao"), rs2
							.getInt("setor_id")));
				}
				a.setSetores(setores);
				list.add(a);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return list;
	}

	public Agenda findById(int id) {
		Connection c = null;
		Agenda a = new Agenda();
		try {
			c = ConnectionHelper.getConnection();
			Statement stm = c.createStatement();
			ResultSet rs, rs2;
			rs = stm.executeQuery("SELECT a.id AS id, a.horaInicial, a.horaFinal, a.estado AS estado, a.seg AS seg, a.ter AS ter, a.qua AS qua, a.qui AS qui, a.sex AS sex, a.sab AS sab, a.dom AS dom  FROM agenda a WHERE a.id="
					+ id);
			while (rs.next()) {
				a.setId(rs.getInt("id"));
				a.setHoraInicial(rs.getString("horaInicial"));
				a.setHoraFinal(rs.getString("horaFinal"));
				a.setEstado(rs.getBoolean("estado"));
				a.setSeg(rs.getBoolean("seg"));
				a.setTer(rs.getBoolean("ter"));
				a.setQua(rs.getBoolean("qua"));
				a.setQui(rs.getBoolean("qui"));
				a.setSex(rs.getBoolean("sex"));
				a.setSab(rs.getBoolean("sab"));
				a.setDom(rs.getBoolean("dom"));

				Statement stm2 = c.createStatement();
				rs2 = stm2
						.executeQuery(""
								+ "SELECT s.id AS setor_id, s.descricao AS descricao"
								+ " FROM agenda a, agenda_setores ase, setor s"
								+ " WHERE a.id = ase.agenda_id" + " AND a.id ="
								+ id + " AND s.id = ase.setor_id"
								+ " AND a.id=" + a.getId());
				List<Setor> setores = new ArrayList<Setor>();
				while (rs2.next()) {
					setores.add(new Setor(rs2.getString("descricao"), rs2
							.getInt("setor_id")));
				}
				a.setSetores(setores);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return a;
	}

	public Agenda create(Agenda a) {
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			c.setAutoCommit(true);
			Statement stm = c.createStatement();
			ResultSet rs = stm.executeQuery("SELECT (max(id)+1) as idAgenda from agenda");
			int id = 1;
			while (rs.next()) {
				if(rs.getInt("idAgenda") !=0 )
					id = rs.getInt("idAgenda");
			}
			rs.close();
			stm.close();

			PreparedStatement ps = c.prepareStatement("INSERT INTO agenda (horaInicial, horaFinal, estado, seg, ter, qua, qui, sex, sab, dom, id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ id + ")");
			ps.setString(1, a.getHoraInicial());
			ps.setString(2, a.getHoraFinal());
			ps.setBoolean(3, a.isEstado());
			ps.setBoolean(4, a.isSeg());
			ps.setBoolean(5, a.isTer());
			ps.setBoolean(6, a.isQua());
			ps.setBoolean(7, a.isQui());
			ps.setBoolean(8, a.isSex());
			ps.setBoolean(9, a.isSab());
			ps.setBoolean(10, a.isDom());
			ps.execute();
			
			for (Setor s : a.getSetores()) {
				if(s!=null)
				if (s.getId() != 0) {
					PreparedStatement ps2 = c.prepareStatement("INSERT INTO agenda_setores (setor_id, agenda_id) VALUES (?, ?)");
					ps2.setInt(1, s.getId());
					ps2.setInt(2, id);
					ps2.execute();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return a;
	}

	public Agenda update(Agenda a) {
		Connection c = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		try {
			c = ConnectionHelper.getConnection();
			ps = c.prepareStatement("UPDATE agenda SET horaInicial=?, horaFinal=?, estado=?, seg=?, ter=?, qua=?, qui=?, sex=?, sab=?, dom=? WHERE id=?");
			ps.setString(1, a.getHoraInicial());
			ps.setString(2, a.getHoraFinal());
			ps.setBoolean(3, a.isEstado());
			ps.setBoolean(4, a.isSeg());
			ps.setBoolean(5, a.isTer());
			ps.setBoolean(6, a.isQua());
			ps.setBoolean(7, a.isQui());
			ps.setBoolean(8, a.isSex());
			ps.setBoolean(9, a.isSab());
			ps.setBoolean(10, a.isDom());
			ps.setInt(11, a.getId());
			ps.executeUpdate(); 

			ps2 = c.prepareStatement("DELETE FROM agenda_setores WHERE agenda_id=?");
			ps2.setInt(1, a.getId());
			ps2.executeUpdate();
			
			for (Setor s : a.getSetores()) {
				if(s.getId() != 0){
					ps3 = c.prepareStatement(
							"INSERT INTO agenda_setores(setor_id, agenda_id) VALUES (?,?)");
					ps3.setInt(1, s.getId());
					ps3.setInt(2, a.getId());
					ps3.executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
		return a;
	}

	public void update(boolean estado) {
		Connection c = null;
		PreparedStatement ps = null;
		try {
			c = ConnectionHelper.getConnection();
			ps = c.prepareStatement("UPDATE agenda SET estado=?");
			ps.setBoolean(1, estado);
			ps.executeUpdate(); 
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
	}

	public boolean remove(int id) {
		Connection c = null;
		try {
			c = ConnectionHelper.getConnection();
			PreparedStatement ps2 = c.prepareStatement("DELETE FROM agenda_setores WHERE agenda_id=?");
			ps2.setInt(1, id);
			ps2.executeUpdate();

			c = ConnectionHelper.getConnection();
			PreparedStatement ps = c
					.prepareStatement("DELETE FROM agenda WHERE id=?");
			ps.setInt(1, id);
			int count = ps.executeUpdate();
			ps.close();
			ps2.close();
			return count == 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			ConnectionHelper.close(c);
		}
	}

}
