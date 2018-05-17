# Movie Finder

## Written by Mário Augusto Mota Martins 

## Objectives

● Blockers:
> Select a specific movie to see details (name, poster image, backdrop image, genre, overview and release date). 

> Scroll through the list of upcoming movies - including movie name, poster image, genre and release date. List should not be limited to show only the first page as returned by the API. 

> Logic is out of Views, currently on Controller class, which is the instance of the Application, working as a Façade and being a Singleton;

> SplashScreen was removed, the Genres are now requested at the opening of the Main activity - that changed the name to MovieListActivity - and stored in a local Realm database;

● Optionals:
> Network Layer Maintainability

> BaseActivity was removed, the api instance is now on Controller.

> Search for movies by entering a partial or full movie name. 

> Orientation doesn't reload the page on a change, anymore.

## Build Details

> Target Android Version: 8.1 Oreo

> Minimum recommended Version: 4.4 KitKat

## Used platform

> Android Studio 3.1.2

## Main used - frameworks

> Retrofit API v2.3.0 - API to handle REST requests

> Moshi Converter v2.3.0 - Json framework for Android and Java, used to deserialize objects returned by API in Json.

> Realm Plugin v5.0.1 - Plugin to work with SQL databases in a very abstract way, without the need of writting SQL queries.

## Get Started
> Run the code through the Andrid Studio 3.1.2 in an emulator or debug using a device.

## Details

> The Strings from TmdbApi.class were stored on a xml file and the API_KEY is Distributed on string_array to make it harder to reassemble in case of a reverse engineering on the .apk file

> The Movie Search is being done by an url that was found at the TmdbAPI documentation page.

> Problem with loss of Internet connection is reported to user.

> Next page is requested to API once the user gets to the end of the list.

> Query requests made using search box runs for every character added or removed from the box, if the user delete all the characters the hint reappears and the upcoming movies are requested again.

> To avoid problems with results from last requests bein shown on the list, it's generated a requestKey for every request, the requestKey is a random string, with a length of 10 unites. When the response of the request gets in the system the requestKey is checked to see if it is the last request made, if it is, the data are added to the list, if it is not, the data are discarted.

## API routes
>> doc: 
>> https://developers.themoviedb.org/3

> Genres
>> url:
>> https://api.themoviedb.org/3/genre/movie/list?api_key={api_key}&language={language}

> Upcoming Movies
>> url:
>> https://api.themoviedb.org/3/movie/upcoming?api_key={api_key}&language={language}&page={page}&region={region}

> Images
>> poster url:
>> https://image.tmdb.org/t/p/w154/{poster_path}?api_key={api_key}

>> backdrop url:
>> https://image.tmdb.org/t/p/w154/{backdrop_path}?api_key={api_key}

> Movie search by query
>> url:
>> https://api.themoviedb.org/3/search/movies?api_key={api_key}&language={language}&query={query}&page={page}&region={region}
