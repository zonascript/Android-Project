package Rest;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import hibernate.HibernateHelper;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ThreadService.AssignmentService;
import entity.Request;
import entity.RequestAccepted;
import entity.RequestDetail;
import entity.RequestOrder;
import entity.ServiceProvide;
import entity.User;


@Path("/jasaService")
public class SimpleRestService {

	private static final Logger logger = Logger.getLogger(SimpleRestService.class);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("status")
	public Response getStatus() {
		//pushNotificationToFirebase.main(null);
//		boolean closeAfter = false;
//		try {
//	        closeAfter = HibernateHelper.beginTx();
//	        Session session = HibernateHelper.getSession();
//	        
//	        List<Object[]> user = session.createSQLQuery("Select * from TBL_USER").list();
//	        for (Object object[] : user) {
//	        	System.out.println(object[0]);
//	        	System.out.println(object[1]);
//	        	System.out.println(object[2]);
//	        	System.out.println(object[3]);
//	        }
//	        
//	        session.beginTransaction();
//	        HibernateHelper.commitTx(closeAfter);
//	    } catch (Exception e) {
//	        HibernateHelper.rollbackTx(closeAfter);
//	        e.printStackTrace();
//	    }
		return Response.ok(
				"{\"status\":\"Web Service is running...\"}").build();
	}
	
	@POST
	@Path("/postAddUser")
    @Consumes(MediaType.APPLICATION_JSON)
	public Response postAddUser(
			User user) {
		String response = null;
		
		boolean closeAfter = false;
		try {
	        closeAfter = HibernateHelper.beginTx();
	        Session session = HibernateHelper.getSession();
	        session.beginTransaction();
	        boolean isExistUser = false;
	        
	        Criteria crt = session.createCriteria(User.class);
	        crt.add(Restrictions.eq("email", user.getEmail()));
	        List<User> existUser = crt.list();

	        Criteria crt1 = session.createCriteria(User.class);
	        crt1.add(Restrictions.eq("noTelp", user.getNoTelp()));
	        List<User> existUser1 = crt.list();
	        
	        
	        if((existUser.size()>0) && (existUser1.size()>0)){
	        	isExistUser = true;
		        response = "Email dan No. Telpon anda sudah terdaftar \n Gunakan email dan No. Telp yang lain";

	        }else{
		        if(existUser.size()>0){
		        	isExistUser = true;
			        response = "Email anda sudah terdaftar \n Gunakan email yang lain";
		        }
		        if(existUser1.size()>0){
		        	isExistUser = true;
			        response = "No Telp. anda sudah terdaftar \n Gunakan No Telpon lain";
		        }

	        }
	        
	        if(!isExistUser){
		        session.save(user);
		        session.flush();
		        response = "Succes";
		        
		        PushNotificationToFirebase pushNotif = new PushNotificationToFirebase();
		        pushNotif.pushNotificationToClient(user.getToken(), "Konfirmasi Pendaftaran", "Terimaksih, Pendaftaran Account anda berhasil");

	        }
	        HibernateHelper.commitTx(closeAfter);
	        
	    } catch (Exception e) {
	        HibernateHelper.rollbackTx(closeAfter);
	        e.printStackTrace();
	        response = "Error";
	    }
        return Response.status(201).entity(response).build();	
	}
	
	@POST
	@Path("/postUpdateUser")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postUpdateUser(User user) {
		String response = null;
		
		boolean closeAfter = false;
		try {
	        closeAfter = HibernateHelper.beginTx();
	        Session session = HibernateHelper.getSession();
	        session.beginTransaction();
	        
	        session.update(user);
	        session.flush();
	        
	        response = "Succes";
	        
	        HibernateHelper.commitTx(closeAfter);
	    } catch (Exception e) {
	        HibernateHelper.rollbackTx(closeAfter);
	        e.printStackTrace();
	        response = "Error";
	    }
		return Response.status(201).entity(response).build();
	}
	
	
	@GET
	@Path("/getUser")
    @Produces(MediaType.APPLICATION_JSON)
	public Response postGetUser(
			@QueryParam("email") String email,
			@QueryParam("password") String password
			) {
		String response = null;
		String json = "";
		boolean closeAfter = false;
		try {
	        closeAfter = HibernateHelper.beginTx();
	        Session session = HibernateHelper.getSession();
	        session.beginTransaction();
	        
	        Criteria crt = session.createCriteria(User.class)
	        				.add(Restrictions.eq("email", email))
	        				.add(Restrictions.eq("password", password));
	        User user = (User) crt.setMaxResults(1).uniqueResult();
	        if(user != null){
	        	json = new Gson().toJson(user);
	        	response = "Succes";
	        }else{
	        	response = "Gagal";
	        }
	        
	        
	        
	        HibernateHelper.commitTx(closeAfter);
	        
	        return Response.ok(response+"/"+json).build();
	    } catch (Exception e) {
	        HibernateHelper.rollbackTx(closeAfter);
	        e.printStackTrace();
	        response = "Error";
	        return Response.serverError().build();
	    }
        
	}
	
