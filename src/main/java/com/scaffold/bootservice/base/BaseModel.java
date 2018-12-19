package com.scaffold.bootservice.base;

import javax.persistence.Id;
import java.io.Serializable;

public class BaseModel implements Serializable {

    @Id
    private Integer id;


}
