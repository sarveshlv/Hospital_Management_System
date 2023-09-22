package com.Capg.BedModule.Model;

import com.Capg.BedModule.Constants.BedStatus;
import com.Capg.BedModule.Constants.BedType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "beds")
public class Bed {
    @Id
    private String bedId;
    private String hospitalId;
    private BedType bedType;
    private BedStatus bedStatus;
    private Double costPerDay;

}
