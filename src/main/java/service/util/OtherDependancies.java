package service.util;

import org.modelmapper.ModelMapper;

public class OtherDependancies {
    private final static OtherDependancies instance = new OtherDependancies();

    private final ModelMapper mapper;

    private OtherDependancies(){
        this.mapper = new ModelMapper();
    }

    public static OtherDependancies getInstance(){
        return instance;
    }

    public ModelMapper getModelMapper(){
        return this.mapper;
    }
}
