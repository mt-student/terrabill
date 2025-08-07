package com.example.terrabill.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.terrabill.data.model.Request
import com.example.terrabill.data.model.RequestStatus
import com.example.terrabill.data.model.Customer
import com.example.terrabill.data.model.Job
import com.example.terrabill.data.model.JobStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

object DatabaseProvider {

    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            lateinit var instance: AppDatabase
            instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "terra_bill_db"
            )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val customerDao = instance.customerDao()
                            val requestDao = instance.requestDao()
                            val jobDao = instance.jobDao()

                            // Beispielkunden (lustige Garten-Namen, echte Adressen Köln/Bergisch Gladbach)
                            val customers = listOf(
                                Customer(
                                    UUID.randomUUID().toString(),
                                    "Rudi",
                                    "Rasenreich",
                                    "Rasenreich & Co. KG",
                                    "Aachener Straße",
                                    "123",
                                    "50674",
                                    "Köln",
                                    "02211234567"
                                ),
                                Customer(
                                    UUID.randomUUID().toString(),
                                    "Heike",
                                    "Heckenschnitt",
                                    null,
                                    "Venloer Straße",
                                    "456",
                                    "50823",
                                    "Köln",
                                    "02217654321"
                                ),
                                Customer(
                                    UUID.randomUUID().toString(),
                                    "Benno",
                                    "Beetfreund",
                                    "Beetfreund GmbH",
                                    "Zülpicher Straße",
                                    "78",
                                    "50674",
                                    "Köln",
                                    "02219988776"
                                ),
                                Customer(
                                    UUID.randomUUID().toString(),
                                    "Lotti",
                                    "Laubfrei",
                                    null,
                                    "Severinstraße",
                                    "22",
                                    "50678",
                                    "Köln",
                                    "02213344556"
                                ),
                                Customer(
                                    UUID.randomUUID().toString(),
                                    "Gero",
                                    "Gartenzwerg",
                                    "Gartenzwerg AG",
                                    "Hohenzollernring",
                                    "85",
                                    "50672",
                                    "Köln",
                                    "02215566778"
                                ),
                                Customer(
                                    UUID.randomUUID().toString(),
                                    "Paula",
                                    "Pflanzlich",
                                    null,
                                    "Neusser Straße",
                                    "210",
                                    "50733",
                                    "Köln",
                                    "02217788990"
                                ),
                                Customer(
                                    UUID.randomUUID().toString(),
                                    "Wally",
                                    "Wurzeltief",
                                    "Wurzelwerk KG",
                                    "Subbelrather Straße",
                                    "320",
                                    "50825",
                                    "Köln",
                                    "02212233445"
                                ),
                                Customer(
                                    UUID.randomUUID().toString(),
                                    "Mona",
                                    "Moosig",
                                    null,
                                    "Hansaring",
                                    "12",
                                    "50670",
                                    "Köln",
                                    "02211122334"
                                ),
                                Customer(
                                    UUID.randomUUID().toString(),
                                    "Karla",
                                    "Klee",
                                    "Kompost & Co. GbR",
                                    "Bonner Straße",
                                    "200",
                                    "50968",
                                    "Köln",
                                    "02216677889"
                                ),
                                Customer(
                                    UUID.randomUUID().toString(),
                                    "Theo",
                                    "Thujamann",
                                    null,
                                    "Deutz-Mülheimer Straße",
                                    "45",
                                    "51063",
                                    "Köln",
                                    "02219090909"
                                ),
                                Customer(
                                    UUID.randomUUID().toString(),
                                    "Frieda",
                                    "Farn",
                                    "Farn & Partner",
                                    "Paffrather Straße",
                                    "120",
                                    "51465",
                                    "Bergisch Gladbach",
                                    "02202551122"
                                ),
                                Customer(
                                    UUID.randomUUID().toString(),
                                    "Bruno",
                                    "Buchs",
                                    null,
                                    "Bensberger Straße",
                                    "88",
                                    "51429",
                                    "Bergisch Gladbach",
                                    "02204889977"
                                )
                            )
                            customers.forEach { customerDao.insert(it) }

                            // Beispielanfragen (genau 5)
                            val requests = listOf(
                                Request(
                                    UUID.randomUUID().toString(),
                                    "Rudi",
                                    "Rasenreich",
                                    "Rasenreich & Co. KG",
                                    "Aachener Straße",
                                    "123",
                                    "50674",
                                    "Köln",
                                    "02211234567",
                                    LocalDateTime.now().plusDays(2).toString(),
                                    "Rasen mähen",
                                    2,
                                    RequestStatus.OFFEN
                                ),
                                Request(
                                    UUID.randomUUID().toString(),
                                    "Heike",
                                    "Heckenschnitt",
                                    null,
                                    "Venloer Straße",
                                    "456",
                                    "50823",
                                    "Köln",
                                    "02217654321",
                                    LocalDateTime.now().plusDays(5).toString(),
                                    "Hecke formen",
                                    3,
                                    RequestStatus.OFFEN
                                ),
                                Request(
                                    UUID.randomUUID().toString(),
                                    "Frieda",
                                    "Farn",
                                    "Farn & Partner",
                                    "Paffrather Straße",
                                    "120",
                                    "51465",
                                    "Bergisch Gladbach",
                                    "02202551122",
                                    LocalDateTime.now().plusDays(7).toString(),
                                    "Beet umgraben",
                                    4,
                                    RequestStatus.OFFEN
                                ),
                                Request(
                                    UUID.randomUUID().toString(),
                                    "Karla",
                                    "Klee",
                                    "Kompost & Co. GbR",
                                    "Bonner Straße",
                                    "200",
                                    "50968",
                                    "Köln",
                                    "02216677889",
                                    LocalDateTime.now().plusDays(10).toString(),
                                    "Laub entsorgen",
                                    1,
                                    RequestStatus.OFFEN
                                ),
                                Request(
                                    UUID.randomUUID().toString(),
                                    "Bruno",
                                    "Buchs",
                                    null,
                                    "Bensberger Straße",
                                    "88",
                                    "51429",
                                    "Bergisch Gladbach",
                                    "02204889977",
                                    LocalDateTime.now().plusDays(12).toString(),
                                    "Rollrasen verlegen",
                                    5,
                                    RequestStatus.OFFEN
                                )
                            )
                            requests.forEach { requestDao.insert(it) }

                            // Beispieljobs (12+, verschiedene Zeiträume)
                            val jobs = listOf(
                                Job(
                                    UUID.randomUUID().toString(),
                                    requests[0].id,
                                    customers[0].id,
                                    25.0,
                                    LocalDateTime.now().plusHours(1).toString(),
                                    JobStatus.OFFEN
                                ),
                                Job(
                                    UUID.randomUUID().toString(),
                                    requests[0].id,
                                    customers[1].id,
                                    25.0,
                                    LocalDateTime.now().plusDays(1).toString(),
                                    JobStatus.OFFEN
                                ),
                                Job(
                                    UUID.randomUUID().toString(),
                                    requests[1].id,
                                    customers[2].id,
                                    30.0,
                                    LocalDateTime.now().plusWeeks(1).toString(),
                                    JobStatus.OFFEN
                                ),
                                Job(
                                    UUID.randomUUID().toString(),
                                    requests[2].id,
                                    customers[10].id,
                                    20.0,
                                    LocalDateTime.now().plusWeeks(2).toString(),
                                    JobStatus.OFFEN
                                ),
                                Job(
                                    UUID.randomUUID().toString(),
                                    requests[3].id,
                                    customers[8].id,
                                    28.0,
                                    LocalDateTime.now().plusWeeks(3).toString(),
                                    JobStatus.OFFEN
                                ),
                                Job(
                                    UUID.randomUUID().toString(),
                                    requests[4].id,
                                    customers[11].id,
                                    35.0,
                                    LocalDateTime.now().plusMonths(1).toString(),
                                    JobStatus.OFFEN
                                ),
                                Job(
                                    UUID.randomUUID().toString(),
                                    requests[1].id,
                                    customers[3].id,
                                    26.5,
                                    LocalDateTime.now().plusDays(3).toString(),
                                    JobStatus.OFFEN
                                ),
                                Job(
                                    UUID.randomUUID().toString(),
                                    requests[2].id,
                                    customers[4].id,
                                    22.0,
                                    LocalDateTime.now().plusDays(9).toString(),
                                    JobStatus.OFFEN
                                ),
                                Job(
                                    UUID.randomUUID().toString(),
                                    requests[3].id,
                                    customers[5].id,
                                    29.0,
                                    LocalDateTime.now().plusMonths(2).toString(),
                                    JobStatus.OFFEN
                                ),
                                Job(
                                    UUID.randomUUID().toString(),
                                    requests[0].id,
                                    customers[6].id,
                                    24.0,
                                    LocalDateTime.now().plusDays(14).toString(),
                                    JobStatus.OFFEN
                                ),
                                Job(
                                    UUID.randomUUID().toString(),
                                    requests[4].id,
                                    customers[7].id,
                                    33.0,
                                    LocalDateTime.now().plusMonths(2).plusDays(5).toString(),
                                    JobStatus.OFFEN
                                ),
                                Job(
                                    UUID.randomUUID().toString(),
                                    requests[2].id,
                                    customers[9].id,
                                    21.0,
                                    LocalDateTime.now().plusWeeks(4).toString(),
                                    JobStatus.OFFEN
                                )
                            )
                            jobs.forEach { jobDao.insert(it) }
                        }
                    }
                })
                .build()

            INSTANCE = instance
            return instance
        }
    }
}