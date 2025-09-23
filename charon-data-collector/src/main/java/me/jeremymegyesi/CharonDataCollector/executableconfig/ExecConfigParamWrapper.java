package me.jeremymegyesi.CharonDataCollector.executableconfig;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExecConfigParamWrapper<T> {
    private T value;

    public ExecConfigParamWrapper(T value) {
        this.value = value;
    }
}
