/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WS.service;

import WS.Usuarios;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * Facade encargada de recibir y procesar las solicitudes de los clientes para la
 * Applicación WS Seguro desarrollada para TTFFE Maestria Seguridad Informática UBA
 * @author DAG.
 */
@Stateless
@Path("ws.usuarios")
public class UsuariosFacadeREST extends AbstractFacade<Usuarios> {

    private final static String DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss z";
    private final static String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private final static String SECRET_KEY = "ubaWS.HMAC";

    @PersistenceContext(unitName = "WebApplicationUBATestPU")
    private EntityManager em;

    public UsuariosFacadeREST() {
        super(Usuarios.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Usuarios entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Usuarios entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Usuarios find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    /**
     * Médoto que recibe la solicitud de un cliente para el servicio verificarHmac
     * @param sc Contexto de seguridad usado en la autenticación y autorización de clientes
     * @param hmac El hmac recibido en el parametro del encabezado "hmac"
     * @param data El texto plano con el que se genero el hmac enviado en el encabezado
     * @return 
     */
    @GET
    @Path("verificarHmac")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
    public String getUsersHmac(@Context SecurityContext sc, @HeaderParam("hmac") String hmac, @HeaderParam("data") String data) {
        System.out.println("SC: " + sc + " scheme: " + sc.getAuthenticationScheme() + "; test: " + sc.getUserPrincipal());
        String currentDate = new SimpleDateFormat(DATE_FORMAT).format(new Date(System.currentTimeMillis()));
        if (hmac != null && !hmac.isEmpty()) {
            System.out.println("HMAC recibido: " + hmac + "\nEmpieza verificación:" + currentDate);
            if (validarData(hmac, data)) {
                currentDate = new SimpleDateFormat(DATE_FORMAT).format(new Date(System.currentTimeMillis()));
                System.out.println("HMAC validado correctamente: " + currentDate);
                return "HMAC valido";
            } else {
                currentDate = new SimpleDateFormat(DATE_FORMAT).format(new Date(System.currentTimeMillis()));
                System.out.println("HMAC invalido: " + currentDate);
                return "HMAC invalido";
            }
        } else {
            return "HMAC no encontrado invalido";
        }
    }

    /**
     * Método que valida el digest genrado por la data enviada por el cliente
     *
     * @param hmac el digest
     * @param data la data enviada por el cliente
     * @return Si la validacion es correcta si el digest generado con la data es
     * equivalente al digest recibido
     */
    private boolean validarData(String hmac, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            SecretKeySpec secret_key = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), HMAC_SHA256_ALGORITHM);
            sha256_HMAC.init(secret_key);
            byte[] rawHmac = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(rawHmac).equals(hmac);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException ex) {
            System.out.println("ERROR: --->" + ex.getLocalizedMessage());
            return false;
        }
    }

    @POST
    @Path("Login")
    @Consumes("application/x-www-form-urlencoded")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Usuarios finIdUser(@FormParam("email") String email, @FormParam("password") String password) {
        System.out.println("user:" + email + ",pass: " + password);
        List<Usuarios> usuarios = super.findAll();
        for (int i = 0; i < usuarios.size(); i++) {
            Usuarios temp = usuarios.get(i);
            if (temp.getEmail().equals(email) && temp.getPassword().equals(password)) {
                return temp;
            }
        }
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuarios> findAll(@Context SecurityContext sc) {
        System.out.println("SC" + sc + " scheme: " + sc.getAuthenticationScheme());
        //System.out.println("SC---->" + sc);
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuarios> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