	@POST
	@Path("/postAddServiceProvides")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postUpdateUser(String serviceProvides) {
		String response = null;
		boolean closeAfter = false;		
		List<ServiceProvide> listServiceProvide = new ArrayList<>();
		
		try {
			if(!serviceProvides.isEmpty()){
				JsonParser jp = new JsonParser();
				JsonObject jo = (JsonObject) jp.parse(serviceProvides);
				JsonArray ja = jo.getAsJsonArray("serviceProvides");
				Gson gson = new Gson();
				for (JsonElement jsonElement : ja) {
					ServiceProvide sp = gson.fromJson(jsonElement, ServiceProvide.class);
					String fidUser = sp.getIdServiceProvide().split("/")[0];
					sp.setFidUser(fidUser);
					listServiceProvide.add(sp);
				}
			
				closeAfter = HibernateHelper.beginTx();
		        Session session = HibernateHelper.getSession();
		        session.beginTransaction();
		        
				for(ServiceProvide sp : listServiceProvide){
					Criteria cr = session.createCriteria(ServiceProvide.class).add(Restrictions.eq("idServiceProvide", sp.getIdServiceProvide()));
					List<ServiceProvide> listSP = cr.list();
					if(listSP.size()==0){
						session.save(sp);
				        session.flush();
				    }
					
					
				}
		        response = "Succes";
		        HibernateHelper.commitTx(closeAfter);
			}
	    } catch (Exception e) {
	        HibernateHelper.rollbackTx(closeAfter);
	        e.printStackTrace();
	        response = "Error";
	    }
		return Response.status(201).entity(response).build();
	}
	
	@POST
	@Path("/postDeleteServiceProvides")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postDeleteServiceProvides(ServiceProvide serviceProvide) {
		String response = null;
		boolean closeAfter = false;
		try {
	        closeAfter = HibernateHelper.beginTx();
	        Session session = HibernateHelper.getSession();
	        session.beginTransaction();
	        
	        session.delete(serviceProvide);
	        session.flush();
	        
	        response = "Succes";
	        
	        HibernateHelper.commitTx(closeAfter);
	    } catch (Exception e) {
	        HibernateHelper.rollbackTx(closeAfter);
	        e.printStackTrace();
	        response = "Error";
	    }
		return Response.status(201).entity(response).build();
	}
	
	@POST
	@Path("/postAddRequestOrder")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postAddRequestOrder(String request) {
		String response = null;
		
		boolean closeAfter = false;
		String idRequest = "";
		try {
	        closeAfter = HibernateHelper.beginTx();
	        Session session = HibernateHelper.getSession();
	        session.beginTransaction();
	        
	        String splitRequest[] = request.split("#");
	        String requestO = splitRequest[0];
	        String requestDet = splitRequest[1];

			
			Gson gson = new Gson();
			RequestOrder ro = gson.fromJson(requestO, RequestOrder.class);
			// covert RequestOrder to Request
	        Request req = new Request(ro);
	        idRequest = ro.getIdRequest();
	        session.save(req);
	        session.flush();
			

			JsonParser jp = new JsonParser();
			JsonObject jo = (JsonObject) jp.parse(requestDet);
			JsonArray jaRequestDetail = jo.getAsJsonArray("requestDetail");

			for (JsonElement jsonElement : jaRequestDetail) {
				RequestDetail rd = gson.fromJson(jsonElement, RequestDetail.class);
				session.save(rd);
		        session.flush();
			}
	        
	        response = "Succes";
	        
	        HibernateHelper.commitTx(closeAfter);
	    } catch (Exception e) {
	        HibernateHelper.rollbackTx(closeAfter);
	        e.printStackTrace();
	        response = "Error";
	    } finally {
			AssignmentService assignmentService = new AssignmentService();
			assignmentService.setIdRequest(idRequest);
			assignmentService.run();
		}
		return Response.status(201).entity(response).build();
	}
	
