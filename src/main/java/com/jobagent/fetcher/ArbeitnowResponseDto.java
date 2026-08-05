package com.jobagent.fetcher;

import java.util.List;

public class ArbeitnowResponseDto {

    private List<ArbeitnowJobDto> data;

    public List<ArbeitnowJobDto> getData() { return data; }
    public void setData(List<ArbeitnowJobDto> data) { this.data = data; }
}