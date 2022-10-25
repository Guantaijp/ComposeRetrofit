package com.example.composeretrofit

fun redirectedImgSrcUrl(url: String): String {
    return url.replace( // replace part of a string
        "http://mars.jpl.nasa.gov",
        "https://mars.nasa.gov"
    )
}