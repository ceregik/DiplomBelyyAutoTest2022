package data.hotel.add;

import java.util.Objects;

public class AddHotelRequest {

    private String name;
    private String city;
    private String address;
    private String description;
    private Integer cost;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddHotelRequest that = (AddHotelRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(city, that.city) && Objects.equals(address, that.address) && Objects.equals(description, that.description) && Objects.equals(cost, that.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, city, address, description, cost);
    }

    @Override
    public String toString() {
        return "AddHotelRequest{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                '}';
    }
}
