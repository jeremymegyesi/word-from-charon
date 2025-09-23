package me.jeremymegyesi.CharonCore.externalmapping.converters;

public interface DTOConverter<T, E> {
    public T mapToInternalModel(E externalModel);
}
