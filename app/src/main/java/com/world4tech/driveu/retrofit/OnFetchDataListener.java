package com.world4tech.driveu.retrofit;

import com.world4tech.driveu.models.NewsHeadlines;

import java.util.List;

public interface OnFetchDataListener<NewApiResponse> {
    void onFetchData(List<NewsHeadlines> list, String message);
    void onError(String message);
}
