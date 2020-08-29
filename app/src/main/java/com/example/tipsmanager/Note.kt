package com.example.tipsmanager

import java.util.*

class Note {

    var id: String? = null
    var name:String? = null
    var sales:Double? = null
    var tip:Double? = null
    var timestamp:String? = null
    //MutableMap<String, String>)
    constructor(){
    }

    constructor(id:String, name:String, sales:Double, tip:Double, timestamp:String){
        this.id = id
        this.name = name
        this.sales = sales
        this.tip = tip
        this.timestamp = timestamp
    }

}