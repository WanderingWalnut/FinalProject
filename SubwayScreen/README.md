# FinalProject


Run the following command to compile all the files and all dependencies:

javac -cp lib/sqlite-jdbc-3.34.0.jar:lib/okhttp-4.9.3.jar:lib/gson-2.8.8.jar:lib/kotlin-stdlib-1.5.31.jar:lib/okio-2.10.0.jar:lib/WeatherFetch-1.0-SNAPSHOT.jar -d target/classes \
src/main/java/ca/ucalgary/edu/ensf380/Ad.java \
src/main/java/ca/ucalgary/edu/ensf380/AdvertisementDisplay.java \
src/main/java/ca/ucalgary/edu/ensf380/CurrentTimePanel.java \
src/main/java/ca/ucalgary/edu/ensf380/DatabaseManager.java \
src/main/java/ca/ucalgary/edu/ensf380/MainApp.java \
src/main/java/ca/ucalgary/edu/ensf380/NewsFetcher.java \
src/main/java/ca/ucalgary/edu/ensf380/Station.java \
src/main/java/ca/ucalgary/edu/ensf380/TrainMap.java \
src/main/java/ca/ucalgary/edu/ensf380/TrainTracker.java

Run the MainApp file with all dependencies:

java -cp lib/sqlite-jdbc-3.34.0.jar:lib/okhttp-4.9.3.jar:lib/gson-2.8.8.jar:lib/kotlin-stdlib-1.5.31.jar:lib/okio-2.10.0.jar:lib/WeatherFetch-1.0-SNAPSHOT.jar:target/classes ca.ucalgary.edu.ensf380.MainApp 10


