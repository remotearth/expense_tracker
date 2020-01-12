package com.remotearthsolutions.expensetracker.utils


data class Response(
    var code: Int? = null,
    var message: String? = null
){
    companion object {
        var SUCCESS = 0
        var FAILURE = 1
    }

}