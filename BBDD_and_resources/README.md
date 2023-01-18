# BBDD 
Los siguientes archivos JSON son ejemplos del posible contenido en la base de datos para la aplicación. 

## Colecciones usadas

### Escapes 
Esta colección contiene la información necesaria para generar un juego de escape room. Se debe inicializar en la propia base de datos y se carga durante la ejecución de la aplicación.

 - Colección: escapes
 - Documento: identificadores del juego: er1, er2, er3, etc.
 - Valores: se describen a continuación

    ```
    "id": Identificador del juego (erX)
    "name": Nombre del juego
    "info": Descripción del juego
    "image": Imagen, almacenada en Firestore 
    "enabled": Si está habilitado o no para el usuario
    "city": Ciudad del escape room
    "location": Zona de inicio en coordenadas
    "escape_duration": Duración en segundos
    "difficulty": Información de la dificultad
    "initial_item": Identificador del objeto inicial que recibe el usuario
    "achievements": Un diccionario de identificadores con valores relativos a los logros (aX), compuesto de:
        "ac_id": Identificador de logro
        "ac_active": Boolean, si se ha activado para el usuario el logro
        "ac_name": Nombre 
        "ac_image": Imagen 
        "ac_description": Descripción mostrada tras obtener el logro
    "items":  Un diccionario de identificadores con valores relativos a los objetos del juego (iX), compuesto de:
        "i_id": Identificador
        "i_name": Nombre
        "i_img": Imagen
        "i_found": Si el item se ha encontrado
        "i_used": Si el item ha sido usado
        "i_description": Descripción, mostrada al usuario después de encontrarlo
    "zones": Un diccionario de identificadores con valores relativos a las zonas del juego (zX), compuesto de:
        "p_id": Identificador 
        "p_name": Nombre
        "p_coord": Geolocalización
        "p_description": Descripción, orientada para el diseñador del juego
        "p_visited": Boolean, si la zona ha sido visitada 
    "trials":  Un diccionario de identificadores con valores relativos a las pruebas (tX), compuesto de:
        "t_id": Identificador
        "t_name": Nombre de la prueba, para mostrar al usuario
        "t_description": Descripción mostrada al usuario
        "t_finished": Booleano, si está terminada
        "t_resource": Recurso, pensado para contener direcciones a imagenes, audios o vídeos a mostrar en la prueba
        "t_solution": La solución a la prueba
        "t_solution_tip": Pista sobre que debe escribir el usuario
        "t_points": Puntos conseguidos
        "t_totalPoints": Puntos totales de la prueba
        "t_id_zone": Identificador de la zona de la prueba (zX)
        "t_id_item_found": Identificador del objeto obtenido al finalizar la prueba (iX)
        "t_id_item_used": Identificador del objeto necesario para solucionar la prueba (iX)
        "t_id_achievement": Identificador de logro conseguido tras superar la prueba (aX)
        "t_clue1": Descripción de la primera pista de la prueba 
        "t_clue1_activated": Boolean, para indicar si la pista ha sido usada
        "t_clue2": Segunda pista
        "t_clue2_activated"
        "t_clue3": Tercera pista 
        "t_clue3_activated"
    ``` 

### Ranking 
Colección donde se guardan los valores de Ranking generados por los usuarios en la app, al completar el juego.
Estos datos no son generados por el administrador de la base de datos, pero se ha provisto un ejemplo de los mismos. 

 - Colección: ranking
 - Documento: identificadores del juego: er1, er2, er3, etc.
 - Valores: son diccionarios por cada correo (codificados en URL):

    ```
    "test%2Ecorreo%40gmail%2Ecom": Correo codificado (key)
        "user_name": Nombre de usuario
        "user_points": Puntos conseguidos en la prueba
    ```
    


### Users 
Colección pareja al registro de los usuarios, para dotar de más control al administrador.

 - Colección: users
 - Documento: correos de los usuarios registrados
 - Valores: se definen a continuación:

    ```
        "email": Correo del usuario
        "image": Imagen de perfil
        "registrationDate": Fecha de registro
        "role": Rol del jugador, para todos los registrados será "player"
        "username": Nombre del usuario 
    ```

## Configuración y mantenimiento de la base de datos
Se ha utilizado la interfaz de Firebase para el mantenimiento de todos los recursos.

Adicionalmente para aquellos recursos de "Firestore Database" se ha utilizado la API REST: 
https://firebase.google.com/docs/firestore/use-rest-api

Las request GET se realizan sobre la siguiente URL:

https://firestore.googleapis.com/v1/projects/NOMBRE_DEL_PROYECTO/databases/(default)/documents/NOMBRE_DE_LA_COLECCIÓN/NOMBRE_DEL_DOCUMENTO/

Las request POST para editar usan la siguiente URL:

https://firestore.googleapis.com/v1beta1/projects/NOMBRE_DEL_PROYECTO/databases/(default)/documents/NOMBRE_DE_LA_COLECCIÓN?documentId=NOMBRE_DEL_DOCUMENTO

Y en el body se ha de editar el contenido a ser publicado, como aparece representado bajo el directorio de `firestore-database/`

Se recomienda el uso de métodos de autentificación por Token: https://firebase.google.com/docs/auth/, aunque a modo de prueba se pueden reducir los permisos necesarios de lectura y escritura en las reglas de la base de datos: 

    ```
        rules_version = '2';
        service cloud.firestore {
            match /databases/{database}/documents {
                match /{document=**} {
                    allow read, write: if true; // No recomendado para producción. En lugar de: 
                    // allow read, write: if request.auth != null;
                }
            }
        }
    ```

## Incorporación de la API de Google Maps
Se recomienda seguir los pasos definidos en la documentación de Google: 
https://developers.google.com/maps/get-started/

Avisos: 
- Se debe cambiar el plan de facturación del proyecto a uno de pago
- Hay que añadir este API mediante la interfaz de Google Cloud al proyecto 
- Se otorgará una clave única para ser añadida al proyecto en el archivo: `local.properties` con el siguiente formato: 
    - MAPS_API_KEY=`IDENTIFICADOR_DE_CLAVE`

En caso de no requerirse, se recomienda simplemente añadir al anterior archivo: 
`MAPS_API_KEY=XXX`


