package com.tunha

class DrugPrescription {
    private var name: String = ""
    private var dosage: String = ""
    private var times: String = ""
    private var total: Int = 0

    constructor()

    constructor(name: String, dosage: String, times: String, total: Int) {
        this.name = name
        this.dosage = dosage
        this.times = times
        this.total = total
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getDosage(): String {
        return dosage
    }

    fun setDosage(dosage: String) {
        this.dosage = dosage
    }

    fun getTimes(): String {
        return times
    }

    fun setTimes(times: String) {
        this.times = times
    }

    fun getTotal(): Int {
        return total
    }

    fun setTotal(total: Int) {
        this.total = total
    }
}
