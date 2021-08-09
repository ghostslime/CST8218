/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cst8218.onei0212.service;

import cst8218.onei0212.entity.Sprite;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import cst8218.onei0212.entity.AbstractFacade;
import java.net.URI;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Implementation of the Restful API for this root resource and 
 * all of its defined sub-resources.
 * 
 * @author  Anwis O'Neill
 * @version 0.1
 * @since   1.8
 * @see     ApplicationConfig | AbstractFacade | Sprite
 */
@DeclareRoles("{Admin, RestGroup}")
@Stateless
@Path("cst8218.onei0212.entity.sprite")
public class SpriteFacadeREST extends AbstractFacade<Sprite> {
    @Context
    private UriInfo uriInfo;
    
    @PersistenceContext(unitName = "Assignment2-ejbPU")
    private EntityManager em;

    public SpriteFacadeREST() {
        super(Sprite.class);
    }
    
    
    /**
     * PUT operation on the root resource is not supported.
     * 
     * @return The NOT_IMPLEMENTED (501) response code indicating that this 
     *         feature is not supported.
     */
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"RestGroup", "Admin"})
    public Response SpriteTableReplace() {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
    
    
    /**
     * Provides Sprite creation or modification for the POST operation on 
     * the root resource - creates a new Sprite entity if 
     * the ID belonging to the entity is null. Otherwise, it will attempt 
     * to match the ID to an existing instance and will update the entity 
     * if successful.
     * 
     * @param entity The Sprite entity to either be created or updated 
     *               based on the presence and validity of its ID property.
     * @return Response code 400 (BAD_REQUEST) if:
     *         <ul>
     *           <li>entity is null</li>
     *           <li>
     *             entity.ID is not null but cannot be matched to an existing
     *             instance.
     *           </li>
     *           <li>
     *             entity is not valid
     *           </li>
     *         </ul>
     *         Response code 200 (OK) if the method could successfully update 
     *         or create the entity.
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"RestGroup", "Admin"})
    public Response CreateOrUpdate(Sprite entity) {
        Response response = null;
        
        if (null == entity) {
            // Error can't create or update
            response = Response.status(400, "Entity is null").build();
        } else if (entity.getId() != null) { /* UPDATE */
            if (find(entity.getId()) == null) {
                response = Response.status(
                        400, "Entity does not exist").build();
            } else {
                Sprite updatedSprite = find(entity.getId());
                updatedSprite.updateSprite(entity);
                
                super.edit(updatedSprite);
                response = Response.ok(updatedSprite).build();
            }
        } else { /* CREATE */
            // Check entity first
            if (!entity.isSpriteValid()) {
                response = Response.status(400, "Entity is invalid").build();
            } else {
                super.create(entity);
                response = Response.ok(entity).build();
            }
        }
        
        return response;
    }

    /**
     * Modifies the properties of the sprite having the same ID as 
     * supplied in the function argument with the non-null properties
     * of the supplied entity function argument.
     * 
     * @param id The id of the sprite to be modified.
     * @param entity The entity object holding the values to update the 
     *               sprite with the id parameter.
     * @return Response code 400 (BAD_REQUEST) if:
     *         <ul>
     *           <li>
     *             parameter id is null
     *           </li>
     *           <li>
     *             parameter entity is null
     *           </li>
     *           <li>
     *             parameter id does not lead to an existing Sprite
     *           </li>
     *           <li>
     *             parameter id and entity's id does not match
     *           </li>
     *         </ul>
     *         200 (OK) otherwise.
     */
    @POST
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"RestGroup", "Admin"})
    public Response edit(@PathParam("id") Long id, Sprite entity) {
        Response response = null;
        Sprite updatedSprite;
        
        if (null == id) {
            response = Response.status(
                    400,
                    "Passed ID is null.").build();
        } else if (null == entity) {
            response = Response.status(400, "Passed Entity is null.").build();
        } else if (find(id) == null) {
            response = Response.status(400, "Entity ID not found.").build();
        } else if (!id.equals(entity.getId())) {
            response = Response.status(
                    400, "Passed ID does not match passed entity ID").build();
        } else { // OK
            updatedSprite = find(id);
            updatedSprite.updateSprite(entity);
            
            super.edit(updatedSprite);
            response = Response.ok(updatedSprite).build();
        }
        
        return response;
    }

    
    /**
     * Replaces the entity selected by parameter "id" with the state 
     * of the parameter "entity".
     * 
     * @param id ID of the sprite to be replaced.
     * @param entity Entity to update the sought entity via id.
     * @return Response code 400 (BAD_REQUEST) if:
     *         <ul>
     *           <li>
     *             parameter id is null
     *           </li>
     *           <li>
     *             parameter entity is null
     *           </li>
     *           <li>
     *             parameter id does not lead to an existing Sprite
     *           </li>
     *           <li>
     *             parameter id and entity's id does not match
     *           </li>
     *         </ul>
     *         200 (OK) otherwise.
     */
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"RestGroup", "Admin"})
    public Response replace(@PathParam("id") Long id, Sprite entity) {
        Response response = null;
        
        if (null == id) {
            response = Response.status(400, "ID is null").build();
        } else if (null == entity) {
            response = Response.status(400, "Entity is null").build();
        } else if (find(id) == null) {
            response = Response.status(400, "ID not found in database").build();
        } else if (!id.equals(entity.getId())) {
            response = Response.status(
                    400, "ID and Entity.ID does not match").build();
        } else { 
            if (!entity.isSpriteValid()) {
                response = Response.status(400, "Entity is invalid").build();
            } else {
                super.edit(entity);
                response = Response.ok(entity).build();
            }
        }
        
        return response;
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed({"RestGroup", "Admin"})
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"RestGroup", "Admin"})
    public Sprite find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"RestGroup", "Admin"})
    public List<Sprite> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed({"RestGroup", "Admin"})
    public List<Sprite> findRange(
            @PathParam("from") Integer from, 
            @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed({"RestGroup", "Admin"})
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