	@GET
	@Path("/getRequestAccept")
    @Produces(MediaType.APPLICATION_JSON)
	public Response getRequestAccept(
			@QueryParam("idRequest") String idRequest,
			@QueryParam("idUserPenyediaJasa") String idUserPenyediaJasa
			) {
		String response = null;
		String json = "";
		boolean closeAfter = false;
		try {
	        closeAfter = HibernateHelper.beginTx();
	        Session session = HibernateHelper.getSession();
	        session.beginTransaction();
	        
	        String SQL = "EXECUTE DBO.SELECT_REQUEST_ACCEPT '"+idRequest+"','"+idUserPenyediaJasa+"'";	        
	        List<Object[]> listObject = session.createSQLQuery(SQL).list();
	        RequestAccepted ra = new RequestAccepted();
	        for (Object[] objects : listObject) {
				ra.setIdRequest(objects[0].toString());
				ra.setFidService(objects[1].toString());
				ra.setFidServiceProvide(objects[2].toString());
				ra.setFidUserCreate(objects[3].toString());
				ra.setClientName(objects[4].toString());
				ra.setClientFotoProfil(objects[5]!=null?objects[5].toString():"");
				ra.setClientNoTelfon(objects[6].toString());
				ra.setStatus(objects[7].toString());
			}
	        
	        Criteria criteria = session.createCriteria(RequestDetail.class).add(Restrictions.eq("fidRequest", idRequest)).addOrder(Order.asc("fidServiceItem"));
	        List<RequestDetail> listRequestDetail = criteria.list();
	        
	        HibernateHelper.commitTx(closeAfter);
	        
	        Gson gson = new Gson();
	        json = gson.toJson(ra)+"#{requestDetail : "+gson.toJson(listRequestDetail)+"}";
	        
	        return Response.ok(json).build();
	    } catch (Exception e) {
	        HibernateHelper.rollbackTx(closeAfter);
	        e.printStackTrace();
	        response = "Error";
	        return Response.serverError().build();
	    }
	}	
	/*
	@GET
	@Path("/<add method name here>")
    @Produces(MediaType.TEXT_PLAIN)
	public String getSomething(@QueryParam("request") String request ,
			 @DefaultValue("1") @QueryParam("version") int version) {

		if (logger.isDebugEnabled()) {
			logger.debug("Start getSomething");
			logger.debug("data: '" + request + "'");
			logger.debug("version: '" + version + "'");
		}

		String response = null;

        try{			
            switch(version){
	            case 1:
	                if(logger.isDebugEnabled()) logger.debug("in version 1");

	                response = "Response from RESTEasy Restful Webservice : " + request;
                    break;
                default: throw new Exception("Unsupported version: " + version);
            }
        }
        catch(Exception e){
        	response = e.getMessage().toString();
        }
        
        if(logger.isDebugEnabled()){
            logger.debug("result: '"+response+"'");
            logger.debug("End getSomething");
        }
        return response;	
	}

	@POST
	@Path("/<add method name here>")
    @Produces(MediaType.TEXT_PLAIN)
	public String postSomething(@FormParam("request") String request ,  @DefaultValue("1") @FormParam("version") int version) {

		if (logger.isDebugEnabled()) {
			logger.debug("Start postSomething");
			logger.debug("data: '" + request + "'");
			logger.debug("version: '" + version + "'");
		}

		String response = null;

        try{			
            switch(version){
	            case 1:
	                if(logger.isDebugEnabled()) logger.debug("in version 1");

	                response = "Response from RESTEasy Restful Webservice : " + request;
                    break;
                default: throw new Exception("Unsupported version: " + version);
            }
        }
        catch(Exception e){
        	response = e.getMessage().toString();
        }
        
        if(logger.isDebugEnabled()){
            logger.debug("result: '"+response+"'");
            logger.debug("End postSomething");
        }
        return response;	
	}

	@PUT
	@Path("/<add method name here>")
    @Produces(MediaType.TEXT_PLAIN)
	public String putSomething(@FormParam("request") String request ,  @DefaultValue("1") @FormParam("version") int version) {
		if (logger.isDebugEnabled()) {
			logger.debug("Start putSomething");
			logger.debug("data: '" + request + "'");
			logger.debug("version: '" + version + "'");
		}

		String response = null;

        try{			
            switch(version){
	            case 1:
	                if(logger.isDebugEnabled()) logger.debug("in version 1");

	                response = "Response from RESTEasy Restful Webservice : " + request;
                    break;
                default: throw new Exception("Unsupported version: " + version);
            }
        }
        catch(Exception e){
        	response = e.getMessage().toString();
        }
        
        if(logger.isDebugEnabled()){
            logger.debug("result: '"+response+"'");
            logger.debug("End putSomething");
        }
        return response;	
	}

	@DELETE
	@Path("/<add method name here>")
	public void deleteSomething(@FormParam("request") String request ,  @DefaultValue("1") @FormParam("version") int version) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Start deleteSomething");
			logger.debug("data: '" + request + "'");
			logger.debug("version: '" + version + "'");
		}


        try{			
            switch(version){
	            case 1:
	                if(logger.isDebugEnabled()) logger.debug("in version 1");

                    break;
                default: throw new Exception("Unsupported version: " + version);
            }
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        
        if(logger.isDebugEnabled()){
            logger.debug("End deleteSomething");
        }
	}*/
}
