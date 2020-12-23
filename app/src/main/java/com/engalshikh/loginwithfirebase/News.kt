package com.engalshikh.loginwithfirebase

data class News (

    var title:String="",
    var det:String="",
    var image:String=""
){

    companion object {
        fun newsJSON(id: String, newsMap: MutableMap<String, Any>): News {

            return News(

                title = newsMap["title"] as String,
                det = newsMap["det"] as String,
                image = newsMap["image"] as  String
            )
        }
    }
}