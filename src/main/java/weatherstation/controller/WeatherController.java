package weatherstation.controller;

import weatherstation.core.WeatherStation;
import weatherstation.core.WeatherData;
import weatherstation.core.strategy.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    @Autowired
    private WeatherStation weatherStation;

    @PostMapping("/strategy/realtime")
    public WeatherData setRealTime() {
        weatherStation.setUpdateStrategy(new RealTimeStrategy());
        weatherStation.updateWeatherData();
        return weatherStation.getCurrentData();
    }

    @PostMapping("/strategy/scheduled")
    public WeatherData setScheduled() {
        weatherStation.setUpdateStrategy(new ScheduledStrategy());
        weatherStation.updateWeatherData();
        return weatherStation.getCurrentData();
    }

    @PostMapping("/strategy/manual")
    public WeatherData setManual(@RequestBody ManualRequest request) {
        ManualStrategy strategy = new ManualStrategy();
        // Убираем передачу description - он теперь генерируется автоматически
        strategy.setManualData(request.temp, request.humidity, request.pressure, request.wind);
        weatherStation.setUpdateStrategy(strategy);
        weatherStation.updateWeatherData();
        return weatherStation.getCurrentData();
    }

    @GetMapping("/current")
    public WeatherData getCurrent() {
        return weatherStation.getCurrentData();
    }
}

class ManualRequest {
    public double temp;
    public double humidity;
    public double pressure;
    public double wind;
}