# Aplicativo Móvil de E-Commerce
¡Bienvenido a Mac, la experiencia de compra de bisutería más elegante y exclusiva! Este README te guiará a través de la configuración y dependencias esenciales de nuestra aplicación móvil. Sigue los pasos a continuación para integrar perfectamente tu aplicación con nuestro servicio de e-commerce y disfrutar de las notificaciones de novedades y ofertas a través de Firebase

## Configuración del Proyecto
### Versiones
* SDK: 33
* Gradle: 7.0.0

## Dependencias Principales
### AndroidX:
* androidx.appcompat:appcompat:1.3.0
* com.google.android.material:material:1.3.0
* androidx.constraintlayout:constraintlayout:2.0.4
* androidx.lifecycle:lifecycle-livedata-ktx:2.3.1
* androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1
* androidx.navigation:navigation-fragment:2.3.5
* androidx.navigation:navigation-ui:2.3.5

### Retrofit:
* com.squareup.retrofit2:retrofit:2.9.0
* com.squareup.retrofit2:converter-gson:2.9.0
* com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2

### Dagger:
* com.google.dagger:dagger-android:2.35.1
* com.google.dagger:dagger-android-processor:2.33

### Picasso:
* com.squareup.picasso:picasso:2.71828

### Stetho:

* com.facebook.stetho:stetho:1.5.1
* com.facebook.stetho:stetho-okhttp3:1.5.1

### Librería Spinner: 
* com.github.ganfra:material-spinner:2.0.0

### Java Mail:
* com.sun.mail:android-mail:1.6.6
* com.sun.mail:android-activation:1.6.6

### Google Maps:
* com.google.android.gms:play-services-maps:17.0.1
* com.google.android.gms:play-services-places:17.0.0

### Sweet Alert: 
* com.github.f0ris.sweetalert:library:1.6.2

### ImageView Circle: 
* de.hdodenhof:circleimageview:3.1.0

### Carrusel: 
* com.github.smarteist:autoimageslider:1.4.0

### Glide: 
* com.github.bumptech.glide:glide:4.11.0

### PDF Viewer:
* com.github.barteksc:android-pdf-viewer:2.8.2

## Integración con Firebase
* Firebase BOM Version: 32.4.0

Asegúrate de tener las siguientes dependencias de Firebase:

* Firebase Analytics: com.google.firebase:firebase-analytics
* Firebase Messaging: com.google.firebase:firebase-messaging:23.0.0

## Conexión con el Servicio de E-Commerce
Para conectarte con el servicio de E-Commerce desarrollado en Spring Boot, se utiliza la librería Retrofit. Asegúrate de configurar correctamente las URLs y los endpoints en tu código.
[Descargar.E-CommerceService.](https://github.com/Alexander-FM/E-CommerceService.git)

## Configuración de Notificaciones con Firebase
Asegúrate de haber configurado correctamente Firebase Messaging para recibir notificaciones del servicio de E-Commerce.

## Información Adicional
Para más detalles sobre la configuración específica de tu proyecto, revisa los archivos build.gradle en la raíz del proyecto y en la carpeta app.

## 🚀 Contacto
Hola, y mi nombre es Alexander! 👋 I'm a full stack developer...
Si necesitas ayuda adicional, no dudes en contactarme alexanderfuentes199912@gmail.com.

# Contribución
Si deseas contribuir a este proyecto, por favor sigue los siguientes pasos:

1. Crea un fork del repositorio.
2. Crea una rama para tu nueva funcionalidad: git checkout -b nueva-funcionalidad.
3. Haz tus cambios y realiza commit: git commit -m 'Añade nueva funcionalidad'.
4. Haz push a tu rama: git push origin nueva-funcionalidad.
5. Crea un pull request en GitHub.

# Licencia
Este proyecto está bajo la licencia de MIT. Consulta el archivo LICENSE para más detalles

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)

# Pantallazos
![Registrar Usuario 1-2](https://acortar.link/sArgV5)
![Registrar Usuario 2-2](https://acortar.link/6klNvS)
![Inicio](https://acortar.link/w2r3sy)
![Productos por categoría](https://acortar.link/ACh4jU)
![Sidebar](https://acortar.link/TfoQXF)
![Bolsa de compras](https://acortar.link/lusiBN)
![Mis compras](https://acortar.link/W3iim6)
![Factura PDF](https://acortar.link/rnR1jK)
![Detalle compras](https://acortar.link/pnozjy)
![Login](https://acortar.link/Zyf3LM)
