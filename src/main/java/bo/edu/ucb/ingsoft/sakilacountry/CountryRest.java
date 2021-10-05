package bo.edu.ucb.ingsoft.sakilacountry;

import java.util.Calendar;
import java.util.List;
import java.text.SimpleDateFormat;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

@RestController
public class CountryRest {
    private final CountryRepository repository;
    private final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    CountryRest(CountryRepository repository) {
        this.repository = repository;
    }

    @GetMapping(path = "/country", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Country> listAll() {
        return (List<Country>) repository.findAll();
    }

    @GetMapping(path = "/country/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Country listOne(@PathVariable Integer id) {
        return repository.findById(id).orElseThrow(() -> new CountryException(id));
    }

    @PostMapping(path = "/country", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Country listAll(@RequestBody Country newCountry) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        newCountry.setLastUpdate(parseTimestamp(timeStamp));
        System.out.println(newCountry.getName() + " - " + newCountry.getLastUpdate());
        return repository.save(newCountry);
    }

    @PutMapping(path = "/country/{id}", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String updateCountry(@RequestBody Country newCountry, @PathVariable Integer id) {
        return repository.findById(id).map(country -> {
            country.setName(newCountry.getName());
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            country.setLastUpdate(parseTimestamp(timeStamp));
            repository.save(country);
            return "Country has been updated";
        }).orElseGet(() -> {
            return "Could not find country with id: " + id;
        });
    }

    @DeleteMapping(path = "/country/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String deleteEmployee(@PathVariable Integer id) {
        String resp = "";
        if (!repository.findById(id).isEmpty()) {
            repository.deleteById(id);
            resp = "Country has been delete";
        } else {
            resp = "Could not find country with id: " + id;
        }
        return resp;
    }

    private java.util.Date parseTimestamp(String timestamp) {
        try {
            return DATE_TIME_FORMAT.parse(timestamp);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
