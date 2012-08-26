package com.smugmug.core

abstract class SmOAuthToken {

    static final String TYPE_REQUEST = "requestToken"
    static final String TYPE_ACCESS = "accessToken"

    String id
    String type
    String secret
    String access
    String permissions

    static constraints = {
        id blank:false, nullable:false, unique:true
        type blank:false, nullable:false, default:SmOAuthToken.TYPE_REQUEST
        secret blank:false, nullable:false
        access nullable:true, blank:false
        permissions nullable:true, blank:false
    }

    static mapping = {
        id generator:'assigned'
    }
}
