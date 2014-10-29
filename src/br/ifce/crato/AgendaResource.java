package br.ifce.crato;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.HttpHeaders;

import com.sun.jersey.core.util.Base64;

@Path("/agendas")
public class AgendaResource {

	AgendaDAO dao = new AgendaDAO();
	
//    @Context HttpHeaders headers;
//    protected boolean authenticate() {
//        String header = headers.getRequestHeader("authorization").get(0);
//        header = header.substring("Basic ".length());
//        String[] creds = new String(Base64.base64Decode(header)).split(":");
//        String username = creds[0];
//        String password = creds[1];
//        System.out.println("usuario "+ username);
//        System.out.println("senha "+ password);
//        return false;
//    }
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Agenda> findAll() {
		System.out.println("findAll");
		return dao.findAll();
	}


	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Agenda create(Agenda a) {
		System.out.println("creating agenda");
		return dao.create(a);
	}
	
	@GET @Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Agenda findById(@PathParam("id") String id) {
		System.out.println("findById " + id);
		return dao.findById(Integer.parseInt(id));
	}

	@PUT @Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Agenda update(Agenda a) {
		System.out.println("Updating agenda: " + a.getId());
		dao.update(a);
		return a;
	}
	
	@POST @Path("{estado}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public void updateEstado(@PathParam("estado") Boolean estado) {
		System.out.println("Updating estado das agendas: " + estado);
		dao.update(estado);
	}

	@DELETE @Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void remove(@PathParam("id") int id) {
		dao.remove(id);
	}

}
