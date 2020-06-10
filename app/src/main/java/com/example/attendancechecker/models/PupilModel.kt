package com.example.attendancechecker.models

class PupilModel(var id : Int, var Avatar: String?, var Groupname: String, var Name: String, var Surname: String, var Thirdname: String, var Hashcode: Int?, var Comedate : String?, var Leavedate : String? ) {
    constructor() : this(0, null, "", "", "", "", null, null, null)
}