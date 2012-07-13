package com.smugmug.core

class SmAuthService {
	
	static final String ACCESS_PUBLIC = "Public"
	static final String ACCESS_FULL = "Full"
	static final String PERMISSIONS_READ = "Read"
	static final String PERMISSIONS_ADD = "Add"
	static final String PERMISSIONS_MODIFY = "Modify"

	def smCoreService
	
	String getAuthURLWithToken(String oAuthRequestToken,String access=SmAuthService.ACCESS_PUBLIC, String permissions=SmAuthService.PERMISSIONS_READ){
		return "https://api.smugmug.com/services/oauth/authorize.mg?oauth_token=${oAuthRequestToken}&Access=${access}&Permissions=${permissions}"
	}
	
	String getAuthURL(String access=SmAuthService.ACCESS_PUBLIC, String permissions=SmAuthService.PERMISSIONS_READ){
		return getAuthURLWithToken(requestToken.id)
	}
    
	def getRequestToken() {
		def result = smCoreService.sendOAuth("smugmug.auth.getRequestToken", [])
		return result.Auth.Token
    }

    String getAccessToken(SmOAuthToken oAuthRequestToken){
    	def args = [
	    	[key:"oauth_token",value:oAuthRequestToken.id]
	    ]
    	smCoreService.sendSignedOAuth("smugmug.auth.getAccessToken",args,oAuthRequestToken)
    }
}