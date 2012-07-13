package com.smugmug.core

import com.sun.jersey.api.client.*
import com.sun.jersey.core.util.*
import com.sun.jersey.oauth.client.*
import com.sun.jersey.oauth.signature.*
import grails.converters.JSON
import javax.ws.rs.core.*

class SmCoreService {

	def grailsApplication
    
    String endpoint = "https://api.smugmug.com/services/api/json/1.3.0/"

    def getApiKey(){
        return grailsApplication?.config?.smugmug?.apiKey
    }

    def getApiSecret(){
        return grailsApplication?.config?.smugmug?.apiSecret
    }

    Boolean isConfigured(){
        return (apiKey instanceof String && apiSecret instanceof String)
    }

    def send(String method, ArrayList args){
    	def url = buildURL(method,args)
    	return JSON.parse(new URL(url).getText())
    }
	
	def sendOAuth(String method, ArrayList args){
		// Create a Jersey client
		Client client = Client.create()
		
        String modifiedEndpoint = "${endpoint}?rnd=${new Date().getTime()}"
		args?.each{ arg ->
			modifiedEndpoint += "&${arg.key}=${arg.value}"
		}
		
		// Create a resource to be used to make SmugMug API calls
		WebResource resource = client.resource(modifiedEndpoint).queryParam("method",method)
 
		// Set the OAuth parameters
		OAuthSecrets secrets = new OAuthSecrets().consumerSecret(apiSecret);
		OAuthParameters params = new OAuthParameters().consumerKey(apiKey).signatureMethod("HMAC-SHA1").version("1.0")
		// Create the OAuth client filter
		OAuthClientFilter filter = new OAuthClientFilter(client.getProviders(), params, secrets)
		// Add the filter to the resource
		resource.addFilter(filter)
		
		return JSON.parse(resource.get(String.class))
	}

    def sendSignedOAuth(String method, ArrayList args, SmOAuthToken oAuthToken){
        // Create a Jersey client
        Client client = Client.create()
        
        String modifiedEndpoint = "${endpoint}?rnd=${new Date().getTime()}"
        args?.each{ arg ->
            modifiedEndpoint += "&${arg.key}=${arg.value}"
        }
        
        // Create a resource to be used to make SmugMug API calls
        WebResource resource = client.resource(modifiedEndpoint).queryParam("method",method)
 
        // Set the OAuth parameters
        OAuthSecrets secrets = new OAuthSecrets().consumerSecret(apiSecret).tokenSecret(oAuthToken.secret)
        OAuthParameters params = new OAuthParameters().consumerKey(apiKey).token(oAuthToken.id).signatureMethod("HMAC-SHA1").version("1.0")
        // Create the OAuth client filter
        OAuthClientFilter filter = new OAuthClientFilter(client.getProviders(), params, secrets)
        // Add the filter to the resource
        resource.addFilter(filter)
        
        // sign the request
        OAuthSignature.sign(resource,params,secrets)

        return JSON.parse(resource.get(String.class))
    }

    String buildURL(String method, ArrayList args){
    	
    	if (!isConfigured()){
    		throw new RuntimeException("API credentials not supplied. Please add them to your applications Config.groovy")
    	}

    	String url = "$endpoint?method=$method&APIKey=$apiKey"
    	args?.each {arg ->
    		url += "&${arg.key}=${arg.value}"
    	}
    	return url
    }
}
