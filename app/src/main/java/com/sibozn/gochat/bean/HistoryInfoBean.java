package com.sibozn.gochat.bean;

/**
 * Created by Administrator on 2016/8/16.
 */
public class HistoryInfoBean {
    public HistoryInfoBean(String id, String from, String to, String data, String type, String time, String uid,
                           String photo, String sex, String age, String country, String city) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.data = data;
        this.type = type;
        this.time = time;
        this.uid = uid;
        this.photo = photo;
        this.sex = sex;
        this.age = age;
        this.country = country;
        this.city = city;
    }

    /**
     * id : 772
     * from : 441910462@qq.com
     * to : 290896625@qq.com
     * data : fffffgg
     * type : text
     * time : 2016-08-16 13:04:26
     * uid : 6bdb54625939514b
     * photo : http://media2.giphy.com/media/1cQMlSncDxzwc/100w.gif
     * sex : female
     * age : 256
     * country : China
     * city :
     */

    private String id;
    private String from;
    private String to;
    private String data;
    private String type;
    private String time;
    private String uid;
    private String photo;
    private String sex;
    private String age;
    private String country;
    private String city;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
