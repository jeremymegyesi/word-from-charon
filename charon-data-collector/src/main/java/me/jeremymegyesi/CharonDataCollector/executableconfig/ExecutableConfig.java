package me.jeremymegyesi.CharonDataCollector.executableconfig;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "scheduled_exec_config")
public class ExecutableConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String configName;
    private String execServiceClassName;
    private String utilityType;

    @Convert(converter = ExecConfigParamWrapperConverter.class)
    @Column(columnDefinition = "jsonb")
    private ExecConfigParamWrapper<?> params;
    
    // TODO - create a new table for execution history instead of this
    // public LocalDateTime lastExecuted;
}
