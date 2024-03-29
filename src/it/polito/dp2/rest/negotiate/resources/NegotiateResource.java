
package it.polito.dp2.rest.negotiate.resources;

import java.net.URI;
import java.util.List;

import it.polito.dp2.rest.negotiate.model.Negotiate;
import it.polito.dp2.rest.negotiate.service.NegotiateService;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/** Resource class hosted at the URI relative path "/negotiateService"
 */
@Path("/negotiateService")
@Api(value = "/negotiateService", description = "a collection of negotiate objects")
public class NegotiateResource {
	// create an instance of the object that can execute operations
    NegotiateService service = new NegotiateService();
    
    /** Method processing HTTP GET requests, producing "text/plain" MIME media type.
     * @return String that will be send back as a response of type "text/plain".
     */
    @GET 
    @ApiOperation(	value = "get the negotiate objects ", notes = "text plain format")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces("text/plain")
    public String getNegotiateTextPlain() {
    	
        List<Negotiate> list = service.getNegotiate();
        StringBuilder sb = new StringBuilder();
        if(list.size()==0)
        	sb.append("No Element");
        else
        for(Negotiate no : list ){
        	if (no!=null) {
        		sb.append( "Negotiate object: Max " + no.getMax() + "; Min " + no.getMin());
        		sb.append("; Id " + no.getId() + ".\n");
        	}
        }
        
        return sb.toString();
    }

    @GET 
    @ApiOperation(	value = "get the negotiate objects", notes = "xml and json formats")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public List<Negotiate> getNegotiateXML() {

    	List<Negotiate> list =service.getNegotiate();
    	return list;
  
    }
    
    @GET 
    @Path("{id}")
    @ApiOperation(	value = "get a single negotiate object", notes = "json and xml formats"
	)
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 404, message = "Not Found"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Negotiate getSingleNegotiate(@PathParam("id") long id ) {

    	Negotiate neg = service.getSingleNegotiate(id);
    	if (neg == null)
    		throw new NotFoundException();
    	return neg; 
    }
    
    @POST
    @ApiOperation(	value = "create a new negotiate object", notes = "json and xml formats"
	)
    @ApiResponses(value = {
    		@ApiResponse(code = 201, message = "Created"),
    		@ApiResponse(code = 403, message = "Forbidden because negotiation failed"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response postNegotiateJson(Negotiate neg, @Context UriInfo uriInfo) {

    	Negotiate created = service.createNegotiate(neg);
    	if (created != null) { // success
        	UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        	URI u = builder.path(Long.toString(created.getId())).build();
        	return Response.created(u).entity(created).build();
    	} else
    		throw new ForbiddenException("Forbidden because negotiation failed");	
       
    }

    @DELETE
    @Path("{id}")
    @ApiOperation(	value = "remove a negotiate object", notes = "json and xml formats"
	)
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 404, message = "Not Found"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Negotiate deleteNegotiateJson(@PathParam("id") long id){
    	
    	Negotiate deleted = service.remove(id);
    	if (deleted != null) { // success
        	return deleted;
    	} else
    		throw new NotFoundException();	

    }
    
    @PUT
    @Path("{id}")
    @ApiOperation(	value = "update a negotiate object", notes = "json and xml formats"
	)
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 403, message = "Forbidden because negotiation failed"),
			@ApiResponse(code = 404, message = "Not found"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response  putNegotiateJson(@PathParam("id") long id, Negotiate neg, @Context UriInfo uriInfo) {

    	neg.setId(id);
    	Negotiate oldNeg = service.getSingleNegotiate(id);
    	if (oldNeg == null)
    		throw new NotFoundException();
    	Negotiate newNeg = service.modifyNegotiate(neg);
    	if (newNeg == null)
    		throw new ForbiddenException("Forbidden because negotiation failed");	
    	else
    		return Response.ok(newNeg).build();
    		
    }
    
}
