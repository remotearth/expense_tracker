package com.remotearthsolutions.expensetracker.services

interface ProgressService {
    fun showProgressBar(message: String?)
    fun hideProgressBar()
}