package com.sibozn.gochat.bean;

/**
 * Created by Administrator on 2016/8/12.
 */
public class GifsBean {
    private String id;
    private String fixed_height_width;
    private String fixed_height_url;
    private String fixed_height_height;
    private String fixed_height_size;

    public GifsBean(String id, String fixed_height_width, String fixed_height_url, String
            fixed_height_height, String fixed_height_size) {
        this.id = id;
        this.fixed_height_width = fixed_height_width;
        this.fixed_height_url = fixed_height_url;
        this.fixed_height_height = fixed_height_height;
        this.fixed_height_size = fixed_height_size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getFixed_height_width() {
        return fixed_height_width;
    }

    public void setFixed_height_width(String fixed_height_width) {
        this.fixed_height_width = fixed_height_width;
    }

    public String getFixed_height_url() {
        return fixed_height_url;
    }

    public void setFixed_height_url(String fixed_height_url) {
        this.fixed_height_url = fixed_height_url;
    }

    public String getFixed_height_height() {
        return fixed_height_height;
    }

    public void setFixed_height_height(String fixed_height_height) {
        this.fixed_height_height = fixed_height_height;
    }

    public String getFixed_height_size() {
        return fixed_height_size;
    }

    public void setFixed_height_size(String fixed_height_size) {
        this.fixed_height_size = fixed_height_size;
    }
}
