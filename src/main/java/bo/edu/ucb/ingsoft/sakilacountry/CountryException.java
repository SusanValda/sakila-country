package bo.edu.ucb.ingsoft.sakilacountry;

class CountryException extends RuntimeException {
    CountryException(Integer id) {
        super("Could not find country with id: " + id);
    }
}