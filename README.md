# nuts

My very intent was to create a webapp which use the public GTFS data files to represent stops and departure times of the Budapest Transport Center(BKK) system. Also I use this project to learn some important framework and improve my programming skills.

The application actually has two main function:
  - Search by coordinates, location name, user click in a specified radius.
  - Show actual departure times in each stop for each transport vehicle in a specified radius.

The data come from the official website: https://bkk.hu/apps/gtfs/ and the downloader service can download automatically the archive, this service is bind on a thread which check the new versions.
This version supports only the Budapest public transport GTFS, see more in features.

important:
  - check config.properties, this contains the utility params for the app, in order to run properly please check before start.
  - stop_times file is huge(sometimes can contains 5m row), which means the inserts can take a lot of time, so don't be surpised.
  - the gtfs-dataset process can be take 1 hour or more.
  - in order to run the project you need your own google api key, for dev, is free, here you can get one: https://developers.google.com/maps/documentation/javascript/get-api-key
  - Because this version download archive from BKK's official website, this might be usefull, if you have truble with certificate:
  "%JAVA_HOME%\bin\keytool" -import -trustcacerts -file "<your_certi's_location>" -alias tomcat -keystore "%JAVA_HOME%\jre\lib\security\cacerts"

requirements:
  - JDK8
  - Apache tomcat 8.5
  - PostgreSQL 9.6
  

Further development, features:
  - Integrate more GTFS archive in order to increase the service coverage.
  - More analitic function, like service overlap, statictics about routes stops etc.
  - Mobile version
  - Tests, tests, tests
