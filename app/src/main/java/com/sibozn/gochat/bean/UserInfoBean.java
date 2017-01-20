package com.sibozn.gochat.bean;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Administrator on 2016/8/5.
 */
public class UserInfoBean {

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "email='" + email + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", data='" + data + '\'' +
                ", type='" + type + '\'' +
                ", uid='" + uid + '\'' +
                ", photo='" + photo + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public UserInfoBean(String email, String lng, String lat, String data, String type, String uid, String photo,
                        String sex, String age, String country, String city,Marker marker) {
        this.email = email;
        this.lng = lng;
        this.lat = lat;
        this.data = data;
        this.type = type;
        this.uid = uid;
        this.photo = photo;
        this.sex = sex;
        this.age = age;
        this.country = country;
        this.city = city;
        this.marker = marker;
    }

    /**
     * email : qin4571@qq.com
     * lng : 39.98123848
     * lat : 116.30683690
     * data : hi
     * type : text
     * uid :
     * photo : http://test.sibozn.com/gochat/f_img/20160720/1469030269ecdf85112d653eae2d53130053d39fd3.png
     * sex :
     * age : 0
     * country : China
     * city : Guanglian
     */

    private String email;
    private String lng;
    private String lat;
    private String data;
    private String type;
    private String uid;
    private String photo;
    private String sex;
    private String age;
    private String country;
    private String city;
    private Marker marker;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
