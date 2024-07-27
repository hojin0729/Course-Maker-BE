package coursemaker.coursemaker.api.busanApi.service;

import coursemaker.coursemaker.api.busanApi.dto.BusanApiResponse;
import coursemaker.coursemaker.api.busanApi.entity.BusanApi;

import java.util.List;
import java.util.Optional;

public interface BusanApiService {

    BusanApiResponse updateAndGetTour();
    Optional<BusanApi> getTourById(Long id);
    List<BusanApi> getAllTours();
    BusanApiResponse initialUpdate();
    void busanConvertAndSaveToDestination();
}