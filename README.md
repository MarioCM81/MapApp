
# MAPASEADOR **Aplicación de Navegación Móvil con Geolocalización**

## **Descripción**

Esta es una aplicación móvil desarrollada para **Android** que permite a los usuarios trazar rutas y escuchar audios asociados a localizaciones utilizando **GPS** en tiempo real. 
Utiliza **Room** para persistir datos localmente en un dispositivo, lo que permite el acceso a la información incluso sin conexión a Internet. 
La aplicación está diseñada para ser intuitiva y fácil de usar, proporcionando una experiencia de navegación fluida.

## **Tecnologías Usadas**

- **Android Studio** (Java y Kotlin)
- **Room** para persistencia de datos (SQLite)
- **Geolocalización y GPS** en tiempo real
- **API de mapas de Google** (si se ha utilizado alguna)
- **UI/UX**: Diseño orientado a la experiencia de usuario

## **Características**

- **Navegación GPS**: Obtén direcciones precisas y en tiempo real.
- **Persistencia de datos**: Guarda puntos de longitud/latitud recorridos por el usuario utilizando **Room**.
- **Modo Offline**: Permite acceder a los datos almacenados en el dispositivo.
- **Pruebas en Dispositivo Real**: Verificado sobre un smartphone real para asegurar la precisión del GPS.

## **Instrucciones de Instalación**

1. Clona este repositorio a tu máquina local:
   ```bash
   git clone https://github.com/MarioCM81/MapApp
   ```

2. Abre el proyecto en **Android Studio**.

3. Asegúrate de tener el **SDK de Android** instalado y configura el entorno de desarrollo si es necesario.

4. Conecta tu dispositivo Android o utiliza un **emulador** para probar la aplicación.

5. Ejecuta la aplicación en Android Studio.

6. No olvides utilizar tu propia API Key de Google Maps para hacer tus pruebas, modificando el AndroidManifest.xml

## **Uso**

1. Al abrir la aplicación, el usuario comienza en su ubicación de incio, marcada por el GPS del dispositivo.
2. Luego, se puede trazar una ruta hasta un destino utilizando el GPS en tiempo real.
3. Los puntos de ruta y puntos de interés se guardan localmente, lo que permite acceso sin conexión.

Este proyecto está licenciado bajo la licencia **MIT**.

---

Este README proporciona una visión general clara y sencilla para que cualquier persona que vea tu repositorio entienda rápidamente de qué trata el proyecto, cómo instalarlo, cómo usarlo y cómo contribuir. Si quieres agregar más detalles o tienes alguna pregunta específica, ¡avísame!
