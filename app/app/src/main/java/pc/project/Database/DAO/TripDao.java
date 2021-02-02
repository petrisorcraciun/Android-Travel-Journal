package pc.project.Database.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pc.project.Database.Models.Trip;

@Dao
public interface TripDao {
    @Query("SELECT * from trips where isFav = 1")
    List<Trip> getFav();

    @Query("SELECT * FROM trips order by createdTime desc")
    List<Trip> getAllTrips();

    @Query("DELETE FROM trips")
    void deleteAllTrips();

    @Insert
    void insert(Trip trip);
    @Update
    void update(Trip trip);
    @Delete
    void delete(Trip trip);

    @Query("update trips set isFav = :value where id = :tripID")
    void changeFav(long tripID, boolean value);

    @Query("delete from trips where id = :tripID")
    void deleteTrip(long tripID);


}
