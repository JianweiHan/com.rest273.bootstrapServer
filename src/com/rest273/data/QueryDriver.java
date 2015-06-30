package com.rest273.data;

import java.net.UnknownHostException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class QueryDriver {
	
	
	public static DBCollection getmongoDbCollection(String database, String selectCollection) throws UnknownHostException{
		DB dB=(new MongoClient("localhost",27017)).getDB(database);
		DBCollection dBCollection = dB.getCollection(selectCollection);
		return dBCollection;
	}
	
	public static void findAll(DBCollection channelDBCollection){
		DBCursor dbCursor = channelDBCollection.find();
		while (dbCursor.hasNext()) System.out.println(dbCursor.next());
	}
	
	public static void insertJSON(String obj,DBCollection channelDBCollection) {
		
		channelDBCollection.insert((DBObject) JSON.parse(obj));
	}
	
/*
	public static void insertJSONArray(String array,DBCollection channelDBCollection) {
		
		channelDBCollection.insert((DBObject) JSON.parse(array));
	}
*/	
	
	public static void delete(String name,String value, DBCollection channelDBCollection) {
		
		DBObject basicDBObject = new BasicDBObject();
		basicDBObject.put(name,value);
		channelDBCollection.remove(basicDBObject);
	}
	
	public static void delete(DBObject obj, DBCollection channelDBCollection) {
		
		channelDBCollection.remove(obj);
	}
	
	
	public static void update(String fromName, String fromValue, String toName, String toValue, DBCollection channelDBCollection) {
		
		DBObject fromDBObject = new BasicDBObject();
		fromDBObject.put(fromName, fromValue);
		DBObject toDBObject = new BasicDBObject();
		toDBObject.put(toName, toValue);
		DBObject updateDBObject = new BasicDBObject();
		updateDBObject.put("$set", toDBObject);
		channelDBCollection.update(fromDBObject, updateDBObject);
	}
	
	public static void update(DBObject fromDBObject,DBObject toDBObject, DBCollection channelDBCollection) {
		
		DBObject updateDBObject = new BasicDBObject();
		updateDBObject.put("$set", toDBObject);
		channelDBCollection.update(fromDBObject, updateDBObject);
	}

}
