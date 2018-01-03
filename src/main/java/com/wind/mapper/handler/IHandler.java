package com.wind.mapper.handler;

import com.glaurung.batMap.controller.MapperEngine;

public interface IHandler {

    void setMapperEngine(MapperEngine mapperEngine);

    void beginHandle();
}
