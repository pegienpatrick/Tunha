package com.tunha

import java.util.*

class Admin(email: String, fullName: String, password: String, id: String = UUID.randomUUID().toString()) : User(email, fullName, password, "Admin", id) {
}
