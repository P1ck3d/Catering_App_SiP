package com.example.a5150_courier.Model

class PopularCategoryModel {
    var food_id:String?=null
    var menu_id:String?=null
    var name:String?=null
    var image:String?=null

    constructor()
    constructor(food_id: String?, menu_id: String?, name: String?, image_id: String?) {
        this.food_id = food_id
        this.menu_id = menu_id
        this.name = name
        this.image = image_id
    }


}