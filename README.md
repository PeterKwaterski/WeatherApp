WeatherApp Explaination:

For reasons of privacy and security, the OpenWeather API key was not included as part of the code and is not hardcoded into anything.
The code handles getting this API Key by retrieving it from the System Envionment Variables in a process that I believe is standard and a common practice.
See section below for setting up your key.

TO GET AND CONFIGURE YOUR API KEY: 
Go to openweathermap.org
login in or create an account
Click on your username and from the dropdown select API Keys
One should appear by default but a new one can also be created
It may take a while for the keys to become active so if the app initially fails wait a while and try again
Once you have your key, go to environment variables, under system select new.
Name the variable OPEN_WEATHER_KEY and copy and paste your key into the value.
Select ok on all the windows
Go to the folder with the jar and open a command prompt there
enter java -jar DSWeatherKata.jar or whatever you named it
If it fails you may need to restart to update the variables

To Use the App:
Once the app is running, enter the zip code of the city you want to see the weather for
In the other search bar enter the ISO 2-Digit country code for the country that the zip code is in
The country codes are for example "US" for the USA "JP" for Japan etc. 
Hitting enter in either box or search will retrieve the data of the current weather at that location.
