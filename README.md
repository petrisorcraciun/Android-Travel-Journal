## Final Project - Travel Journal

## About 
    Final project for Android Fundamentals course - Google Digital Workshop for Programmers.

## Demo 📱

<p align="center">
<img  src = "https://raw.githubusercontent.com/petrisorcraciun/Android-Travel-Journal/main/screenshots/1.png"  width="250"  height="450"> <img  src = "https://raw.githubusercontent.com/petrisorcraciun/Android-Travel-Journal/main/screenshots/2.png"  width="250"  height="450"> <img  src = "https://raw.githubusercontent.com/petrisorcraciun/Android-Travel-Journal/main/screenshots/3.png"  width="250"  height="450"> <img  src = "https://raw.githubusercontent.com/petrisorcraciun/Android-Travel-Journal/main/screenshots/4.png"  width="250"  height="450"> <img  src = "https://raw.githubusercontent.com/petrisorcraciun/Android-Travel-Journal/main/screenshots/5.png"  width="250"  height="450"> <img  src = "https://raw.githubusercontent.com/petrisorcraciun/Android-Travel-Journal/main/screenshots/6.png"  width="250"  height="450"> <img  src = "https://raw.githubusercontent.com/petrisorcraciun/Android-Travel-Journal/main/screenshots/7.png"  width="250"  height="450"> <img  src = "https://raw.githubusercontent.com/petrisorcraciun/Android-Travel-Journal/main/screenshots/8.png"  width="250"  height="450">
</p>

## Requirments 📝

* Splashscreen with the icon and the name of the app
* Main screen
  - It is a navigation drawer activity
  - The menu on the left contains the next items:
      - Full name of the user (on the top area)
      - Email address of the user (on the top area)
      - Home (item)
      - About Us (item)
      - Contact (item)
      - Share (item)
  - The main fragment of this activity will be the list of trips. This list will be displayed when the home item will be clicked
  - Each trip could be added in the favourite list
  - If the user press long click on an item, then the edit screen of the selected trip will be displayed
* Trip list will be a RecylerView and each item will contain the next fields:
  - Picture
  - Trip Name
  - Destination
  - Price / Rating
  - Bookmark button to mark the trip as favourite
* Add/Edit trip
  - The add action will be triggered by the FAB button from the trips list
  - The edit action will be triggered when the user will long click on the item from the trips list
  - This activity will contain the next fields:
      - Trip Name (input)
      - Destination (input)
      - Trip Type with 3 possible options (single choice - radio group)
        - City Break
        - Sea Side
        - Mountains
      - Price (EUR) (slider)
      - Start Date (date picker)
      - End Date (date picker)
      - Rating (rating bar)
      - Gallery Photo (select a photo from gallery) [optional]
      - Take Photo (take a picture using a camera) [optional]
      - Save button
- Save all the data from your app in the local database, recommended using Room library.
- Create a new screen that will display the details of the trip in read only mode. You will be the designer of that screen, so be creative! :) The details screen will be displayed when the user will click on an item from the trips list. Also display in this screen the weather from that location using a public api like https://openweathermao.org/current
