package pc.project.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import pc.project.Database.DAO.TripDao;
import pc.project.Database.Models.Trip;

@Database(entities={Trip.class}, version=1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME  = "aTravelJournal";
    private static AppDatabase INSTANCE;
    public abstract TripDao tripDao();

    public static AppDatabase createDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            AppDatabase.class,
                            "android_travel98")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return  INSTANCE;
    }

//    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//            new PopulateDbAsyncTask(INSTANCE).execute();
//        }
//    };
//
//    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
//        private TripDao tripDao;
//        private PopulateDbAsyncTask(AppDatabase db) {
//            tripDao = db.tripDao();
//        }
//        @Override
//        protected Void doInBackground(Void... voids) {
////            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
////            String currentData = df.format(new Date());
//
//            tripDao.insert(new Trip(1, "Name 1", "ddfsdf", "CITY_BREAK", 100, 5, "28/01/2021", "28/01/2021", true, "28/01/2021"));
//            tripDao.insert(new Trip(2, "Name 2", "asdasdsa", "CITY_BREAK", 300,5, "28/01/2021", "28/01/2021", true ,"28/01/2021"));
//            tripDao.insert(new Trip(3, "Name 3", "asdasdsa", "CITY_BREAK", 900, 5, "28/01/2021", "28/01/2021", false, "28/01/2021"));
//            return null;
//        }
//    }



}
