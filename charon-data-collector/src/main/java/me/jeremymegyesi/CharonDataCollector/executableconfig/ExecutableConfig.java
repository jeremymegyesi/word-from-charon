package me.jeremymegyesi.CharonDataCollector.executableconfig;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ExecutableConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public String configName;
    public String execServiceClassName;
    // TODO - create a new table for execution history instead of this
    // public LocalDateTime lastExecuted;
}
