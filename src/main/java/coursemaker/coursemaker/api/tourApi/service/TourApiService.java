package coursemaker.coursemaker.api.tourApi.service;

import coursemaker.coursemaker.api.busanApi.dto.BusanApiResponse;
import coursemaker.coursemaker.api.tourApi.dto.TourApiResponse;
import coursemaker.coursemaker.api.tourApi.entity.TourApi;

import java.util.List;
import java.util.Optional;

public interface TourApiService {

    TourApiResponse updateAndGetTour();
    Optional<TourApi> getTourById(Long id);
    List<TourApi> getAllTours();
}