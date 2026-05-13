package ru.filimonov.eventhandlerservice.mapper.submapper;

import model.dto.RouteData;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import ru.filimonov.eventhandlerservice.dto.response.subdto.RouteResponse;
import ru.filimonov.eventhandlerservice.entity.Route;

@Mapper(componentModel = ComponentModel.SPRING)
public interface RouteMapper {
  Route toRouteEntity(RouteData routeData);

  RouteResponse toRouteResponse(Route route);
}
