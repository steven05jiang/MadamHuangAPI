package com.madamhuang.api.restful_api.model;

public class APIPaginatedRequest extends APIRequest {

    private int page = -1;

    private int size = 3;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public static APIPaginatedRequest parse(String json) {
        return (APIPaginatedRequest) APIRequest.parse(json, APIPaginatedRequest.class);
    }

}
