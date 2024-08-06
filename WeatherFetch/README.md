# WeatherFetch

The WeatherFetch class is a simple utility to fetch and parse weather data from the website wttr.in. It uses the Java HttpClient to make HTTP requests and retrieve the weather information in a specific format. The class also includes a method to map weather conditions to corresponding image icons.

## Features

Fetches weather data for a given city.
Parses the fetched HTML to extract temperature and weather conditions.
Provides a method to get a visual representation (image icon) for the weather condition.

## Usage

Fetching Weather Data

The main method fetches the weather data for a city provided as a command-line argument. It constructs a URL to wttr.in with the specified city and format, retrieves the HTML content, and parses it to extract the temperature and condition.