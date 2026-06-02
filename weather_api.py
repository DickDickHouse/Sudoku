import requests
from typing import Dict, Optional, List
from dataclasses import dataclass
from datetime import datetime
import os
from dotenv import load_dotenv

load_dotenv()

@dataclass
class WeatherData:
    """Data class for weather information"""
    location: str
    temperature: float
    feels_like: float
    humidity: int
    pressure: int
    description: str
    wind_speed: float
    wind_direction: int
    cloudiness: int
    visibility: int
    sunrise: str
    sunset: str
    uv_index: Optional[float] = None
    rain: Optional[float] = None
    snow: Optional[float] = None
    timestamp: str = None

    def __post_init__(self):
        if self.timestamp is None:
            self.timestamp = datetime.now().isoformat()

class WeatherAPI:
    """
    Weather API client using OpenWeatherMap API
    Requires: OPENWEATHER_API_KEY environment variable
    """
    
    BASE_URL = "https://api.openweathermap.org/data/2.5"
    FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast"
    ONE_CALL_URL = "https://api.openweathermap.org/data/3.0/onecall"
    
    def __init__(self, api_key: Optional[str] = None):
        """
        Initialize WeatherAPI client
        
        Args:
            api_key: OpenWeatherMap API key. If not provided, reads from OPENWEATHER_API_KEY env var
        """
        self.api_key = api_key or os.getenv("OPENWEATHER_API_KEY")
        if not self.api_key:
            raise ValueError(
                "API key not found. Please set OPENWEATHER_API_KEY environment variable "
                "or pass it as parameter. Get your free key at: https://openweathermap.org/api"
            )
    
    def get_current_weather(self, city: str, units: str = "metric") -> WeatherData:
        """
        Fetch current weather for a city
        
        Args:
            city: City name (e.g., "London", "New York")
            units: Temperature units - 'metric' (Celsius), 'imperial' (Fahrenheit), 'standard' (Kelvin)
        
        Returns:
            WeatherData object with current weather information
            
        Raises:
            requests.exceptions.RequestException: If API request fails
            ValueError: If city not found
        """
        endpoint = f"{self.BASE_URL}/weather"
        params = {
            "q": city,
            "appid": self.api_key,
            "units": units
        }
        
        try:
            response = requests.get(endpoint, params=params, timeout=10)
            response.raise_for_status()
            data = response.json()
            
            if response.status_code == 200:
                return self._parse_current_weather(data, units)
            else:
                raise ValueError(f"City '{city}' not found")
                
        except requests.exceptions.Timeout:
            raise requests.exceptions.RequestException("Request timed out. Please try again.")
        except requests.exceptions.ConnectionError:
            raise requests.exceptions.RequestException("Connection error. Please check your internet connection.")
    
    def get_weather_by_coordinates(
        self, 
        latitude: float, 
        longitude: float, 
        units: str = "metric"
    ) -> WeatherData:
        """
        Fetch current weather by geographic coordinates
        
        Args:
            latitude: Latitude coordinate
            longitude: Longitude coordinate
            units: Temperature units
            
        Returns:
            WeatherData object
        """
        endpoint = f"{self.BASE_URL}/weather"
        params = {
            "lat": latitude,
            "lon": longitude,
            "appid": self.api_key,
            "units": units
        }
        
        response = requests.get(endpoint, params=params, timeout=10)
        response.raise_for_status()
        data = response.json()
        
        return self._parse_current_weather(data, units)
    
    def get_forecast(self, city: str, units: str = "metric", days: int = 5) -> List[Dict]:
        """
        Fetch weather forecast (5-day forecast with 3-hour intervals)
        
        Args:
            city: City name
            units: Temperature units
            days: Number of days (default 5, max 5 for free tier)
            
        Returns:
            List of forecast data dictionaries
        """
        endpoint = self.FORECAST_URL
        params = {
            "q": city,
            "appid": self.api_key,
            "units": units,
            "cnt": days * 8  # 8 x 3-hour intervals per day
        }
        
        response = requests.get(endpoint, params=params, timeout=10)
        response.raise_for_status()
        data = response.json()
        
        forecasts = []
        for item in data.get("list", []):
            forecast = {
                "datetime": item.get("dt_txt"),
                "temperature": item.get("main", {}).get("temp"),
                "feels_like": item.get("main", {}).get("feels_like"),
                "humidity": item.get("main", {}).get("humidity"),
                "pressure": item.get("main", {}).get("pressure"),
                "description": item.get("weather", [{}])[0].get("description", ""),
                "icon": item.get("weather", [{}])[0].get("icon", ""),
                "wind_speed": item.get("wind", {}).get("speed"),
                "rain": item.get("rain", {}).get("3h", 0),
                "cloudiness": item.get("clouds", {}).get("all")
            }
            forecasts.append(forecast)
        
        return forecasts
    
    def get_air_quality(self, latitude: float, longitude: float) -> Dict:
        """
        Fetch air quality data
        
        Args:
            latitude: Latitude coordinate
            longitude: Longitude coordinate
            
        Returns:
            Air quality data dictionary
        """
        endpoint = "https://api.openweathermap.org/data/3.0/stations"
        # Note: Air quality requires different endpoint
        # This is a simplified example
        return {"note": "Air quality requires separate API call"}
    
    def _parse_current_weather(self, data: Dict, units: str) -> WeatherData:
        """Parse current weather API response"""
        main_data = data.get("main", {})
        weather_data = data.get("weather", [{}])[0]
        wind_data = data.get("wind", {})
        sys_data = data.get("sys", {})
        clouds_data = data.get("clouds", {})
        
        temp_symbol = "°C" if units == "metric" else "°F" if units == "imperial" else "K"
        
        return WeatherData(
            location=f"{data.get('name')}, {data.get('sys', {}).get('country', '')}",
            temperature=main_data.get("temp", 0),
            feels_like=main_data.get("feels_like", 0),
            humidity=main_data.get("humidity", 0),
            pressure=main_data.get("pressure", 0),
            description=weather_data.get("main", ""),
            wind_speed=wind_data.get("speed", 0),
            wind_direction=wind_data.get("deg", 0),
            cloudiness=clouds_data.get("all", 0),
            visibility=data.get("visibility", 0),
            sunrise=self._format_timestamp(sys_data.get("sunrise")),
            sunset=self._format_timestamp(sys_data.get("sunset")),
            rain=data.get("rain", {}).get("1h"),
            snow=data.get("snow", {}).get("1h")
        )
    
    @staticmethod
    def _format_timestamp(timestamp: Optional[int]) -> str:
        """Convert Unix timestamp to readable format"""
        if timestamp:
            return datetime.fromtimestamp(timestamp).strftime("%H:%M:%S")
        return "N/A"
    
    @staticmethod
    def get_weather_icon_emoji(description: str) -> str:
        """Get emoji representation of weather condition"""
        description = description.lower()
        emoji_map = {
            "clear": "☀️",
            "clouds": "☁️",
            "rain": "🌧️",
            "drizzle": "🌦️",
            "thunderstorm": "⛈️",
            "snow": "❄️",
            "mist": "🌫️",
            "smoke": "💨",
            "haze": "🌫️",
            "dust": "🌪️",
            "fog": "🌫️",
            "sand": "🌪️",
            "ash": "💨",
            "squall": "💨",
            "tornado": "🌪️"
        }
        
        for key, emoji in emoji_map.items():
            if key in description:
                return emoji
        return "🌤️"
