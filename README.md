# AttendenceTaker
An Android app to make attendance taking job easy and fun.You just have to swipe to take attendace and all your attendance will be store in firebase database
Dependencies
    implementation 'androidx.appcompat:appcompat:1.5.1'
    implementation 'com.google.android.material:material:1.6.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'com.firebaseui:firebase-ui-auth:7.2.0'
    implementation platform('com.google.firebase:firebase-bom:31.0.1')
    implementation 'com.google.firebase:firebase-analytics'
    implementation 'com.google.firebase:firebase-auth:21.1.0'
    implementation 'com.google.firebase:firebase-firestore:24.4.0'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.3.1'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1'
    implementation 'androidx.navigation:navigation-fragment:2.5.0'
    implementation 'androidx.navigation:navigation-ui:2.5.0'
    implementation 'com.github.aaronbond:Swipe-Deck:0.1.0'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
Add maven to the repsitories of both dependencyResolutionManagement and PluginManagement inside setting.gradle file
maven { url 'https://jitpack.io' }
