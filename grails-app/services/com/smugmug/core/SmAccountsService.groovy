package com.smugmug.core

class SmAccountsService {

	def smCoreService

	// returns a URL that redirects to a user's custom or default smugmug URL
    def browse(String nickName) {
    	return smCoreService.buildURL("smugmug.accounts.browse",[[key:"NickName",value:nickName]])
    }
}
