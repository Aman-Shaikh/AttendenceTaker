# AttendenceTaker
An Android app to make attendance taking job easy and fun.You just have to swipe to take attendace and all your attendance will be store in firebase database.

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

Images of App

![Screenshot_20221028-160209_Attendance Manager](https://user-images.githubusercontent.com/85395708/198848103-184388de-0db8-438f-9222-362d68d29dac.jpg)
![Screenshot_20221028-160217_Attendance Manager](https://user-images.githubusercontent.com/85395708/198848108-4c79c431-949b-4f37-8401-52c95bccaff7.jpg)
![Screenshot_20221030-001146_Attendance Manager](https://user-images.githubusercontent.com/85395708/198848114-057669db-a6e0-4406-bcea-f8643dfe7966.jpg)
![Screenshot_20221030-001246_Attendance Manager](https://user-images.githubusercontent.com/85395708/198848119-92e521ff-6ece-4d17-acb9-4537b16573bd.jpg)
![Screenshot_20221030-001313_Attendance Manager](https://user-images.githubusercontent.com/85395708/198848127-b537fd54-661f-4532-b1f1-2acf9050cc09.jpg)
![Screenshot_20221030-001327_Attendance Manager](https://user-images.githubusercontent.com/85395708/198848139-26ca8855-d5d2-4654-8195-7ba8314b8f37.jpg)
![Screenshot_20221029-164145_Attendance Manager](https://user-images.githubusercontent.com/85395708/198848143-1dbc7860-ddfb-4b91-9c1b-5b7f5f298b7d.jpg)

If yeas is pressed then 3 randomly genrated nos will be provided to the professor 😎
