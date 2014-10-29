package br.ifce.crato;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class Setor {
	private String descricao;
	private int id;
	public Setor() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Setor(String descricao, int id) {
		super();
		this.descricao = descricao;
		this.id = id;
	}
	// getters e setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	
}
