package id.co.sigma.mx.channel.bri.syariah.ws;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import id.co.sigma.mx.channel.bri.syariah.ws.model.Data;

/**
 *
 * @author <a href="mailto:muhammad.ichsan@sigma.co.id">Muhammad Ichsan</a>
 *
 */
//@Path("/channel")
@Produces({"application/xml", "application/json"})
@WebService
public interface Channel {

	@PUT
	@Path("/login")
	public void login(@WebParam String username, @WebParam String password);

	@POST
	@Path("/exec/{mappingCode}")
	public Data execute(
			@PathParam("mappingCode") @WebParam(name = "requestCode") String mappingCode, // WebParam diperlukan agar nama field gak berubah
			@WebParam(name = "reqData") Data requestData);

	@GET
	@Path("/sample")
	public Data showSample();
}
