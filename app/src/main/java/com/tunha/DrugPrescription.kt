package com.tunha

class DrugPrescription {
    private var name: String = ""
    private var dosage: Int = 0
    private var times: Int=0
    private var total: Int = 0

    constructor()

    constructor(name: String, dosage: Int, times: Int, total: Int) {
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

    fun getDosage(): Int {
        return dosage
    }

    fun setDosage(dosage: Int) {
        this.dosage = dosage
    }

    fun getTimes(): Int {
        return times
    }

    fun setTimes(times: Int) {
        this.times = times
    }

    fun getTotal(): Int {
        return total
    }

    fun setTotal(total: Int) {
        this.total = total
    }
}
