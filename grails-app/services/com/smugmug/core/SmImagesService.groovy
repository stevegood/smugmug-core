package com.smugmug.core

class SmImagesService {

	def smCoreService

    def getImageInfo(imageId, imageKey) {
    	def method = "smugmug.images.getInfo"
    	def args = [
    		[key:"ImageID",value:imageId],
    		[key:"ImageKey",value:imageKey]
    	]
    	
    	return smCoreService.send(method, args)
    }
}
