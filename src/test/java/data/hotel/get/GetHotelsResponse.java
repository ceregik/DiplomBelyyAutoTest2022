package data.hotel.get;

import java.util.List;

public class GetHotelsResponse {
    private List<Hotel> hotels;

    public List<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }

    public GetHotelsResponse(List<Hotel> hotels) {
        this.hotels = hotels;
    }

    @Override
    public String toString() {
        return "GetHotelsResponse{" +
                "hotels=" + hotels +
                '}';
    }
}
