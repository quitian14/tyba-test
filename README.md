# README #


### Que se implemento? ###

* Se uso el APi de places de google para buscar los restaurantes
* Un fallback con el api de here https://developer.here.com/documentation/geocoding-search-api/api-reference-swagger.html
* cache con redis a la llamada de las APIS
* Interface para abstraer la implementacion de las llamadas al API (org/quitian14/tyba/technicaltest/httpclients/IGetPlaces.kt)
* Los siguientes Endpoints
    * crear usuario (/api/test-ms/users)
    * login (/api/test-ms/users/login)
    * buscar restaurantes por lat/long (/api/test-ms/locations)
    * buscar transacciones por usuario (/api/test-ms/users/transactions)
* se genero token con JWT y las operaciones que necesitan identificar al usuario dicha identificacion se saca del Bearer
* para usar las operaciones primero se debe loguear y usar el token en el header de Authorization
* las configuraciones quemadas en el properties no se alcanzaron a borrar pero son API-KEY que se pueden inhabilitar y ademas se estan recibiendo por ENVIROMENTS