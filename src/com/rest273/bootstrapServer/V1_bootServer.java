package com.rest273.bootstrapServer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.rest273.data.QueryDriver;


@Path("/V1/server/")
public class V1_bootServer {
	static JSONArray jsonarray = new JSONArray();

	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTrackInJSON() throws Exception {
		
		Response rb = null;
		
		DBCollection dBCollection = QueryDriver.getmongoDbCollection("zaneacademydatabase", "Channel"); 
		
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("name", "zaneacademy");
		DBCursor dbCursor = dBCollection.find(basicDBObject);
		if(dbCursor.hasNext()) 
		{
			String next = dbCursor.next().toString();
			System.out.println(next);
			rb = Response.ok(next).build();
		}
		return rb;
	}
	
	
	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createTrackInJSON(String incomingData) throws Exception {

		JSONObject returnJson = new JSONObject(); 
		JSONObject incomingJson=new JSONObject(incomingData);
        DBCollection dBCollection = QueryDriver.getmongoDbCollection("bootserver", "clientinventory"); 
		
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("clientESN_URN", incomingJson.getString("clientESN_URN"));
		DBCursor dbCursor = dBCollection.find(basicDBObject);
		if(dbCursor.hasNext()) 
		{
			JSONObject jsonFind= new JSONObject(dbCursor.next().toString());
			String serverURI=jsonFind.getString("serverURI");
			returnJson.put("serverURI", serverURI);
            JSONObject jsonFindUpdate= new JSONObject(jsonFind.toString());
            jsonFindUpdate.remove("boot");
            jsonFindUpdate.put("boot", true);
            
            QueryDriver.update((DBObject)JSON.parse(jsonFind.toString()), (DBObject)JSON.parse(jsonFindUpdate.toString()), dBCollection);

		}

		else {
			return Response.status(500).entity("URN is not matched, and bootstrap denied. ").build();
		}
		
		
		

		/*
		 * try { System.out.println("incomingData: " + incomingData);
		 * 
		 * 
		 * 
		 * ObjectMapper mapper = new ObjectMapper(); ItemEntry itemEntry =
		 * mapper.readValue(incomingData, ItemEntry.class);
		 * 
		 * int http_code = dao.insertIntoPC_PARTS(itemEntry.clientID,
		 * itemEntry.times);
		 * 
		 * if( http_code == 200 ) { json =
		 * dao.queryReturnbrandParts(itemEntry.clientID); returnString =
		 * json.toString();
		 * 
		 * } else { return
		 * Response.status(500).entity("Unable to process Item").build(); }
		 * 
		 * } catch (Exception e) { e.printStackTrace(); return
		 * Response.status(500
		 * ).entity("Server was not able to process your request").build(); }
		 */
		
		return Response.status(201).entity(returnJson).build();
	}

}
