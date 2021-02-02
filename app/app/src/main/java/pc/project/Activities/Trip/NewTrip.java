package pc.project.Activities.Trip;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pc.project.Activities.NavigationDrawerActivity;
import pc.project.Database.AppDatabase;
import pc.project.Database.Models.Trip;
import pc.project.R;

public class NewTrip extends AppCompatActivity {

    private int year;
    private int month;
    private int day;

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
    private String photoPath = "";

    public static final int CAMERA_REQUEST_CODE = 101;
    public static final int GALLERY_REQUEST_CODE = 100;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(checkIfGivePermission())
               {
               } else
               {
                   requestPermission();
               }
            }
        });




//        openGallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
//            }
//        });

        saveTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appDatabase = AppDatabase.createDatabase(getApplicationContext());

                int selectedIdRadioGroup = tripType.getCheckedRadioButtonId();
                RadioButton radioButtonSelectedOption = (RadioButton) findViewById(selectedIdRadioGroup);
                String radioButtonToStringTripType = (String) radioButtonSelectedOption.getText();

                   Trip trip = new Trip(0, tripName.getText().toString().trim(),
                           tripDestionation.getText().toString().trim(),
                           radioButtonToStringTripType,
                           tripPrice.getProgress(),
                           (int)Math.round(ratingBarTrip.getRating()), photoPath, btnStartDate.getText().toString().trim(),
                           btnEndDate.getText().toString().trim(),
                           false,
                           "29/01/2021");

                        appDatabase.tripDao().insert(trip);

                        Intent intent = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                        startActivity(intent);
            }
        });


    }

    public void btnSelectPhotoOnClick(View view) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
        } else {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");
            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");
            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
            startActivityForResult(chooserIntent, GALLERY_REQUEST_CODE);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                Uri selectedImageURI = data.getData();
                selectedImage.setImageURI(selectedImageURI);
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImageURI, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                photoPath = cursor.getString(columnIndex);
                cursor.close();



                //selectedImage.setText(photoPath);
                //selectedImage.setImage(photoPath);
            }
        }

//        if (requestCode == GALLERY_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                Uri contentUri = data.getData();
//                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                selectedImage.setImageURI(contentUri);
//                photoPath = String.valueOf(contentUri);
//            }
//        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    private boolean checkIfGivePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        photoPath = image.getAbsolutePath();
        return image;
    }





}