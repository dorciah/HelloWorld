package com.simon.tools;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONTools {

	 /* load an object serialized as json from a file */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object fromJSON(String fileLoc, Class type){
        Object output = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            output = mapper.readValue(new File(fileLoc),type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;

    }

    /* load an object serialized as json from a string */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object fromJSONString(String input, Class type){
        Object output = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            output = mapper.readValue(input,type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;

    }
    
    /* load an object serialized as json from a String. Object contains lists */
	public static Object fromJSONStringWithList(String input, @SuppressWarnings("rawtypes") Class classType){
        Object output = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            output = mapper.readValue(input, mapper.getTypeFactory().constructCollectionType(List.class, classType));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;

    }
    
    /* pretty print an object as json */
    public static String toJSONString(Object input){
        String output = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);        
        try {
            output = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return output;
    }
    
    /* simple print an object as json */
    public static String toJSONStringSimple(Object input){
        String output = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);        
        try {
            output = mapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return output;
    }
	
}
