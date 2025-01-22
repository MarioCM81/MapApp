package com.example.appmaps.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_locations")
//Esto sirve para definir un objeto que se caracteriza por id, userId, latitude, longitude, dateTime.
/*
La clase `UserLocation` es una entidad de base de datos que representa una ubicación registrada por un
usuario en una aplicación Android. Contiene campos para almacenar un identificador único (id), el
identificador del usuario (`userId`), la latitud (`latitude`), la longitud (`longitude`) y
la fecha y hora en que se registró la ubicación (`dateTime`). Esta clase se utiliza con la biblioteca
Room para guardar y recuperar datos de ubicaciones en una base de datos SQLite local.
*/

public class UserLocation {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public double latitude;
    public double longitude;
    public String dateTime;

    public UserLocation(int userId, double latitude, double longitude, String dateTime) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateTime = dateTime;
    }
}