package pc.project.Activities.Trip;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.Calendar;

import pc.project.Adaptor.TripAdaptor;
import pc.project.Database.AppDatabase;
import pc.project.Database.Models.Trip;
import pc.project.R;

public class UpdateTrip extends AppCompatActivity {

    private int year;
    private int month;
    private int day;

    Intent intent;
    Trip trip;
    ImageView openGallery;
    ImageView openCamera;
    ImageView selectedImage;

    Button btnStartDate;
    Button btnEndDate;
    Button saveTripButton;
    private AppDatabase appDatabase;

    EditText tripName, tripDestionation;
    RadioGroup tripType;
    SeekBar tripPrice;
    RatingBar ratingBarTrip;
    private String photoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_trip);

        intent = getIntent();
        trip = (Trip) intent.getSerializableExtra(TripAdaptor.TripHolder.TRIP_KEY);

        btnStartDate        = findViewById(R.id.btnStartDate);
        btnEndDate          = findViewById(R.id.btnEndDate);
        openGallery         = findViewById(R.id.openGallery);
        openCamera          = findViewById(R.id.openCamera);
        selectedImage       = findViewById(R.id.selectedImage);
        saveTripButton      = findViewById(R.id.save_trip_button);
        tripName            = findViewById(R.id.tripName);
        tripDestionation    = findViewById(R.id.tripDestionation);
        tripType            = findViewById(R.id.tripType);
        tripPrice           = findViewById(R.id.tripPrice);
        ratingBarTrip       = findViewById(R.id.ratingBarTrip);

        tripName.setText(trip.getName());
        tripDestionation.setText(trip.getDestination());
        ratingBarTrip.setRating(trip.getRating());
        if(trip.getType().contains("City")){
            tripType.check(R.id.city_break_radioButton);
        } else if(trip.getType().contains("Seaside")){
            tripType.check(R.id.seaside_radioButton);
        } else {
            tripType.check(R.id.mountains_radioButton);
        }
        btnStartDate.setText(trip.getStart_date());
        btnEndDate.setText(trip.getEnd_date());
        tripPrice.setProgress(trip.getPrice());

        photoPath = trip.getImage();

        if(trip.getImage() != null )
        {
            if(trip.getImage().length() > 0)
            {
                File imageFile = new File(trip.getImage());
                if(imageFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(trip.getImage());
                    selectedImage.setImageBitmap(myBitmap);
                    selectedImage.setClipToOutline(true);
                }
            }
        }


        saveTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appDatabase = AppDatabase.createDatabase(getApplicationContext());

                int selectedIdRadioGroup = tripType.getCheckedRadioButtonId();
                RadioButton radioButtonSelectedOption = (RadioButton) findViewById(selectedIdRadioGroup);
                String radioButtonToStringTripType = (String) radioButtonSelectedOption.getText();

                Trip tripUpdate = new Trip(trip.getId(),tripName.getText().toString().trim(),
                        tripDestionation.getText().toString().trim(),
                        radioButtonToStringTripType,
                        tripPrice.getProgress(),
                        (int)Math.round(ratingBarTrip.getRating()), photoPath, btnStartDate.getText().toString().trim(),
                        btnEndDate.getText().toString().trim(),
                        trip.isFav(),
                        "29/01/2021");

                appDatabase.tripDao().update(tripUpdate);

                Snackbar.make(v, "The changes have been saved successfully.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });


    }
    public void onClickPickStartDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.getTime();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String monthS = (month + 1) < 10 ? "0" + (month+1) : "" + (month + 1);
                String dayS = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
                btnStartDate.setText(dayS + "-" + monthS + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void onClickPickEndDate(View view) {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.getTime();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String monthS = (month + 1) < 10 ? "0" + (month+1) : "" + (month + 1);
                String dayS = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
                btnEndDate.setText(dayS + "-" + monthS + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
    public void btnSelectPhotoOnClick(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");
            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");
            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
            startActivityForResult(chooserIntent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if(resultCode == Activity.RESULT_OK) {
                Uri selectedImageURI = data.getData();
                selectedImage.setImageURI(selectedImageURI);
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImageURI, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                photoPath = cursor.getString(columnIndex);
                cursor.close();
            }
        }


    }


}