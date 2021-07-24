package com.toprest.firebaselib.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.core.module.Module
import org.koin.dsl.module

private const val DATABASE_URL = "https://toprest-4c560-default-rtdb.europe-west1.firebasedatabase.app"

fun firebaseLibModule() : Module = module {

    single { Firebase.auth }

    single { FirebaseDatabase.getInstance(DATABASE_URL) }

    single { get<FirebaseDatabase>().reference }
}
