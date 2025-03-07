package com.led.broker.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "counters")
public class ControladorId {

        @Id
        private String id;
        private long sequenceValue;
}
