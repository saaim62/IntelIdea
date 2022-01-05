package com.example.fypintelidea.core.app

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.example.fypintelidea.core.koin.getListOfModules
import com.example.fypintelidea.core.services.ApiClient
import com.example.fypintelidea.core.session.SessionManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho
import com.orm.SchemaGenerator
import com.orm.SugarApp
import com.orm.SugarContext
import com.orm.SugarDb
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class Sugar : SugarApp() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        SugarContext.init(applicationContext)

        if (!SessionManager(this@Sugar).getString(SessionManager.KEY_LOGIN_TOKEN).isNullOrEmpty()) {
            ApiClient.initialize(applicationContext)
            ApiClient.initializeConnectavoServicesWithExcludedFields(applicationContext)
        }

        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(getListOfModules())
        }

        // create table if not exists
        val schemaGenerator = SchemaGenerator(this)
        schemaGenerator.createDatabase(SugarDb(this).db)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        Fresco.initialize(this)

//        //Android Job Library
//        JobManager.create(this).addJobCreator(new DemoJobCreator());
////        JobManager.instance().getConfig().setAllowSmallerIntervalsForMarshmallow(true); //Don't use this in production

//        // SQUARE memory leakage library
//        Log.d(TAG, "onCreate: SquareLeakLibrary");
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
//        // Normal app init code...
        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build()
        )
    }

    override fun onTerminate() {
        super.onTerminate()
        SugarContext.terminate()
    }

    override fun onLowMemory() {
        Log.d(TAG, "onLowMemory: ")
        //        Toast.makeText(this, "Memory Low", Toast.LENGTH_SHORT).show();
        super.onLowMemory()
    }

    companion object {
        private const val TAG = "SugarApplicationClass"
    }
}