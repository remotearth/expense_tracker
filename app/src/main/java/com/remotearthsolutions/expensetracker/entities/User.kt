package com.remotearthsolutions.expensetracker.entities

import org.parceler.Parcel

@Parcel(Parcel.Serialization.BEAN)
class User {
    var userId = 0
    var userName: String? = null
    var authType: String? = null

    constructor() {}
    constructor(userName: String?, authType: String?) {
        this.userName = userName
        this.authType = authType
    }

}