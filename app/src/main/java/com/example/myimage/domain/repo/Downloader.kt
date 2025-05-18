package com.example.myimage.domain.repo

interface Downloader {

    fun downloadFile(url: String, fileName: String?)
}