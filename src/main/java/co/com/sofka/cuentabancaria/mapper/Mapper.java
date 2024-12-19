package co.com.sofka.cuentabancaria.mapper;

@FunctionalInterface
public interface Mapper <T, R> {
    R map(T input);
}
