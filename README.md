nuts

My very intent was to create a webapp which use the public GTFS data files to represent stops and departure times of the Budapest Transport Center(BKK) system. Also I use this project to learn some important framework and pratice my programming skills, so if you find some "wierd" solution, please don't hesitate, tell me. :)

The application actually has two main function:

Search by coordinates, location name, user click in a specified radius.
Show actual departure times in each stop for each transport vehicle in a specified radius.
The data come from the official website: https://bkk.hu/apps/gtfs/ and the downloader service can download automatically the archive, this service is bind on a thread which check the new versions.

important:

check config.properties, this contains the utility params for the app, in order to run properly please check before start.
stop_times file is huge(sometimes can contains 5m row), which means the inserts can take a lot of time, so don't be surpised.
Further development, features:

Integrate more GTFS archive in order to increase the service coverage.
Mobile version, with bootstrap.
