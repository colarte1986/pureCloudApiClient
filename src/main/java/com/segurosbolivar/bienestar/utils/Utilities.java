package com.segurosbolivar.bienestar.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.slf4j.*;
import com.segurosbolivar.bienestar.model.PureCloudRequest;


public class Utilities {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(Utilities.class);
	
	private String script_id="bd6aeed7-3e0b-4e25-a648-43edb490c490";
    private String queue_id="5a3830b8-e6eb-41ac-8892-f89d05c68967";
	
    
    
	//construct request body 
	public String getRequestBody(PureCloudRequest pureCloudRequest) {
		

		JSONObject jsonObject = new JSONObject(); 
		JSONObject routingData = new JSONObject();
		JSONObject data = new JSONObject();
		JSONArray callbackNumbers = new JSONArray();
		
		try {	
			jsonObject.put("scriptId", script_id);
			routingData.put("queueId", queue_id);
			jsonObject.put("routingData", routingData);
			jsonObject.put("callbackUserName", "Seguros Bolivar");
			pureCloudRequest.getCallbackNumbers().forEach(number->callbackNumbers.put(number));
			jsonObject.put("callbackNumbers", callbackNumbers);
			jsonObject.put("callbackScheduledTime", "2019-03-13T17:00:00.000Z");
			data.put("documentType", pureCloudRequest.getDocumentType());
			data.put("documentNumber", pureCloudRequest.getDocumentNumber());
			data.put("requestType", "Rescisión");
			data.put("observations", "Petición realizada");
			
		} catch (Exception e) {
			
			LOGGER.error("Exception happens while trying to make JSON object at PureCloud Rest Client", e.getMessage());
		
		}
		
		
		return jsonObject.toString();
	}
	
}
