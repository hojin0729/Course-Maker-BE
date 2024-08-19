package coursemaker.coursemaker.api.busanApi.service;

import coursemaker.coursemaker.api.busanApi.dto.BusanApiResponse;

public interface BusanApiService {

    BusanApiResponse initialUpdate();
    void busanConvertAndSaveToDestination();
}